package refinitiv.scdadlsvc.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import refinitiv.scdadlsvc.dao.entity.enums.Format;
import refinitiv.scdadlsvc.dao.entity.enums.QualityGrade;
import refinitiv.scdadlsvc.dao.entity.enums.VersionAvoid;
import refinitiv.scdadlsvc.dao.entity.enums.VersionValidated;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "component_version")
@Entity
public class ComponentVersionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String version;

    @Column(name = "package_url", nullable = false)
    private String packageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Format format;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_grade", nullable = false)
    private QualityGrade qualityGrade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VersionValidated validated;

    @Enumerated(EnumType.STRING)
    @Column(name = "version_avoid", nullable = false)
    private VersionAvoid versionAvoid;

    @Column(name = "validation_error", nullable = false)
    private String validationError;

    @Embedded
    private Metadata metadata;

    @ManyToOne
    @JoinColumn(name = "component_fk")
    private ComponentEntity component;
}
