package refinitiv.scdadlsvc.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import refinitiv.scdadlsvc.rest.dto.ComponentStackVersionDto;
import refinitiv.scdadlsvc.service.ComponentStackVersionService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class ComponentStackVersionController {
    private ComponentStackVersionService componentStackVersionService;

    @Autowired
    public void setComponentStackVersionService(ComponentStackVersionService componentStackVersionService) {
        this.componentStackVersionService = componentStackVersionService;
    }

    @PostMapping(value = "/component-stack/{id}/versions", consumes = "application/json")
    @ResponseStatus(CREATED)
    public void createComponentStackVersion(@PathVariable("id") Long componentStackId,
                                       @Valid @RequestBody ComponentStackVersionDto componentStackVersionDto) {
        componentStackVersionService.createComponentStackVersion(componentStackId, componentStackVersionDto);
    }

    @GetMapping(value = "/component-stack-versions/{id}", produces = "application/json")
    public ComponentStackVersionDto getComponentStackVersionById(@PathVariable Long id) {
        return ComponentStackVersionDto.fromEntity(componentStackVersionService.getComponentStackVersionById(id));
    }

    @GetMapping(value = "/component-stack/{id}/versions", produces = "application/json")
    public List<ComponentStackVersionDto> getComponentStackVersionsByComponentStackId(@PathVariable("id") Long componentStackId) {
        return componentStackVersionService.getComponentStackVersionsByComponentStackId(componentStackId)
                .stream()
                .map(ComponentStackVersionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/component-stack-versions", produces = "application/json")
    public List<ComponentStackVersionDto> searchComponentStackVersions(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                                             @RequestParam(value = "search") String search) {
            return componentStackVersionService.searchComponentStackVersions(page, limit, search)
                    .stream()
                    .map(ComponentStackVersionDto::fromEntity)
                    .collect(Collectors.toList());
    }
}
