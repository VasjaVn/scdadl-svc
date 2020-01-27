package refinitiv.scdadlsvc.service;

import refinitiv.scdadlsvc.dao.entity.ComponentStackEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentStackDto;

import java.util.List;

public interface ComponentStackService {
    void createComponentStack(ComponentStackDto dto);

    ComponentStackEntity getComponentStackById(Long id);

    List<ComponentStackEntity> searchComponentStacks(Integer page, Integer limit, String search);

    void updateComponentStack(Long id, ComponentStackDto dto);
}
