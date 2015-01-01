package restwars.rest.resources;

import com.google.common.base.Preconditions;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import restwars.rest.api.ship.ShipResponse;
import restwars.rest.resources.param.LocationParam;
import restwars.rest.util.Helper;
import restwars.service.planet.Planet;
import restwars.service.planet.PlanetService;
import restwars.service.player.Player;
import restwars.service.ship.Ship;
import restwars.service.ship.ShipService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import java.util.List;

@Api(value = "/{location}/ship", hidden = true)
public class ShipSubResource {
    private final ShipService shipService;
    private final PlanetService planetService;

    @Inject
    public ShipSubResource(ShipService shipService, PlanetService planetService) {
        this.planetService = Preconditions.checkNotNull(planetService, "planetService");
        this.shipService = Preconditions.checkNotNull(shipService, "shipService");
    }

    @GET
    @ApiOperation("Lists all ships on the planet")
    public List<ShipResponse> getShips(
            @Auth @ApiParam(access = "internal") Player player,
            @PathParam("location") @ApiParam("Planet location") LocationParam location
    ) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(location, "location");

        Planet planet = Helper.findPlanetWithLocationAndOwner(planetService, location.getValue(), player);
        List<Ship> ships = shipService.findShipsOnPlanet(planet).asList();

        return Helper.mapToList(ships, ShipResponse::fromShip);
    }
}
