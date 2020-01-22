package refinitiv.scdadlsvc.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentDto implements Serializable {
    @JsonProperty
    private Long id;

    @NotNull(message = "ComponentDto.name is null")
    @JsonProperty("name")
    private String componentName;

    @NotNull(message = "ComponentDto.componentGroup is null")
    @JsonProperty("componentGroup")
    private String componentGroupName;

    @NotNull(message = "ComponentDto.platform is null")
    @JsonProperty("platform")
    private String platformName;

    @NotNull(message = "ComponentDto.assetInsightId is null")
    @JsonProperty
    private Long assetInsightId;

    public static ComponentDto fromEntity(ComponentEntity componentEntity) {
        return ComponentDto.builder()
                .id(componentEntity.getId())
                .componentName(componentEntity.getName())
                .componentGroupName(componentEntity.getComponentGroup().getName())
                .platformName(componentEntity.getComponentGroup().getPlatform().getName())
                .assetInsightId(componentEntity.getAssetInsightId())
                .build();
    }
}
