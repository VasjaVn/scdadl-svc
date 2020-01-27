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
import refinitiv.scdadlsvc.rest.dto.ComponentStackDto;
import refinitiv.scdadlsvc.service.ComponentStackService;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
public class ComponentStackController {
    private ComponentStackService componentStackService;

    @Autowired
    public void setComponentStackService(ComponentStackService componentStackService) {
        this.componentStackService = componentStackService;
    }

    @PostMapping(value = "/component-stack", consumes = "application/json")
    @ResponseStatus(CREATED)
    public void createComponentStack(@Valid @RequestBody ComponentStackDto dto) {
        componentStackService.createComponentStack(dto);
    }

    @GetMapping(value = "/component-stack/{id}", produces = "application/json")
    public ComponentStackDto getComponentStack(@PathVariable Long id) {
        return ComponentStackDto.fromEntity(componentStackService.getComponentStackById(id));
    }

    @GetMapping(value = "/component-stack", produces = "application/json")
    public List<ComponentStackDto> getSearchComponentStacks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                                       @RequestParam(value = "search") String search) {
        return componentStackService.searchComponentStacks(page, limit, search)
                .stream()
                .map(ComponentStackDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/component-stack/{id}", consumes = "application/json")
    public void updateComponentStack(@PathVariable Long id, @Valid @RequestBody ComponentStackDto dto) {
        componentStackService.updateComponentStack(id, dto);
    }
}
