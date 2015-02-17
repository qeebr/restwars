package restwars.rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.cache.CacheBuilderSpec;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import dagger.ObjectGraph;
import io.dropwizard.Application;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.MetaBroadcaster;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restwars.model.UniverseConfiguration;
import restwars.model.resource.Resources;
import restwars.rest.configuration.RestwarsConfiguration;
import restwars.rest.di.RestWarsModule;
import restwars.rest.doc.ModelConverter;
import restwars.rest.doc.SwaggerFilter;
import restwars.rest.integration.database.UnitOfWorkResourceMethodDispatchAdapter;
import restwars.rest.websocket.WebsocketResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class RestwarsApplication extends Application<RestwarsConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestwarsApplication.class);
    public static final String REALM = "RESTwars";

    public static void main(String[] args) throws Exception {
        try {
            new RestwarsApplication().run(args);
        } catch (Exception e) {
            LOGGER.error("Exception while starting the application", e);
            throw e;
        }
    }

    @Override
    public void initialize(Bootstrap<RestwarsConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<RestwarsConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(RestwarsConfiguration configuration) {
                return configuration.getDatabase();
            }
        });
    }

    @Override
    public void run(RestwarsConfiguration configuration, Environment environment) throws Exception {
        // Write datetimes as ISO8601
        environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Start connection pool
        ManagedDataSource dataSource = configuration.getDatabase().build(environment.metrics(), "datasource");
        environment.lifecycle().manage(dataSource);

        UniverseConfiguration universeConfiguration = new UniverseConfiguration(
                configuration.getGalaxies(), configuration.getSolarSystems(), configuration.getPlanets(),
                new Resources(1000L, 200L, 200L), configuration.getRoundTime()
        );

        ObjectGraph objectGraph = ObjectGraph.create(new RestWarsModule(universeConfiguration, dataSource, configuration.getPasswordIterations()));
        CompositionRoot compositionRoot = objectGraph.get(CompositionRoot.class);

        registerJerseyHooks(environment, compositionRoot);

        environment.jersey().register(new BasicAuthProvider<>(new CachingAuthenticator<>(environment.metrics(), compositionRoot.getPlayerAuthenticator(), CacheBuilderSpec.parse(configuration.getPasswordCache())), REALM));
        environment.jersey().register(compositionRoot.getRootResource());
        environment.jersey().register(compositionRoot.getSystemResource());
        environment.jersey().register(compositionRoot.getPlayerResource());
        environment.jersey().register(compositionRoot.getPlanetResource());
        environment.jersey().register(compositionRoot.getTechnologyResource());
        environment.jersey().register(compositionRoot.getEventResource());
        environment.jersey().register(compositionRoot.getFightResource());
        environment.jersey().register(compositionRoot.getFlightResource());
        environment.jersey().register(compositionRoot.getMetadataResource());

        // Initialize swagger documentation
        registerSwagger(environment, configuration);

        registerCorsFilter(environment);

        Clock clock = compositionRoot.getClock();
        registerAtmosphere(environment, clock);

        environment.lifecycle().manage(clock);
    }

    private void registerAtmosphere(Environment environment, Clock clock) {
        AtmosphereServlet atmosphereServlet = new AtmosphereServlet();
        atmosphereServlet.framework().addInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, WebsocketResource.class.getPackage().getName());
        atmosphereServlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");

        ServletRegistration.Dynamic servlet = environment.servlets().addServlet("atmosphere", atmosphereServlet);
        servlet.addMapping("/websocket/*");

        MetaBroadcaster broadcaster = atmosphereServlet.framework().metaBroadcaster();

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {
                clock.setNextRoundCallback(
                        new Runnable() {
                            @Override
                            public void run() {
                                broadcaster.broadcastTo("/*", "NEXT ROUND!");
                            }
                        }
                );
            }

            @Override
            public void stop() throws Exception {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void registerJerseyHooks(Environment environment, CompositionRoot compositionRoot) {
        environment.jersey().getResourceConfig().getResourceFilterFactories().add(compositionRoot.getLockingFilter());
        environment.jersey().register(new UnitOfWorkResourceMethodDispatchAdapter(compositionRoot.getUnitOfWorkService()));
    }

    private void registerSwagger(Environment environment, RestwarsConfiguration configuration) {
        SwaggerConfig swagger = ConfigFactory.config();
        swagger.setApiVersion("1.0.0");
        swagger.setBasePath(configuration.getPublicUrl());

        environment.jersey().register(new ApiListingResourceJSON());
        environment.jersey().register(new ResourceListingProvider());
        environment.jersey().register(new ApiDeclarationProvider());
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        ClassReaders.setReader(new DefaultJaxrsApiReader());

        FilterFactory.setFilter(new SwaggerFilter());
        ModelConverter.register();
    }

    private void registerCorsFilter(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
    }
}
