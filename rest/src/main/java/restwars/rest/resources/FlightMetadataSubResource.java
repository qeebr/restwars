package restwars.rest.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import restwars.model.flight.FlightType;
import restwars.rest.mapper.FlightMapper;
import restwars.restapi.dto.metadata.FlightTypesMetadataResponse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Subresource for flight metadata.
 */
@Api(value = "/flight", hidden = true)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlightMetadataSubResource {
    @Inject
    public FlightMetadataSubResource() {
    }

    /**
     * Lists all flight types.
     *
     * @return All flight types.
     */
    @GET
    @Path("/type")
    @ApiOperation("Lists all flight types")
    public FlightTypesMetadataResponse flightTypes() {
        return new FlightTypesMetadataResponse(
                Stream.of(FlightType.values()).map(FlightMapper::fromFlightType).collect(Collectors.toList())
        );
    }
}
