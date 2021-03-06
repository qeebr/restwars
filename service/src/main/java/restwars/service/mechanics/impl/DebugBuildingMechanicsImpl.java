package restwars.service.mechanics.impl;

import com.google.common.base.Preconditions;
import restwars.model.UniverseConfiguration;
import restwars.model.building.BuildingType;
import restwars.model.resource.Resources;
import restwars.model.techtree.Prerequisites;
import restwars.service.mechanics.BuildingMechanics;

/**
 * Debug mechanics for buildings. Overrides some mechanics when enabled in universe configuration.
 */
public class DebugBuildingMechanicsImpl implements BuildingMechanics {
    private final BuildingMechanics delegate;
    private final UniverseConfiguration universeConfiguration;

    public DebugBuildingMechanicsImpl(UniverseConfiguration universeConfiguration, BuildingMechanics delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "delegate");
        this.universeConfiguration = Preconditions.checkNotNull(universeConfiguration, "universeConfiguration");
    }

    @Override
    public Resources calculateBuildCost(BuildingType type, int level) {
        if (universeConfiguration.isFreeBuildings()) {
            return Resources.NONE;
        } else {
            return delegate.calculateBuildCost(type, level);
        }
    }

    @Override
    public Resources calculateCommandCenterResourcesGathered(int level) {
        return delegate.calculateCommandCenterResourcesGathered(level);
    }

    @Override
    public int calculateBuildTime(BuildingType type, int level) {
        if (universeConfiguration.isSpeedUpBuildingConstructions()) {
            return 1;
        } else {
            return delegate.calculateBuildTime(type, level);
        }
    }

    @Override
    public int calculateCrystalsGathered(int level) {
        return delegate.calculateCrystalsGathered(level);
    }

    @Override
    public int calculateGasGathered(int level) {
        return delegate.calculateGasGathered(level);
    }

    @Override
    public int calculateEnergyGathered(int level) {
        return delegate.calculateEnergyGathered(level);
    }

    @Override
    public double calculateBuildingBuildTimeSpeedup(int level) {
        return delegate.calculateBuildingBuildTimeSpeedup(level);
    }

    @Override
    public double calculateResearchTimeSpeedup(int level) {
        return delegate.calculateResearchTimeSpeedup(level);
    }

    @Override
    public double calculateShipBuildTimeSpeedup(int level) {
        return delegate.calculateShipBuildTimeSpeedup(level);
    }

    @Override
    public int calculateScanRange(int level) {
        return delegate.calculateScanRange(level);
    }

    @Override
    public int calculateFlightDetectionRange(int level) {
        return delegate.calculateFlightDetectionRange(level);
    }

    @Override
    public double calculateFleetSizeVariance(int level) {
        return delegate.calculateFleetSizeVariance(level);
    }

    @Override
    public Prerequisites getPrerequisites(BuildingType type) {
        if (universeConfiguration.isNoBuildingPrerequisites()) {
            return Prerequisites.NONE;
        } else {
            return delegate.getPrerequisites(type);
        }
    }

    @Override
    public long calculatePointsForConstructionSite(BuildingType type, int level) {
        return delegate.calculatePointsForConstructionSite(type, level);
    }

    @Override
    public long calculatePointsForBuilding(BuildingType type, int level) {
        return delegate.calculatePointsForBuilding(type, level);
    }
}
