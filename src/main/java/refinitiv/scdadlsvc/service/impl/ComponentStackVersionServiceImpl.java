package refinitiv.scdadlsvc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refinitiv.scdadlsvc.dao.entity.ComponentStackEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentStackVersionEntity;
import refinitiv.scdadlsvc.dao.repository.ComponentStackRepository;
import refinitiv.scdadlsvc.dao.repository.ComponentStackVersionRepository;
import refinitiv.scdadlsvc.dao.repository.ComponentVersionRepository;
import refinitiv.scdadlsvc.rest.dto.ComponentStackVersionDto;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentStackVersionNotFoundException;
import refinitiv.scdadlsvc.service.ComponentStackVersionService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class ComponentStackVersionServiceImpl implements ComponentStackVersionService {
    private ComponentStackRepository componentStackRepository;
    private ComponentStackVersionRepository componentStackVersionRepository;

    private ComponentVersionRepository componentVersionRepository;

    @Autowired
    public void setComponentStackRepository(ComponentStackRepository componentStackRepository) {
        this.componentStackRepository = componentStackRepository;
    }

    @Autowired
    public void setComponentStackVersionRepository(ComponentStackVersionRepository componentStackVersionRepository) {
        this.componentStackVersionRepository = componentStackVersionRepository;
    }

    @Autowired
    public void setComponentVersionRepository(ComponentVersionRepository componentVersionRepository) {
        this.componentVersionRepository = componentVersionRepository;
    }

    @Override
    public void createComponentStackVersion(Long componentStackId, ComponentStackVersionDto dto) {
        Optional<ComponentStackEntity> componentStackEntityOptional = componentStackRepository.findById(componentStackId);

        if (!componentStackEntityOptional.isPresent()) {
            // throw
        }
        //ffff

/*

        ComponentStackEntity componentStackEntity = componentStackEntityOptional.get();
        List<ComponentStackVersionEntity> componentStackVersions = componentStackEntity.getComponentStackVersions();

        if (componentStackVersions == null || componentStackVersions.isEmpty()) {
            componentStackVersions = new ArrayList<>();
        }

        Optional<ComponentVersionEntity> componentVersionEntityOptional =
                componentVersionRepository.findById(dto.getComponentConfiguration().get(0).getComponentVersion().getId());

        if (!componentStackEntityOptional.isPresent()) {
            // throw
        }

        ComponentVersionEntity componentVersionEntity = componentVersionEntityOptional.get();

//        ComponentVersionEntity componentVersionEntity = ComponentVersionEntity.builder()
//                .version(dto.getComponentConfiguration().get(0).getComponentVersion().getVersion())
//                .packageUrl(dto.getComponentConfiguration().get(0).getComponentVersion().getPackageUrl())
//                .format(dto.getComponentConfiguration().get(0).getComponentVersion().getFormat())
//                .qualityGrade(dto.getComponentConfiguration().get(0).getComponentVersion().getQualityGrade())
//                .versionValidated(dto.getComponentConfiguration().get(0).getComponentVersion().getVersionValidated())
//                .versionAvoid(dto.getComponentConfiguration().get(0).getComponentVersion().getVersionAvoid())
//                .versionValidationError(dto.getComponentConfiguration().get(0).getComponentVersion().getVersionValidationError())
//                .metadata(MetadataUtility.withOnlyCreatedData("createdBy"))
//                .build();

        ComponentStackVersionEntity componentStackVersionEntity = ComponentStackVersionEntity.builder()
                .version(dto.getVersion())
                .validated(dto.getValidated())
                .componentStack(componentStackEntity)
                .build();

        ComponentConfigurationEntity componentConfigurationEntity = ComponentConfigurationEntity.builder()
                .triggerNewStackVer(dto.getComponentConfiguration().get(0).getTriggerNewStack())
                .minGrade(dto.getComponentConfiguration().get(0).getMinQualityGrade())
                .componentStackVersion(componentStackVersionEntity)
                .componentVersion(componentVersionEntity)
                .build();

        componentStackVersionEntity.setComponentStack(componentStackEntity);
        componentStackVersionEntity.setComponentConfiguration();

        componentStackVersions.add(componentStackVersionEntity);
        //componentStackEntity.

*/
    }

    @Override
    public ComponentStackVersionEntity getComponentStackVersionById(Long id) {
        log.info("getComponentStackVersionById: - [id={}]", id);
        Optional<ComponentStackVersionEntity> componentVersionEntityOptional = componentStackVersionRepository.findById(id);
        if (componentVersionEntityOptional.isPresent()) {
            ComponentStackVersionEntity componentStackVersionEntity = componentVersionEntityOptional.get();
            log.info("getComponentStackVersionById: component stack version is founded - [componentStackVersionEntity={}]", componentStackVersionEntity);
            return componentStackVersionEntity;
        } else {
            log.info("getComponentStackVersionById: component stack version is not founded - [componentStackVersionId={}]", id);
            throw new ComponentStackVersionNotFoundException(String.format("Component stack version is not founded: [componentStackVersionId=%s]", id));
        }
    }

    @Override
    public List<ComponentStackVersionEntity> getComponentStackVersionsByComponentStackId(Long componentStackId) {
        return null;
    }

    @Override
    public List<ComponentStackVersionEntity> searchComponentStackVersions(Integer page, Integer limit, String search) {
        return null;
    }
}
