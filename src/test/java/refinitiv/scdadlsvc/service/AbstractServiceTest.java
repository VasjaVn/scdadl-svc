package refinitiv.scdadlsvc.service;

import org.mockito.Mock;
import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.dao.entity.enums.Format;
import refinitiv.scdadlsvc.dao.entity.enums.QualityGrade;
import refinitiv.scdadlsvc.dao.entity.enums.VersionAvoid;
import refinitiv.scdadlsvc.dao.entity.enums.VersionValidated;
import refinitiv.scdadlsvc.dao.repository.ComponentGroupRepository;
import refinitiv.scdadlsvc.dao.repository.ComponentRepository;
import refinitiv.scdadlsvc.dao.repository.ComponentVersionRepository;
import refinitiv.scdadlsvc.dao.repository.PlatformRepository;
import refinitiv.scdadlsvc.rest.dto.ComponentVersionDto;

import java.util.Date;

public abstract class AbstractServiceTest {
    protected static final Long COMPONENT_ID = 111111L;
    protected static final String COMPONENT_NAME = "component_name";
    protected static final Long COMPONENT_ASSET_INSIGHT_ID = 5858L;

    protected static final Long COMPONENT_GROUP_ID = 2222222L;
    protected static final String COMPONENT_GROUP_NAME = "component_group_name";

    protected static final Long COMPONENT_PLATFORM_ID = 3333333L;
    protected static final String COMPONENT_PLATFORM_NAME = "platform_name";

    protected static final Date CREATED_DATE = new Date();
    protected static final String CREATED_BY = "created_by_name";
    protected static final Date UPDATED_DATE = new Date();
    protected static final String UPDATED_BY = "updated_by_name";

    protected static final Long COMPONENT_VERSION_ID = 444444L;
    protected static final String COMPONENT_VERSION = "111.222.333-444-555-666";
    protected static final String COMPONENT_VERSION_PACKAGE_URL = "http://url.package.component";
    protected static final Format COMPONENT_VERSION_FORMAT = Format.R10K;
    protected static final QualityGrade COMPONENT_VERSION_QUALITY_GRADE = QualityGrade.PRODUCTION;
    protected static final VersionValidated COMPONENT_VERSION_VALIDATED = VersionValidated.NEW;
    protected static final VersionAvoid COMPONENT_VERSION_AVOID = VersionAvoid.BAD_RELEASE;
    protected static final String COMPONENT_VERSION_VALIDATION_ERRORS = "component validation errors";

    protected static final Integer PAGE = 0;
    protected static final Integer LIMIT = 1;
    protected static final String SEARCH_PATTERN_COMPONENT_NAME = "search_pattern_component_name";

    @Mock
    protected PlatformRepository platformRepositoryMock;
    @Mock
    protected ComponentGroupRepository componentGroupRepositoryMock;
    @Mock
    protected ComponentRepository componentRepositoryMock;
    @Mock
    protected ComponentVersionRepository componentVersionRepositoryMock;

    protected ComponentVersionEntity toComponentVersionEntityFromDto(ComponentVersionDto componentVersionDto) {
        return ComponentVersionEntity.builder()
                .id(componentVersionDto.getId())
                .version(componentVersionDto.getVersion())
                .packageUrl(componentVersionDto.getPackageUrl())
                .format(componentVersionDto.getFormat())
                .qualityGrade(componentVersionDto.getQualityGrade())
                .validated(componentVersionDto.getVersionValidated())
                .versionAvoid(componentVersionDto.getVersionAvoid())
                .validationError(componentVersionDto.getVersionValidationError())
                .build();
    }
}
