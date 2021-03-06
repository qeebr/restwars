package restwars.model.technology;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.UUID;

public class Technology {
    private final UUID id;

    private final TechnologyType type;

    private final int level;

    private final UUID playerId;

    public Technology(UUID id, TechnologyType type, int level, UUID playerId) {
        Preconditions.checkArgument(level > 0, "level must be > 0");

        this.id = Preconditions.checkNotNull(id, "id");
        this.type = Preconditions.checkNotNull(type, "type");
        this.level = level;
        this.playerId = Preconditions.checkNotNull(playerId, "playerId");
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

    public UUID getPlayerId() {
        return playerId;
    }

    public Technology withLevel(int level) {
        return new Technology(id, type, level, playerId);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("level", level)
                .add("playerId", playerId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Technology that = (Technology) o;

        return Objects.equal(this.id, that.id) &&
                Objects.equal(this.type, that.type) &&
                Objects.equal(this.level, that.level) &&
                Objects.equal(this.playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, level, playerId);
    }
}
