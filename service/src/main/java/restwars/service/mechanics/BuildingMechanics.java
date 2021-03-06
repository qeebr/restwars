package restwars.service.mechanics;

import restwars.model.building.BuildingType;
import restwars.model.resource.Resources;
import restwars.model.techtree.Prerequisites;

/**
 * Building mechanics.
 */
public interface BuildingMechanics {
    /**
     * Calculates the build cost for the given building and level.
     *
     * @param type  Building.
     * @param level Level. Must be > 0.
     * @return Build cost.
     */
    Resources calculateBuildCost(BuildingType type, int level);

    /**
     * Calculates the resources gather rate for a command center with the given level.
     *
     * @param level Level. Must be > 0.
     * @return Resources gathered per round.
     */
    Resources calculateCommandCenterResourcesGathered(int level);

    /**
     * Calculates the build time for the given building and level.
     *
     * @param type  Building.
     * @param level Level. Must be > 0.
     * @return Build time in rounds.
     */
    int calculateBuildTime(BuildingType type, int level);

    /**
     * Calculates the crystal gather rate for a crystal mine with the given level.
     *
     * @param level Level of the crystal mine.
     * @return Crystals gathered per round.
     */
    int calculateCrystalsGathered(int level);

    /**
     * Calculates the gas gather rate for a gas refinery with the given level.
     *
     * @param level Level of the gas refinery.
     * @return Gas gathered per round.
     */
    int calculateGasGathered(int level);

    /**
     * Calculates the energy gather rate for solar panels with the given level.
     *
     * @param level Level of the solar panels.
     * @return Energy gathered per round.
     */
    int calculateEnergyGathered(int level);

    /**
     * Calculates the building build time speedup for a command center with the given level.
     * <p>
     * If the build time speedup is for example 0.1, the building will be finished 10% faster.
     *
     * @param level Level of the command center.
     * @return Building build time speedup.
     */
    double calculateBuildingBuildTimeSpeedup(int level);

    /**
     * Calculates the research time speedup for a research center with the given level.
     *
     * @param level Level of the research center.
     * @return Research time speedup.
     */
    double calculateResearchTimeSpeedup(int level);

    /**
     * Calculates the ship build time speedup for a shipyard with the given level.
     *
     * @param level Level of the shipyard.
     * @return Ship build time speedup.
     */
    double calculateShipBuildTimeSpeedup(int level);

    /**
     * Calculates the scan range of a telescope with the given level.
     *
     * @param level Level of the telescope.
     * @return Scan range in solar systems.
     */
    int calculateScanRange(int level);

    /**
     * Calculates the flight detection range of a telescope with the given level.
     *
     * @param level Level of the telescope.
     * @return Flight detection rate in rounds.
     */
    int calculateFlightDetectionRange(int level);

    /**
     * Calculates the fleet size variance of a telescope with the given level.
     * <p>
     * Guaranteed to be >= 0.0.
     *
     * @param level Level of the telescope.
     * @return Fleet size variance.
     */
    double calculateFleetSizeVariance(int level);

    /**
     * Returns the prerequisites for the given building.
     *
     * @param type Building.
     * @return Prerequisites.
     */
    Prerequisites getPrerequisites(BuildingType type);

    /**
     * Calculates the points for the given building.
     *
     * @param type  Type of building.
     * @param level Building level.
     * @return Points.
     */
    long calculatePointsForBuilding(BuildingType type, int level);

    /**
     * Calculates the points for the given construction site.
     *
     * @param type  Type of construction site.
     * @param level Building level.
     * @return Points.
     */
    long calculatePointsForConstructionSite(BuildingType type, int level);
}
