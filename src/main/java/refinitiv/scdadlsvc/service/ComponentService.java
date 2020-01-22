package refinitiv.scdadlsvc.service;

import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentDto;

import java.util.List;

public interface ComponentService {
    void createComponent(ComponentDto dto);

    ComponentEntity getComponentById(Long id);

    List<ComponentEntity> searchComponents(Integer page, Integer limit, String patternComponentName);

    void updateComponent(Long id, ComponentDto dto);
}
