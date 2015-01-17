package restwars.service.ship.impl.flighthandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restwars.service.event.EventService;
import restwars.service.infrastructure.RoundService;
import restwars.service.infrastructure.UUIDFactory;
import restwars.service.planet.Planet;
import restwars.service.planet.PlanetDAO;
import restwars.service.resource.Resources;
import restwars.service.ship.Flight;
import restwars.service.ship.FlightDAO;
import restwars.service.ship.HangarDAO;

import java.util.Optional;

public class TransportFlightHandler extends AbstractFlightHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportFlightHandler.class);

    public TransportFlightHandler(RoundService roundService, FlightDAO flightDAO, PlanetDAO planetDAO, HangarDAO hangarDAO, UUIDFactory uuidFactory, EventService eventService) {
        super(roundService, flightDAO, planetDAO, hangarDAO, uuidFactory, eventService);
    }

    @Override
    public void handle(Flight flight, long round) {
        assert flight != null;
        LOGGER.debug("Finishing transport flight");

        Optional<Planet> planet = getPlanetDAO().findWithLocation(flight.getDestination());
        if (planet.isPresent()) {
            if (planet.get().getOwnerId().equals(flight.getPlayerId())) {
                LOGGER.debug("Transfering cargo to planet {}", planet.get());

                Planet updatedPlanet = planet.get().withResources(planet.get().getResources().plus(flight.getCargo()));
                getPlanetDAO().update(updatedPlanet);

                createReturnFlight(flight, flight.getShips(), Resources.NONE);

                // Create event
                getEventService().createTransportArrivedEvent(flight.getPlayerId(), updatedPlanet.getId());
            } else {
                LOGGER.debug("Tried to transport to enemy planet {} , creating return flight", flight.getDestination());

                createReturnFlight(flight, flight.getShips(), flight.getCargo());
            }
        } else {
            LOGGER.debug("Planet {} isn't colonized, creating return flight", flight.getDestination());

            createReturnFlight(flight, flight.getShips(), flight.getCargo());
        }
    }
}
