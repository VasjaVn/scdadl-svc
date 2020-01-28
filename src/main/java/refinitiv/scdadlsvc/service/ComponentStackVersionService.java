package refinitiv.scdadlsvc.service;

import refinitiv.scdadlsvc.dao.entity.ComponentStackVersionEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentStackVersionDto;

import java.util.List;

public interface ComponentStackVersionService {
    void createComponentStackVersion(Long componentStackId, ComponentStackVersionDto dto);

    ComponentStackVersionEntity getComponentStackVersionById(Long id);

    List<ComponentStackVersionEntity> getComponentStackVersionsByComponentStackId(Long componentStackId);

    List<ComponentStackVersionEntity> searchComponentStackVersions(Integer page, Integer limit, String search);
}
