package refinitiv.scdadlsvc.rest.dto.jntbl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import refinitiv.scdadlsvc.dao.entity.enums.QualityGrade;
import refinitiv.scdadlsvc.dao.entity.jntbl.ComponentConfigurationEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentVersionDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentConfigurationDto implements Serializable {
    @JsonProperty
    private Long id;

    @JsonProperty
    private ComponentVersionDto componentVersion;

    @JsonProperty
    private Boolean triggerNewStack;

    @JsonProperty
    private QualityGrade minQualityGrade;

    public static ComponentConfigurationDto fromEntity(ComponentConfigurationEntity entity) {
        return ComponentConfigurationDto.builder()
                .id(entity.getId())
                //.componentVersion(ComponentVersionDto.fromEntity(entity.getComponentVersion()))
                .triggerNewStack(entity.getTriggerNewStackVer())
                .minQualityGrade(entity.getMinGrade())
                .build();
    }
}
