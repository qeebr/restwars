package restwars.rest.api.ship;

import org.hibernate.validator.constraints.NotEmpty;
import restwars.service.ship.FlightType;
import restwars.service.ship.ShipType;
import restwars.service.ship.Ships;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

public class CreateFlightRequest {
    public static class Ship {
        @NotEmpty
        private String type;

        @Min(1)
        private long count;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    @NotEmpty
    private String type;

    @Valid
    @NotEmpty
    private List<Ship> ships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public FlightType getParsedType() {
        return FlightType.valueOf(type);
    }

    public Ships getParsedShips() {
        return new Ships(getShips().stream()
                .map(s -> new restwars.service.ship.Ship(ShipType.valueOf(s.getType()), s.getCount()))
                .collect(Collectors.toList())
        );
    }
}
