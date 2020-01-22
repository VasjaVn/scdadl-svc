package refinitiv.scdadlsvc.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "componentVersions", "metadata"})
@Builder
@Table(name = "component")
@Entity
public class ComponentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "asset_insight_id", nullable = false)
    private Long assetInsightId;

    @Embedded
    private Metadata metadata;

    @OneToMany(mappedBy = "component")
    private List<ComponentVersionEntity> componentVersions;

    @ManyToOne
    @JoinColumn(name = "component_group_fk")
    private ComponentGroupEntity componentGroup;
}
