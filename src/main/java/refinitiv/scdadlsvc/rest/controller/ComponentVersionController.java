package refinitiv.scdadlsvc.rest.controller;

import refinitiv.scdadlsvc.rest.dto.ComponentVersionDto;
import refinitiv.scdadlsvc.service.ComponentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class ComponentVersionController {
    private ComponentVersionService componentVersionService;

    @Autowired
    public void setComponentVersionService(ComponentVersionService componentVersionService) {
        this.componentVersionService = componentVersionService;
    }

    @PostMapping(value = "/component/{id}/versions", consumes = "application/json")
    @ResponseStatus(CREATED)
    public void createComponentVersion(@PathVariable("id") Long componentId,
                                       @Valid @RequestBody ComponentVersionDto componentVersionDto) {
        componentVersionService.createComponentVersion(componentId, componentVersionDto);
    }

    @GetMapping(value = "/component-versions/{id}", produces = "application/json")
    public ComponentVersionDto getComponentVersionById(@PathVariable Long id) {
        return ComponentVersionDto.fromEntity(componentVersionService.getComponentVersionById(id));
    }

    @GetMapping(value = "/component/{id}/versions", produces = "application/json")
    public List<ComponentVersionDto> getComponentVersionsByComponentId(@PathVariable("id") Long componentId) {
        return componentVersionService.getComponentVersionsByComponentId(componentId)
                .stream()
                .map(ComponentVersionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/component-versions", produces = "application/json")
    public List<ComponentVersionDto> searchComponentVersions(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                                             @RequestParam(value = "search") String patternComponentName) {
            return componentVersionService.searchComponentVersions(page, limit, patternComponentName)
                    .stream()
                    .map(ComponentVersionDto::fromEntity)
                    .collect(Collectors.toList());
    }

    @PutMapping(value = "/component-versions/{id}", consumes = "application/json")
    public void updateComponentVersionById(@PathVariable Long id,
                                           @Valid @RequestBody ComponentVersionDto componentVersionDto) {
        componentVersionService.updateComponentVersion(id, componentVersionDto);
    }
}
