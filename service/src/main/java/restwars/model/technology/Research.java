package restwars.model.technology;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import restwars.model.resource.Resources;

import java.util.UUID;

public class Research {
    private final UUID id;

    private final TechnologyType type;

    private final int level;

    private final long started;

    private final long done;

    private final UUID planetId;

    private final Resources researchCost;

    private final UUID playerId;

    public Research(UUID id, TechnologyType type, int level, long started, long done, UUID planetId, UUID playerId, Resources researchCost) {
        Preconditions.checkArgument(level > 0, "level must be > 0");
        Preconditions.checkArgument(started > 0, "started must be > 0");
        Preconditions.checkArgument(done > 0, "done must be > 0");

        this.id = Preconditions.checkNotNull(id, "id");
        this.type = Preconditions.checkNotNull(type, "type");
        this.level = level;
        this.started = started;
        this.done = done;
        this.planetId = Preconditions.checkNotNull(planetId, "planetId");
        this.playerId = Preconditions.checkNotNull(playerId, "playerId");
        this.researchCost = Preconditions.checkNotNull(researchCost, "researchCost");
    }

    public UUID getId() {
        return id;
    }

    public TechnologyType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public long getStarted() {
        return started;
    }

    public long getDone() {
        return done;
    }

    public UUID getPlanetId() {
        return planetId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Resources getResearchCost() {
        return researchCost;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("level", level)
                .add("started", started)
                .add("done", done)
                .add("planetId", planetId)
                .add("researchCost", researchCost)
                .add("playerId", playerId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Research that = (Research) o;

        return Objects.equal(this.id, that.id) &&
                Objects.equal(this.type, that.type) &&
                Objects.equal(this.level, that.level) &&
                Objects.equal(this.started, that.started) &&
                Objects.equal(this.done, that.done) &&
                Objects.equal(this.planetId, that.planetId) &&
                Objects.equal(this.researchCost, that.researchCost) &&
                Objects.equal(this.playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, level, started, done, planetId,
                researchCost, playerId);
    }
}
