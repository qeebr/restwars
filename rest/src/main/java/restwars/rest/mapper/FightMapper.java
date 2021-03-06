package restwars.rest.mapper;

import com.google.common.base.Preconditions;
import restwars.model.fight.FightWithPlanetAndPlayer;
import restwars.restapi.dto.ship.FightResponse;

/**
 * Maps fight entities to DTOs and vice versa.
 */
public final class FightMapper {
    private FightMapper() {
    }

    public static FightResponse fromFight(FightWithPlanetAndPlayer fight) {
        Preconditions.checkNotNull(fight, "fight");

        return new FightResponse(
                fight.getFight().getId().toString(), fight.getPlanet().getLocation().toString(),
                fight.getAttacker().getUsername(), fight.getDefender().getUsername(),
                ShipMapper.fromShips(fight.getFight().getAttackingShips()),
                ShipMapper.fromShips(fight.getFight().getDefendingShips()),
                ShipMapper.fromShips(fight.getFight().getRemainingAttackerShips()),
                ShipMapper.fromShips(fight.getFight().getRemainingDefenderShips()), fight.getFight().getRound(),
                ResourcesMapper.fromResources(fight.getFight().getLoot())
        );
    }
}
