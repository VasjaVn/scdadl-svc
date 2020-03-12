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
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.CreateScdadlObjectException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ListScdadlObjectsEmptyException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ReqParamIdAndDtoIdNotEqualsException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.UpdateScdadlObjectException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ScdadlObjectNotFoundException;
import refinitiv.scdadlsvc.service.ComponentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

    /*****************************************************************
     * TESTS FOR: - POST /components
     ******************************************************************/
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
        final String wrongPlatformName = "wrongPlatformName";
        doThrow(new CreateScdadlObjectException("Create \"Component\": platform name is not existed: [platformName=\"" + wrongPlatformName + "\"]"))
                .when(componentServiceMock).createComponent(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"Eikon\",\n" +
                        "  \"platform\": \"" + wrongPlatformName + "\",\n" +
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
        final String wrongGroupName = "WrongComponentGroupName";
        doThrow(new CreateScdadlObjectException("Create \"Component\": component group name is not existed [componentGroupName=\"" + wrongGroupName + "\"]"))
                .when(componentServiceMock).createComponent(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/components")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"" + wrongGroupName + "\",\n" +
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


    /*****************************************************************
     * TESTS FOR: - GET /components/{id}
     ******************************************************************/
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
        when(componentServiceMock.getComponentById(anyLong()))
                .thenThrow(new ScdadlObjectNotFoundException("Component is not founded: [id = 1]"));

        // when
        ResultActions result = mockMvc.perform(get("/components/1"));

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }


    /*****************************************************************
     * TESTS FOR: - GET /components?page=..&limit=..&search=..
     ******************************************************************/
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
        when(componentServiceMock.searchComponents(anyInt(), anyInt(), anyString()))
                .thenThrow(new ListScdadlObjectsEmptyException("List of components is empty for query parameters: [page=0, limit=20, search=\"testName\"]"));

        // when
        ResultActions result = mockMvc.perform(get("/components").param("search", "testName"));

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }


    /*****************************************************************
     * TESTS FOR: - PUT /components/{id}
     ******************************************************************/
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
        doThrow(new UpdateScdadlObjectException("")).when(componentServiceMock).updateComponent(anyLong(), any());
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
        final String wrongComponentGroupName = "wrongComponentGroupName";
        doThrow(new UpdateScdadlObjectException("Update \"Component\": component group name is not existed [componentGroupName=\"" + wrongComponentGroupName + "\"]"))
                .when(componentServiceMock).updateComponent(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"name\": \"Eikon_ABC\",\n" +
                        "  \"componentGroup\": \"" + wrongComponentGroupName + "\",\n" +
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
        doThrow(new ScdadlObjectNotFoundException("Component is not founded: [id = 101]"))
                .when(componentServiceMock).updateComponent(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/components/101")
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
