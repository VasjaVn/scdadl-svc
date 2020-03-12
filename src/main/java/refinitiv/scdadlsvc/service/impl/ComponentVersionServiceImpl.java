package refinitiv.scdadlsvc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.dao.repository.ComponentRepository;
import refinitiv.scdadlsvc.dao.repository.ComponentVersionRepository;
import refinitiv.scdadlsvc.rest.dto.ComponentVersionDto;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ReqParamIdAndDtoIdNotEqualsException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentVersionNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentVersionsNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ScdadlObjectNotFoundException;
import refinitiv.scdadlsvc.service.ComponentVersionService;
import refinitiv.scdadlsvc.utility.MetadataUtility;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class ComponentVersionServiceImpl implements ComponentVersionService {
    private ComponentVersionRepository componentVersionRepository;
    private ComponentRepository componentRepository;

    @Autowired
    public void setComponentVersionRepository(ComponentVersionRepository componentVersionRepository) {
        this.componentVersionRepository = componentVersionRepository;
    }

    @Autowired
    public void setComponentRepository(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public void createComponentVersion(Long componentId, ComponentVersionDto componentVersionDto) {
        log.info("createComponentVersion: - [componentId={}, componentVersionDto={}]", componentId, componentVersionDto);
        Optional<ComponentEntity> componentEntityOptional = componentRepository.findById(componentId);
        if (componentEntityOptional.isEmpty()) {
            log.warn("createComponentVersion: component is not founded - [componentId={}]", componentId);
            throw new ScdadlObjectNotFoundException(String.format("Component is not founded: [componentId=%s]", componentId));
        }

        ComponentEntity componentEntity = componentEntityOptional.get();
        ComponentVersionEntity componentVersionEntity =
                toComponentVersionEntityFromDto(componentVersionDto, componentEntity, "createdBy");
        componentVersionEntity = componentVersionRepository.save(componentVersionEntity);
        componentEntity.getComponentVersions().add(componentVersionEntity);
        log.info("createComponentVersion: new component version is created for component - [componentId={}, componentVersionEntity={}]", componentId, componentVersionEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ComponentVersionEntity getComponentVersionById(Long id) {
        log.info("getComponentVersionById: - [componentVersionId={}]", id);
        Optional<ComponentVersionEntity> componentVersionEntityOptional = componentVersionRepository.findById(id);
        if (componentVersionEntityOptional.isPresent()) {
            ComponentVersionEntity componentVersionEntity = componentVersionEntityOptional.get();
            log.info("getComponentVersionById: component version is founded - [componentVersionEntity={}]", componentVersionEntity);
            return componentVersionEntity;
        } else {
            log.warn("getComponentVersionById: component version is not founded - [componentVersionId={}]", id);
            throw new ComponentVersionNotFoundException(String.format("Component version is not founded: [componentVersionId=%s]", id));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComponentVersionEntity> getComponentVersionsByComponentId(Long componentId) {
        log.info("getComponentVersionsByComponentId: - [componentId={}]", componentId);
        Optional<ComponentEntity> componentEntityOptional = componentRepository.findById(componentId);
        if (componentEntityOptional.isEmpty()) {
            log.warn("getComponentVersionsByComponentId: component is not founded - [componentId={}]", componentId);
            throw new ScdadlObjectNotFoundException(String.format("Component is not founded: [componentId=%s]", componentId));
        }
        List<ComponentVersionEntity> componentVersionEntities = componentEntityOptional.get().getComponentVersions();
        log.info("getComponentVersionsByComponentId: count of component versions - [count={}]", componentVersionEntities.size());
        return componentVersionEntities;
    }

    @Override
    public List<ComponentVersionEntity> searchComponentVersions(Integer page, Integer limit, String patternComponentName) {
        log.info("searchComponentVersions: query parameters - [page={}, limit={}, search=\"{}\"]", page, limit, patternComponentName);
        List<ComponentVersionEntity> componentVersionEntities = componentVersionRepository.searchByComponentName(patternComponentName, PageRequest.of(page, limit)).getContent();
        if (componentVersionEntities.isEmpty()) {
            log.warn("searchComponentVersions: search component versions is empty list for query params - [page={}, limit={}, search=\"{}\"]", page, limit, patternComponentName);
            throw new ComponentVersionsNotFoundException(String.format("Search component versions is empty list for query params: [page=%s, limit=%s, search=\"%s\"]", page, limit, patternComponentName));
        }
        log.info("searchComponentVersions: count of component versions - [count={}]", componentVersionEntities.size());
        return componentVersionEntities;
    }

    @Override
    public void updateComponentVersion(Long id, ComponentVersionDto dto) {
        log.info("updateComponentVersion: - [id={}, componentVersionDto={}]", id, dto);

        if (!Objects.equals(id, dto.getId())) {
            log.warn("updateComponentVersion: request param id and id from dto are not equals - [id={}, dto.id={}]", id, dto.getId());
            throw new ReqParamIdAndDtoIdNotEqualsException(String.format("Request param id and id from dto are not equals for update ComponentVersion: [id=%s, dto.id=%s]", id, dto.getId()));
        }

        Optional<ComponentVersionEntity> componentVersionEntityOptional = componentVersionRepository.findById(id);
        if (componentVersionEntityOptional.isEmpty()) {
            log.warn("updateComponentVersion: component version is not founded for updating - [componentVersionId={}]", id);
            throw new ComponentVersionNotFoundException(String.format("Component version is not found for updating: [componentVersionId=%s]", id));
        }

        ComponentVersionEntity componentVersionEntity = componentVersionEntityOptional.get();
        log.info("updateComponentVersion: BEFORE - [componentVersionEntity={}]", componentVersionEntity);
        componentVersionEntity.setVersion(dto.getVersion());
        componentVersionEntity.setPackageUrl(dto.getPackageUrl());
        componentVersionEntity.setFormat(dto.getFormat());
        componentVersionEntity.setQualityGrade(dto.getQualityGrade());
        componentVersionEntity.setVersionValidated(dto.getVersionValidated());
        componentVersionEntity.setVersionValidationError(dto.getVersionValidationError());
        componentVersionEntity.getMetadata().setUpdatedDate(new Date());
        componentVersionEntity.getMetadata().setUpdatedBy("updatedBy");

        componentVersionEntity = componentVersionRepository.save(componentVersionEntity);
        log.info("updateComponentVersion: AFTER - [updatedComponentVersionEntity={}]", componentVersionEntity);
    }

    private ComponentVersionEntity toComponentVersionEntityFromDto(ComponentVersionDto componentVersionDto,
                                                                   ComponentEntity componentEntity,
                                                                   String createdBy) {
        return ComponentVersionEntity.builder()
                .version(componentVersionDto.getVersion())
                .packageUrl(componentVersionDto.getPackageUrl())
                .format(componentVersionDto.getFormat())
                .qualityGrade(componentVersionDto.getQualityGrade())
                .versionValidated(componentVersionDto.getVersionValidated())
                .versionAvoid(componentVersionDto.getVersionAvoid())
                .versionValidationError(componentVersionDto.getVersionValidationError())
                .metadata(MetadataUtility.withOnlyCreatedData(createdBy))
                .component(componentEntity)
                .build();
    }
}
