package refinitiv.scdadlsvc.rest;

import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentGroupEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.dao.entity.PlatformEntity;
import refinitiv.scdadlsvc.dao.entity.enums.Format;
import refinitiv.scdadlsvc.dao.entity.enums.QualityGrade;
import refinitiv.scdadlsvc.dao.entity.enums.VersionAvoid;
import refinitiv.scdadlsvc.dao.entity.enums.VersionValidated;

import java.util.List;

public final class TD {

    private TD() {

    }

    public static ComponentEntity createComponentEntity() {
        PlatformEntity platformEntity = PlatformEntity.builder()
                .id(Platform.ID)
                .name(TD.Platform.NAME)
                .build();
        ComponentGroupEntity componentGroupEntity = ComponentGroupEntity.builder()
                .id(CompGroup.ID)
                .name(TD.CompGroup.NAME)
                .platform(platformEntity)
                .build();

        return ComponentEntity.builder()
                .id(TD.Component.ID)
                .name(TD.Component.NAME)
                .assetInsightId(TD.Component.ASSET_INSIGHT_ID)
                .componentGroup(componentGroupEntity)
                .componentVersions(List.of())
                .build();
    }

    public static ComponentVersionEntity createComponentVersionEntity() {
        return ComponentVersionEntity.builder()
                .id(TD.CompVer.ID)
                .packageUrl(TD.CompVer.PACKAGE_URL)
                .format(Format.valueOf(TD.CompVer.FORMAT))
                .version(TD.CompVer.VERSION)
                .qualityGrade(QualityGrade.valueOf(TD.CompVer.QUALITY_GRADE))
                .versionValidated(VersionValidated.valueOf(TD.CompVer.VERSION_VALIDATED))
                .versionValidationError(TD.CompVer.VERSION_VALIDATION_ERROR)
                .versionAvoid(VersionAvoid.valueOf(TD.CompVer.VERSION_AVOID))
                .component(createComponentEntity())
                .build();
    }

    public static class Platform {
        public static final Long ID = 111L;
        public static final String NAME = "PlatformName";
    }

    public static class CompGroup {
        public static final Long ID = 222L;
        public static final String NAME = "ComponentGroupName";
    }

    public static class Component {
        public static final Long ID = 333L;
        public static final String NAME = "ComponentName";
        public static final Long ASSET_INSIGHT_ID = 2744L;
    }

    public static class CompVer {
        public static final Long ID = 444L;
        public static final String PACKAGE_URL = "http://package.url";
        public static final String FORMAT = "R10K";
        public static final String VERSION = "<major>.<minor>.<patch>-<build>-<label>";
        public static final String QUALITY_GRADE = "DEVELOPMENT";
        public static final String VERSION_VALIDATED = "NEW";
        public static final String VERSION_VALIDATION_ERROR = "some string";
        public static final String VERSION_AVOID = "BAD_RELEASE";
    }
}
