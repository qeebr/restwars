package restwars.restapi.dto.planet;

import com.google.common.base.Objects;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A planet")
public class PlanetResponse {
    @ApiModelProperty(value = "Location", required = true)
    private String location;

    @ApiModelProperty(value = "Amount of crystals", required = true)
    private long crystal;

    @ApiModelProperty(value = "Amount of gas", required = true)
    private long gas;

    @ApiModelProperty(value = "Amount of energy", required = true)
    private long energy;

    @ApiModelProperty(value = "Colonized in round", required = true)
    private long colonizedInRound;

    public PlanetResponse() {
    }

    public PlanetResponse(String location, long crystal, long gas, long energy, long colonizedInRound) {
        this.location = location;
        this.crystal = crystal;
        this.gas = gas;
        this.energy = energy;
        this.colonizedInRound = colonizedInRound;
    }

    public String getLocation() {
        return location;
    }

    public long getCrystal() {
        return crystal;
    }

    public long getGas() {
        return gas;
    }

    public long getEnergy() {
        return energy;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCrystal(long crystal) {
        this.crystal = crystal;
    }

    public void setGas(long gas) {
        this.gas = gas;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
    }

    public long getColonizedInRound() {
        return colonizedInRound;
    }

    public void setColonizedInRound(long colonizedInRound) {
        this.colonizedInRound = colonizedInRound;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("location", location)
                .add("crystal", crystal)
                .add("gas", gas)
                .add("energy", energy)
                .add("colonizedInRound", colonizedInRound)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanetResponse that = (PlanetResponse) o;

        return Objects.equal(this.location, that.location) &&
                Objects.equal(this.crystal, that.crystal) &&
                Objects.equal(this.gas, that.gas) &&
                Objects.equal(this.energy, that.energy) &&
                Objects.equal(this.colonizedInRound, that.colonizedInRound);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location, crystal, gas, energy, colonizedInRound);
    }
}
