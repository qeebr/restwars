package restwars.restapi.dto.metadata;

import com.google.common.base.Objects;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import restwars.restapi.dto.ResourcesResponse;

@ApiModel(description = "Building metadata")
public class BuildingMetadataResponse {
    @ApiModelProperty(value = "Building type", required = true)
    private String type;

    @ApiModelProperty(value = "Level", required = true)
    private int level;

    @ApiModelProperty(value = "Build time in rounds", required = true)
    private long buildTime;

    @ApiModelProperty(value = "Build cost", required = true)
    private ResourcesResponse buildCost;

    public BuildingMetadataResponse() {
    }

    public BuildingMetadataResponse(String type, int level, long buildTime, ResourcesResponse buildCost) {
        this.type = type;
        this.level = level;
        this.buildTime = buildTime;
        this.buildCost = buildCost;
    }

    public String getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public ResourcesResponse getBuildCost() {
        return buildCost;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public void setBuildCost(ResourcesResponse buildCost) {
        this.buildCost = buildCost;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", type)
                .add("level", level)
                .add("buildTime", buildTime)
                .add("buildCost", buildCost)
                .toString();
    }
}