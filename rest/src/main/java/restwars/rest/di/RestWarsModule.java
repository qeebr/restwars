package restwars.rest.di;

import com.google.common.base.Preconditions;
import dagger.Module;
import dagger.Provides;
import io.dropwizard.db.ManagedDataSource;
import restwars.service.ServiceModule;
import restwars.service.UniverseConfiguration;
import restwars.service.unitofwork.UnitOfWorkService;
import restwars.storage.JooqDAOModule;
import restwars.storage.jooq.JooqUnitOfWorkService;

@Module(injects = CompositionRoot.class, includes = {
        JooqDAOModule.class, ServiceModule.class
})
public class RestWarsModule {
    private final UniverseConfiguration universeConfiguration;
    private final ManagedDataSource managedDataSource;

    public RestWarsModule(UniverseConfiguration universeConfiguration, ManagedDataSource managedDataSource) {
        this.managedDataSource = Preconditions.checkNotNull(managedDataSource, "managedDataSource");
        this.universeConfiguration = Preconditions.checkNotNull(universeConfiguration, "universeConfiguration");
    }

    @Provides
    UnitOfWorkService providesUnitOfWorkService() {
        return new JooqUnitOfWorkService(managedDataSource);
    }

    @Provides
    UniverseConfiguration providesUniverseConfiguration() {
        return universeConfiguration;
    }
}