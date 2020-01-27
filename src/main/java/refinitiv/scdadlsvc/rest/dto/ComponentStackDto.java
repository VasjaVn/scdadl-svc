package refinitiv.scdadlsvc.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import refinitiv.scdadlsvc.dao.entity.ComponentStackEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentStackDto implements Serializable {
    @JsonProperty
    private Long id;

    @NotNull(message = "ComponentStackDto.name is null")
    @JsonProperty
    private String name;

    @NotNull(message = "ComponentStackDto.nextAutoVerStartAt is null")
    @JsonProperty
    private String nextAutoVerStartAt;

    public static ComponentStackDto fromEntity(ComponentStackEntity componentStackEntity) {
        return ComponentStackDto.builder()
                .id(componentStackEntity.getId())
                .name(componentStackEntity.getName())
                .nextAutoVerStartAt(componentStackEntity.getNextAutoVerStartAt())
                .build();
    }
}
