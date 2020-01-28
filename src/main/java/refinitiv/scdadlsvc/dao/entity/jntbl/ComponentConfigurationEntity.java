package refinitiv.scdadlsvc.dao.entity.jntbl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import refinitiv.scdadlsvc.dao.entity.ComponentStackVersionEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.dao.entity.enums.QualityGrade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cmpntstackver_cmpntver")
public class ComponentConfigurationEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "component_stack_ver_id")
    private ComponentStackVersionEntity componentStackVersion;

    @ManyToOne
    @JoinColumn(name = "component_ver_id")
    private ComponentVersionEntity componentVersion;

    @Column(name = "trigger_new_stack_ver")
    private Boolean triggerNewStackVer;

    @Enumerated(value = STRING)
    @Column(name = "min_grade")
    private QualityGrade minGrade;
}
