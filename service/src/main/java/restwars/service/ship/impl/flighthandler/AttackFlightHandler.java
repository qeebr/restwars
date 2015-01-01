package restwars.service.ship.impl.flighthandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restwars.service.infrastructure.RoundService;
import restwars.service.infrastructure.UUIDFactory;
import restwars.service.planet.Planet;
import restwars.service.planet.PlanetDAO;
import restwars.service.resource.Resources;
import restwars.service.ship.*;

import java.util.Optional;

public class AttackFlightHandler extends AbstractFlightHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttackFlightHandler.class);

    private final FightCalculator fightCalculator;

    public AttackFlightHandler(RoundService roundService, FlightDAO flightDAO, PlanetDAO planetDAO, HangarDAO hangarDAO, UUIDFactory uuidFactory) {
        super(roundService, flightDAO, planetDAO, hangarDAO, uuidFactory);

        this.fightCalculator = new FightCalculator();
    }

    @Override
    public void handle(Flight flight) {
        assert flight != null;
        LOGGER.debug("Handling attack of flight {}", flight);

        Optional<Planet> planet = getPlanetDAO().findWithLocation(flight.getDestination());
        if (planet.isPresent()) {
            Hangar hangar = getOrCreateHangar(planet.get().getId(), planet.get().getOwnerId());

            Fight fight = fightCalculator.attack(flight.getShips(), hangar.getShips());

            // Update defenders hangar
            getHangarDAO().update(hangar.withShips(fight.getRemainingDefenderShips()));

            if (fight.getRemainingAttackerShips().isEmpty()) {
                LOGGER.debug("Attacker lost all ships");
                getFlightDAO().delete(flight);
            } else {
                Resources cargo = Resources.NONE;
                if (fight.getRemainingDefenderShips().isEmpty()) {
                    cargo = lootPlanet(planet.get(), fight.getRemainingAttackerShips());
                }

                createReturnFlight(flight, fight.getRemainingAttackerShips(), cargo);
            }
        } else {
            // Planet is not colonized, create return flight
            createReturnFlight(flight, flight.getShips(), flight.getCargo());
        }
    }

    private Resources lootPlanet(Planet planet, Ships ships) {
        long storageCapacity = getShipUtils().calculateStorageCapacity(ships);

        long lootCrystals = storageCapacity / 3;
        long lootGas = storageCapacity / 3;
        long lootEnergy = storageCapacity - lootCrystals - lootGas;

        // TODO - Gameplay: Implement a more greedy looting strategy
        lootCrystals = Math.min(planet.getResources().getCrystals(), lootCrystals);
        lootGas = Math.min(planet.getResources().getGas(), lootGas);
        lootEnergy = Math.min(planet.getResources().getEnergy(), lootEnergy);

        // Decrease resources on planet
        planet = planet.withResources(planet.getResources().minus(new Resources(lootCrystals, lootGas, lootEnergy)));
        getPlanetDAO().update(planet);

        Resources resources = new Resources(lootCrystals, lootGas, lootEnergy);
        LOGGER.debug("Looted {} from planet {}", resources, planet.getLocation());
        return resources;
    }
}