package refinitiv.scdadlsvc.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentGroupEntity;
import refinitiv.scdadlsvc.dao.entity.PlatformEntity;
import refinitiv.scdadlsvc.rest.controller.ComponentController;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ComponentAlreadyExistException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.service.ComponentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComponentController.class)
public class ComponentControllerTest {
    private static final String PLATFORM_NAME = "PlatformNameTest";
    private static final String COMPONENT_GROUP_NAME = "ComponentGroupNameTest";
    private static final String COMPONENT_NAME = "ComponentNameTest";
    private static final Long COMPONENT_ID = 101L;
    private static final Long COMPONENT_ASSET_INSIGHT_ID = 2744L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComponentService componentServiceMock;

    @Test
    public void createComponentReturn201() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"eikon\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void createComponentReturn400() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createComponentReturn400WhenPlatformNameIsWrong() throws Exception {
        // given
        doThrow(new CreateComponentWithWrongPlatformNameException("")).when(componentServiceMock).createComponent(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"WrongPlatformName\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createComponentReturn400WhenComponentGroupNameIsWrong() throws Exception {
        // given
        doThrow(new CreateComponentWithWrongGroupNameException("")).when(componentServiceMock).createComponent(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"WrongComponentGroupName\",\n" +
                        "  \"platform\": \"eikon\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createComponentReturn409() throws Exception {
        // given
        doThrow(new ComponentAlreadyExistException("")).when(componentServiceMock).createComponent(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"ComponentNameIsAlreadyExist\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"eikon\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isConflict());
    }

    @Test
    public void getComponentByIdReturn200() throws Exception {
        // given
        when(componentServiceMock.getComponentById(anyLong())).thenReturn(createComponentEntity());

        // when
        ResultActions result = mockMvc.perform(get("/components/1").accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(COMPONENT_ID))
                .andExpect(jsonPath("$.name").value(COMPONENT_NAME))
                .andExpect(jsonPath("$.componentGroup").value(COMPONENT_GROUP_NAME))
                .andExpect(jsonPath("$.platform").value(PLATFORM_NAME))
                .andExpect(jsonPath("$.assetInsightId").value(COMPONENT_ASSET_INSIGHT_ID));
    }

    @Test
    public void getComponentByIdReturn404() throws Exception {
        // given
        when(componentServiceMock.getComponentById(anyLong())).thenThrow(new ComponentNotFoundException("Component not found"));

        // when
        ResultActions result = mockMvc.perform(get("/components/1"));

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void updateComponentReturn201() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"eikon\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void updateComponentReturn400() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateComponentReturn400WhenPlatformNameIsWrong() throws Exception {
        // given
        doThrow(new UpdateComponentWithWrongPlatformNameException("")).when(componentServiceMock).updateComponent(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"WrongPlatformName\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateComponentReturn400WhenComponentGroupNameIsWrong() throws Exception {
        // given
        doThrow(new UpdateComponentWithWrongGroupNameException("")).when(componentServiceMock).updateComponent(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"WrongComponentGroupName\",\n" +
                        "  \"platform\": \"eikon\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateComponentReturn404() throws Exception {
        // given
        doThrow(new ComponentNotFoundException("")).when(componentServiceMock).updateComponent(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"eikon\",\n" +
                        "  \"assetInsightId\": 2744\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }

    private ComponentEntity createComponentEntity() {
        PlatformEntity platformEntity = PlatformEntity.builder()
                .name(PLATFORM_NAME)
                .build();
        ComponentGroupEntity componentGroupEntity = ComponentGroupEntity.builder()
                .name(COMPONENT_GROUP_NAME)
                .platform(platformEntity)
                .build();

        return ComponentEntity.builder()
                .id(COMPONENT_ID)
                .assetInsightId(COMPONENT_ASSET_INSIGHT_ID)
                .name(COMPONENT_NAME)
                .componentGroup(componentGroupEntity)
                .componentVersions(List.of())
                .build();
    }
}
