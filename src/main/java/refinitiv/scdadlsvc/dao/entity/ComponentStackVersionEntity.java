package refinitiv.scdadlsvc.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import refinitiv.scdadlsvc.dao.entity.jntbl.ComponentConfigurationEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@ToString(exclude = {"componentConfiguration"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "component_stack_version")
@Entity
public class ComponentStackVersionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String version;

    @Column
    private Boolean validated;

    @Embedded
    private Metadata metadata;

    @ManyToOne
    @JoinColumn(name = "component_stack_id")
    private ComponentStackEntity componentStack;

    @OneToMany(mappedBy = "componentStackVersion")
    private List<ComponentConfigurationEntity> componentConfiguration;
}
