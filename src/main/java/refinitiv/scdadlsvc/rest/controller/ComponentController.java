package refinitiv.scdadlsvc.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import refinitiv.scdadlsvc.rest.dto.ComponentDto;
import refinitiv.scdadlsvc.service.ComponentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
public class ComponentController {
    private ComponentService componentService;

    @Autowired
    public void setComponentService(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping(value = "/components", consumes = "application/json")
    @ResponseStatus(CREATED)
    public void createComponent(@Valid @RequestBody ComponentDto dto) {
        componentService.createComponent(dto);
    }

    @GetMapping(value = "/components/{id}", produces = "application/json")
    public ComponentDto getComponent(@PathVariable Long id) {
        return ComponentDto.fromEntity(componentService.getComponentById(id));
    }

    @GetMapping(value = "/components", produces = "application/json")
    public List<ComponentDto> searchComponents(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                               @RequestParam(value = "search") String patternComponentName) {
        return componentService.searchComponents(page, limit, patternComponentName)
                .stream()
                .map(ComponentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/components/{id}", consumes = "application/json")
    @ResponseStatus(CREATED)
    public void updateComponent(@PathVariable Long id, @Valid @RequestBody ComponentDto dto) {
        componentService.updateComponent(id, dto);
    }
}
