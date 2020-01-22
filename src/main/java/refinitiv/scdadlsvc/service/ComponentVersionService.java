package refinitiv.scdadlsvc.service;

import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentVersionDto;

import java.util.List;

public interface ComponentVersionService {
    void createComponentVersion(Long componentId, ComponentVersionDto dto);

    ComponentVersionEntity getComponentVersionById(Long id);

    List<ComponentVersionEntity> getComponentVersionsByComponentId(Long componentId);

    List<ComponentVersionEntity> searchComponentVersions(Integer page, Integer limit, String patternComponentName);

    void updateComponentVersion(Long id, ComponentVersionDto dto);
}
