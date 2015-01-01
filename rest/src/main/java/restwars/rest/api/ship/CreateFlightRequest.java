package restwars.rest.api.ship;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import restwars.service.ship.FlightType;
import restwars.service.ship.ShipType;
import restwars.service.ship.Ships;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(description = "Creates a flight")
public class CreateFlightRequest {
    @NotEmpty
    @ApiModelProperty(value = "Flight type", required = true)
    private String type;

    @Valid
    @NotEmpty
    @ApiModelProperty(value = "Ships on the flight", required = true)
    private List<ShipRequest> ships;

    @Min(0)
    @ApiModelProperty(value = "Amount of crystals in cargo", required = true)
    private long cargoCrystals;

    @Min(0)
    @ApiModelProperty(value = "Amount of gas in cargo", required = true)
    private long cargoGas;

    @Min(0)
    @ApiModelProperty(value = "Amount of energy in cargo", required = true)
    private long cargoEnergy;

    public long getCargoCrystals() {
        return cargoCrystals;
    }

    public void setCargoCrystals(long cargoCrystals) {
        this.cargoCrystals = cargoCrystals;
    }

    public void setCargoGas(long cargoGas) {
        this.cargoGas = cargoGas;
    }

    public void setCargoEnergy(long cargoEnergy) {
        this.cargoEnergy = cargoEnergy;
    }

    public long getCargoGas() {
        return cargoGas;
    }

    public long getCargoEnergy() {
        return cargoEnergy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ShipRequest> getShips() {
        return ships;
    }

    public void setShips(List<ShipRequest> ships) {
        this.ships = ships;
    }

    @ApiModelProperty(hidden = true)
    public FlightType getParsedType() {
        return FlightType.valueOf(type);
    }

    @ApiModelProperty(hidden = true)
    public Ships getParsedShips() {
        return new Ships(getShips().stream()
                .map(s -> new restwars.service.ship.Ship(ShipType.valueOf(s.getType()), s.getAmount()))
                .collect(Collectors.toList())
        );
    }
}
