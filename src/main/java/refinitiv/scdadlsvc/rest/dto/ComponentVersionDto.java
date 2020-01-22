package refinitiv.scdadlsvc.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.dao.entity.enums.Format;
import refinitiv.scdadlsvc.dao.entity.enums.QualityGrade;
import refinitiv.scdadlsvc.dao.entity.enums.VersionAvoid;
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
public class ComponentVersionDto implements Serializable {
    @JsonProperty
    private Long id;

    @NotNull(message = "ComponentVersionDto.version is null.")
    @JsonProperty
    private String version;

    @NotNull(message = "ComponentVersionDto.packageUrl is null.")
    @JsonProperty
    private String packageUrl;

    @NotNull(message = "ComponentVersionDto.format is null.")
    @JsonProperty
    private Format format;

    @NotNull(message = "ComponentVersionDto.qualityGrade is null.")
    @JsonProperty
    private QualityGrade qualityGrade;

    @NotNull(message = "ComponentVersionDto.versionValidated is null.")
    @JsonProperty
    private Boolean versionValidated;

    @NotNull(message = "ComponentVersionDto.versionAvoid is null.")
    @JsonProperty
    private VersionAvoid versionAvoid;

    @NotNull(message = "ComponentVersionDto.versionValidationError is null.")
    @JsonProperty
    private String versionValidationError;

    @JsonProperty
    private ComponentDto component;

    public static ComponentVersionDto fromEntity(ComponentVersionEntity componentVersionEntity) {
        return ComponentVersionDto.builder()
                .id(componentVersionEntity.getId())
                .version(componentVersionEntity.getVersion())
                .packageUrl(componentVersionEntity.getPackageUrl())
                .format(componentVersionEntity.getFormat())
                .qualityGrade(componentVersionEntity.getQualityGrade())
                .versionValidated(componentVersionEntity.getValidated())
                .versionValidationError(componentVersionEntity.getValidationError())
                .versionAvoid(componentVersionEntity.getVersionAvoid())
                .component(ComponentDto.fromEntity(componentVersionEntity.getComponent()))
                .build();
    }
}
