package restwars.storage.mapper;

import org.jooq.Record;
import restwars.service.planet.Location;
import restwars.service.planet.Planet;
import restwars.service.resource.Resources;

import static restwars.storage.jooq.Tables.PLANET;

public final class PlanetMapper {
    private PlanetMapper() {
    }

    public static Planet fromRecord(Record record) {
        return new Planet(
                record.getValue(PLANET.ID), new Location(record.getValue(PLANET.LOCATION_GALAXY),
                record.getValue(PLANET.LOCATION_SOLAR_SYSTEM), record.getValue(PLANET.LOCATION_PLANET)),
                record.getValue(PLANET.OWNER_ID), new Resources(record.getValue(PLANET.CRYSTALS),
                record.getValue(PLANET.GAS), record.getValue(PLANET.ENERGY))
        );
    }
}
