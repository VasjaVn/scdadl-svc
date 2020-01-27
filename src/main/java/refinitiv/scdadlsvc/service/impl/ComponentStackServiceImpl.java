package refinitiv.scdadlsvc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refinitiv.scdadlsvc.dao.entity.ComponentStackEntity;
import refinitiv.scdadlsvc.dao.repository.ComponentStackRepository;
import refinitiv.scdadlsvc.rest.dto.ComponentStackDto;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ComponentAlreadyExistException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.searchobject.SearchComponentStacksEmptyListException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.componentstack.UpdateComponentStackWithWrongIdException;
import refinitiv.scdadlsvc.service.ComponentStackService;
import refinitiv.scdadlsvc.utility.MetadataUtility;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class ComponentStackServiceImpl implements ComponentStackService {
    private ComponentStackRepository componentStackRepository;

    @Autowired
    public void setComponentStackRepository(ComponentStackRepository componentStackRepository) {
        this.componentStackRepository = componentStackRepository;
    }

    @Override
    public void createComponentStack(ComponentStackDto dto) {
        log.info("createComponentStack: - [componentStackDto={}]", dto);

        ComponentStackEntity componentStackEntity = componentStackRepository.findByName(dto.getName());
        if (componentStackEntity != null) {
            log.warn("createComponentStack: component stack name is already existed - [componentStackName=\"{}\"]", dto.getName());
            throw new ComponentAlreadyExistException(String.format("Component stack name is already existed - [componentStackName=\"%s\"]", dto.getName()));
        }

        componentStackEntity = ComponentStackEntity.builder()
                .name(dto.getName())
                .nextAutoVerStartAt(dto.getNextAutoVerStartAt())
                .metadata(MetadataUtility.withOnlyCreatedData("createdBy"))
                .build();

        componentStackEntity = componentStackRepository.save(componentStackEntity);
        log.info("createComponentStack: component stack is created - [componentStackEntity={}]", componentStackEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ComponentStackEntity getComponentStackById(Long id) {
        log.info("getComponentStackById: - [id={}]", id);
        Optional<ComponentStackEntity> componentStackEntityOptional = componentStackRepository.findById(id);
        if (componentStackEntityOptional.isPresent()) {
            ComponentStackEntity componentStackEntity = componentStackEntityOptional.get();
            log.info("getComponentStackById: - [componentStackEntity={}]", componentStackEntity);
            return componentStackEntity;
        } else {
            log.info("getComponentStackById: component stack is not founded - [id={}]", id);
            throw new ComponentNotFoundException(String.format("Component stack is not founded: [id = %s]", id));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComponentStackEntity> searchComponentStacks(Integer page, Integer limit, String search) {
        log.info("searchComponentStacks: query parameters - [page={}, limit={}, search=\"{}\"]", page, limit, search);
        List<ComponentStackEntity> componentStackEntities = componentStackRepository.searchByComponentStackName(search, PageRequest.of(page, limit)).getContent();
        if (componentStackEntities.isEmpty()) {
            log.info("searchComponentStacks: list of component stacks is empty for query parameters - [page={}, limit={}, search=\"{}\"]", page, limit, search);
            throw new SearchComponentStacksEmptyListException(String.format("List of component stacks is empty for query parameters: [page=%s, limit=%s, search=\"%s\"]", page, limit, search));
        }
        log.info("searchComponentStacks: count of component stacks - [count={}]", componentStackEntities.size());
        return componentStackEntities;
    }

    @Override
    public void updateComponentStack(Long id, ComponentStackDto dto) {
        log.info("updateComponentStack: - [id={}, componentStackDto={}]", id, dto);
        Optional<ComponentStackEntity> componentStackEntityOptional = componentStackRepository.findById(id);
        if (componentStackEntityOptional.isPresent()) {
            ComponentStackEntity componentStackEntity = componentStackEntityOptional.get();
            log.info("updateComponentStack: BEFORE - [componentStackEntity={}]", componentStackEntity);
            componentStackEntity.setName(dto.getName());
            componentStackEntity.setNextAutoVerStartAt(dto.getNextAutoVerStartAt());
            componentStackEntity.getMetadata().setUpdatedDate(new Date());
            componentStackEntity.getMetadata().setUpdatedBy("updatedBy");
            componentStackEntity = componentStackRepository.save(componentStackEntity);
            log.info("updateComponentStack: AFTER - [componentStackEntity={}]", componentStackEntity);
        } else {
            log.warn("updateComponentStack: update component stack is not founded - [id={}]", id);
            throw new UpdateComponentStackWithWrongIdException(String.format("Update component stack is not founded: [id=%s]", id));
        }
    }
}
