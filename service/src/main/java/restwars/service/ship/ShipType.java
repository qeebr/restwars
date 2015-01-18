package restwars.service.ship;

import com.google.common.base.Preconditions;
import restwars.service.resource.Resources;
import restwars.service.techtree.Prerequisites;

public enum ShipType {
    MOSQUITO(0, new Resources(1, 1, 1), 1, 1.0, 1, 1, 1, 100, "Small and cheap fighter ship", Prerequisites.NONE),
    COLONY(1, new Resources(1, 1, 1), 1, 1.0, 1, 0, 5, 10000, "Colonizes planets", Prerequisites.NONE);

    private final int id;

    private final Resources buildCost;

    private final long buildTime;

    private final double flightCostModifier;

    private final int speed;

    private final int attackPoints;

    private final int defensePoints;

    private final long storageCapacity;

    private final String description;

    private final Prerequisites prerequisites;

    ShipType(int id, Resources buildCost, long buildTime, double flightCostModifier, int speed, int attackPoints, int defensePoints, long storageCapacity, String description, Prerequisites prerequisites) {
        this.id = id;
        Preconditions.checkArgument(buildTime > 0, "buildTime must be > 0");
        Preconditions.checkArgument(speed > 0, "speed must be > 0");
        Preconditions.checkArgument(attackPoints >= 0, "attackPoints must be >= 0");
        Preconditions.checkArgument(defensePoints >= 0, "defensePoints must be >= 0");
        Preconditions.checkArgument(storageCapacity >= 0, "storageCapacity must be >= 0");

        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
        this.buildCost = Preconditions.checkNotNull(buildCost, "buildCost");
        this.buildTime = buildTime;
        this.flightCostModifier = flightCostModifier;
        this.speed = speed;
        this.storageCapacity = storageCapacity;
        this.description = Preconditions.checkNotNull(description, "description");
        this.prerequisites = Preconditions.checkNotNull(prerequisites, "prerequisites");
    }

    public int getId() {
        return id;
    }

    public Resources getBuildCost() {
        return buildCost;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public double getFlightCostModifier() {
        return flightCostModifier;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    public long getStorageCapacity() {
        return storageCapacity;
    }

    public String getDescription() {
        return description;
    }

    public Prerequisites getPrerequisites() {
        return prerequisites;
    }

    public static ShipType fromId(int id) {
        for (ShipType type : ShipType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown id: " + id);
    }
}
