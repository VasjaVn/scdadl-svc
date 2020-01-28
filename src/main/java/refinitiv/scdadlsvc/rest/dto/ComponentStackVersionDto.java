package refinitiv.scdadlsvc.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import refinitiv.scdadlsvc.dao.entity.ComponentStackVersionEntity;
import refinitiv.scdadlsvc.rest.dto.jntbl.ComponentConfigurationDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentStackVersionDto implements Serializable {
    @JsonProperty
    private Long id;

    @JsonProperty
    private String version;

    @JsonProperty
    private Boolean validated;

    @JsonProperty
    private Date puppetLastUsed;

    @JsonProperty
    private ComponentStackDto componentStack;

    private List<ComponentConfigurationDto> componentConfiguration;

    public static ComponentStackVersionDto fromEntity(ComponentStackVersionEntity entity) {
        return ComponentStackVersionDto.builder()
                .id(entity.getId())
                .version(entity.getVersion())
                .validated(entity.getValidated())
                //.puppetLastUsed(entity.getPuppetLastUsed())
                .componentStack(ComponentStackDto.fromEntity(entity.getComponentStack()))
                .componentConfiguration(entity.getComponentConfiguration().stream().map(ComponentConfigurationDto::fromEntity).collect(Collectors.toList()))
                .build();
    }
}

/**
 {
 "id": 54,
 "version": "2.1.0",
 "validated": true,
 "puppetLastUsed": "2020-01-27T21:22:00.394Z",

 "componentStack": {
 "id": 102,
 "name": "EAS_Stack",
 "nextAutoVerStartAt": "2.1.0"
 },

 "componentConfiguration": [
 {
 "id": 201,
 "componentVersion": {
 "id": 101,
 "packageUrl": "string",
 "version": "<major>.<minor>.<patch>-<build>-<label>",
 "qualityGrade": "string",
 "versionValidated": "new",
 "versionValidationError": "string",
 "versionAvoid": "Bad Release"
 },
 "triggerNewStack": true,
 "minQualityGrade": "string"
 }
 ]
 }
 */