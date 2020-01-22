package refinitiv.scdadlsvc.service.impl;

import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentGroupEntity;
import refinitiv.scdadlsvc.dao.entity.PlatformEntity;
import refinitiv.scdadlsvc.dao.repository.ComponentGroupRepository;
import refinitiv.scdadlsvc.dao.repository.ComponentRepository;
import refinitiv.scdadlsvc.dao.repository.PlatformRepository;
import refinitiv.scdadlsvc.rest.dto.ComponentDto;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ComponentAlreadyExistException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.searchobject.SearchComponentsEmptyListException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongIdException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.service.ComponentService;
import refinitiv.scdadlsvc.utility.MetadataUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class ComponentServiceImpl implements ComponentService {
    private PlatformRepository platformRepository;
    private ComponentRepository componentRepository;
    private ComponentGroupRepository componentGroupRepository;

    @Autowired
    public void setPlatformRepository(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Autowired
    public void setComponentRepository(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Autowired
    public void setComponentGroupRepository(ComponentGroupRepository componentGroupRepository) {
        this.componentGroupRepository = componentGroupRepository;
    }

    @Override
    public void createComponent(ComponentDto dto) {
        log.info("createComponent: - [componentDto={}]", dto);
        if (componentRepository.findByName(dto.getComponentName()).isPresent()) {
            log.warn("createComponent: component name is already existed - [componentName=\"{}\"]", dto.getComponentName());
            throw new ComponentAlreadyExistException(String.format("Component name is already existed: [componentName=\"%s\"]", dto.getComponentName()));
        }

        PlatformEntity platformEntity = platformRepository.findByName(dto.getPlatformName());
        if (Objects.isNull(platformEntity)) {
            log.warn("createComponent: platform name is not existed - [platformName=\"{}\"]", dto.getPlatformName());
            throw new CreateComponentWithWrongPlatformNameException(String.format("Platform name is not existed: [platformName=\"%s\"]", dto.getPlatformName()));
        }

        Optional<ComponentGroupEntity> componentGroupEntityOptional = platformEntity.getComponentGroups().stream()
                .filter(ge -> Objects.equals(ge.getName(), dto.getComponentGroupName()))
                .findFirst();

        if (componentGroupEntityOptional.isEmpty()) {
            log.warn("createComponent: component group name is not existed - [componentGroupName=\"{}\"]", dto.getComponentGroupName());
            throw new CreateComponentWithWrongGroupNameException(String.format("Component group name is not existed: [componentGroupName=\"%s\"]", dto.getComponentGroupName()));
        }

        ComponentGroupEntity componentGroupEntity = componentGroupEntityOptional.get();
        ComponentEntity componentEntity = ComponentEntity.builder()
                .name(dto.getComponentName())
                .assetInsightId(dto.getAssetInsightId())
                .componentGroup(componentGroupEntity)
                .metadata(MetadataUtility.withOnlyCreatedData("createdBy"))
                .build();

        componentEntity = componentRepository.save(componentEntity);
        componentGroupEntity.getComponents().add(componentEntity);
        log.info("createComponent: component is created - [componentEntity={}]", componentEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ComponentEntity getComponentById(Long id) {
        log.info("getComponentById: - [id={}]", id);
        Optional<ComponentEntity> componentEntityOptional = componentRepository.findById(id);
        if (componentEntityOptional.isPresent()) {
            ComponentEntity componentEntity = componentEntityOptional.get();
            log.info("getComponentById: - [componentEntity={}]", componentEntity);
            return componentEntity;
        } else {
            log.info("getComponentById: component is not founded - [id={}]", id);
            throw new ComponentNotFoundException(String.format("Component is not founded: [id = %s]", id));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComponentEntity> searchComponents(Integer page, Integer limit, String patternComponentName) {
        List<ComponentEntity> componentEntities = componentRepository.searchByComponentName(patternComponentName, PageRequest.of(page, limit)).getContent();
        if (componentEntities.isEmpty()) {
            log.info("searchComponents: list of components is empty for query parameters - [page={}, limit={}, search=\"{}\"]", page, limit, patternComponentName);
            throw new SearchComponentsEmptyListException(String.format("List of components is empty for query parameters: [page=%s, limit=%s, search=\"%s\"]", page, limit, patternComponentName));
        }
        log.info("searchComponents: count of components - [count={}]", componentEntities.size());
        return componentEntities;
    }

    @Override
    public void updateComponent(Long id, ComponentDto dto) {
        log.info("updateComponent: - [id={}, componentDto={}]", id, dto);

        PlatformEntity platformEntity = platformRepository.findByName(dto.getPlatformName());
        if (Objects.isNull(platformEntity)) {
            log.warn("updateComponent: platform name is not existed - [platformName=\"{}\"]", dto.getPlatformName());
            throw new UpdateComponentWithWrongPlatformNameException(String.format("Platform name is not existed: [platformName=\"%s\"]", dto.getPlatformName()));
        }
        log.info("updateComponent: - [platformEntity={}]", platformEntity);

        ComponentGroupEntity componentGroupEntity = componentGroupRepository.findByName(dto.getComponentGroupName());
        if (Objects.isNull(componentGroupEntity)) {
            log.warn("updateComponent: component group name is not existed - [componentGroupName=\"{}\"]", dto.getComponentGroupName());
            throw new UpdateComponentWithWrongGroupNameException(String.format("Component group name is not existed: [componentGroupName=\"%s\"]", dto.getComponentGroupName()));
        }
        log.info("updateComponent: - [componentGroupEntity={}]", componentGroupEntity);

        componentGroupEntity.setPlatform(platformEntity);

        Optional<ComponentEntity> componentEntityOptional = componentRepository.findById(id);
        if (componentEntityOptional.isPresent()) {
            ComponentEntity componentEntity = componentEntityOptional.get();
            log.info("updateComponent: BEFORE - [componentEntity={}]", componentEntity);
            componentEntity.setName(dto.getComponentName());
            componentEntity.getComponentGroup().setName(dto.getComponentGroupName());
            componentEntity.getComponentGroup().getPlatform().setName(dto.getPlatformName());
            componentEntity.setAssetInsightId(dto.getAssetInsightId());
            componentEntity.getMetadata().setUpdatedDate(new Date());
            componentEntity.getMetadata().setUpdatedBy("updatedBy");
            componentEntity.setComponentGroup(componentGroupEntity);

            componentEntity = componentRepository.save(componentEntity);
            log.info("updateComponent: AFTER - [componentEntity={}]", componentEntity);
        } else {
            log.warn("updateComponent: update component is not founded - [id={}]", id);
            throw new UpdateComponentWithWrongIdException(String.format("Update component is not founded: [id=%s]", id));
        }
    }
}
