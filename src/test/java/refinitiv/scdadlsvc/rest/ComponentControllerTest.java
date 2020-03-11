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
import refinitiv.scdadlsvc.rest.controller.ComponentController;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ComponentAlreadyExistException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ReqParamIdAndDtoIdNotEqualsException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentsNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.service.ComponentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComponentController.class)
public class ComponentControllerTest {

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
        when(componentServiceMock.getComponentById(anyLong())).thenReturn(TD.createComponentEntity());

        // when
        ResultActions result = mockMvc.perform(get("/components/1").accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TD.Component.ID))
                .andExpect(jsonPath("$.name").value(TD.Component.NAME))
                .andExpect(jsonPath("$.componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$.platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$.assetInsightId").value(TD.Component.ASSET_INSIGHT_ID));
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
    public void searchComponentsReturn200() throws Exception {
        // given
        when(componentServiceMock.searchComponents(anyInt(), anyInt(), any())).thenReturn(List.of(TD.createComponentEntity()));

        // when
        ResultActions result = mockMvc.perform(get("/components").param("search", "testName"));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TD.Component.ID))
                .andExpect(jsonPath("$[0].name").value(TD.Component.NAME))
                .andExpect(jsonPath("$[0].componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$[0].platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$[0].assetInsightId").value(TD.Component.ASSET_INSIGHT_ID));
    }

    @Test
    public void searchComponentsReturn400WhenQueryParamSearchIsMissed() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/components");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void searchComponentsReturn400WhenQueryParamPageHasText() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/components")
                .param("page", "text")
                .param("search", "test");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void searchComponentsReturn400WhenQueryParamLimitHasText() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/components")
                .param("limit", "text")
                .param("search", "test");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void searchComponentsReturn404() throws Exception {
        // given
        when(componentServiceMock.searchComponents(anyInt(), anyInt(), any())).thenThrow(new ComponentsNotFoundException(""));

        // when
        ResultActions result = mockMvc.perform(get("/components").param("search", "testName"));

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
    public void updateComponentReturn400WhenReqParamIdAndDtoIdNotEquals() throws Exception {
        // given
        final Long reqParId = 202L;
        final Long dtoId = 101L;
        doThrow(new ReqParamIdAndDtoIdNotEqualsException("")).when(componentServiceMock).updateComponent(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/{reqParId}", reqParId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": " + dtoId + ",\n" +
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
}
