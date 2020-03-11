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
import refinitiv.scdadlsvc.rest.controller.ComponentVersionController;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ReqParamIdAndDtoIdNotEqualsException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentVersionNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentVersionsNotFoundException;
import refinitiv.scdadlsvc.service.ComponentVersionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComponentVersionController.class)
public class ComponentVersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComponentVersionService componentVersionServiceMock;

    /*****************************************************************
    * TESTS FOR: - POST /component/{componentId}/versions
    ******************************************************************/
    @Test
    public void createComponentVersionReturn201() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/component/1/versions")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"packageUrl\": \"http://package.url\",\n" +
                        "  \"format\": \"R10K\",\n" +
                        "  \"version\": \"<major>.<minor>.<patch>-<build>-<label>\",\n" +
                        "  \"qualityGrade\": \"DEVELOPMENT\",\n" +
                        "  \"versionValidated\": \"NEW\",\n" +
                        "  \"versionValidationError\": \"some string\",\n" +
                        "  \"versionAvoid\": \"BAD_RELEASE\"\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void createComponentVersionReturn400() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/component/1/versions")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createComponentVersionReturn404() throws Exception {
        // given
        doThrow(new ComponentNotFoundException("")).when(componentVersionServiceMock).createComponentVersion(anyLong(), any());
        final Integer componentIdNotExist = 2;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/component/{componentId}/versions", componentIdNotExist)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"packageUrl\": \"http://package.url\",\n" +
                        "  \"format\": \"R10K\",\n" +
                        "  \"version\": \"<major>.<minor>.<patch>-<build>-<label>\",\n" +
                        "  \"qualityGrade\": \"DEVELOPMENT\",\n" +
                        "  \"versionValidated\": \"NEW\",\n" +
                        "  \"versionValidationError\": \"some string\",\n" +
                        "  \"versionAvoid\": \"BAD_RELEASE\"\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }


    /******************************************************************
     * TESTS FOR: - GET /component/{componentId}/versions
     ******************************************************************/
    @Test
    public void getComponentVersionsByComponentIdReturn200() throws Exception {
        // given
        when(componentVersionServiceMock.getComponentVersionsByComponentId(anyLong()))
                .thenReturn(List.of(TD.createComponentVersionEntity(), TD.createComponentVersionEntity()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component/{componentId}/versions", TD.Component.ID)
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TD.CompVer.ID))
                .andExpect(jsonPath("$[0].version").value(TD.CompVer.VERSION))
                .andExpect(jsonPath("$[0].packageUrl").value(TD.CompVer.PACKAGE_URL))
                .andExpect(jsonPath("$[0].format").value(TD.CompVer.FORMAT))
                .andExpect(jsonPath("$[0].qualityGrade").value(TD.CompVer.QUALITY_GRADE))
                .andExpect(jsonPath("$[0].versionValidated").value(TD.CompVer.VERSION_VALIDATED))
                .andExpect(jsonPath("$[0].versionValidationError").value(TD.CompVer.VERSION_VALIDATION_ERROR))
                .andExpect(jsonPath("$[0].versionAvoid").value(TD.CompVer.VERSION_AVOID))
                .andExpect(jsonPath("$[0].component.id").value(TD.Component.ID))
                .andExpect(jsonPath("$[0].component.name").value(TD.Component.NAME))
                .andExpect(jsonPath("$[0].component.componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$[0].component.platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$[0].component.assetInsightId").value(TD.Component.ASSET_INSIGHT_ID))
                .andExpect(jsonPath("$[1].id").value(TD.CompVer.ID))
                .andExpect(jsonPath("$[1].version").value(TD.CompVer.VERSION))
                .andExpect(jsonPath("$[1].packageUrl").value(TD.CompVer.PACKAGE_URL))
                .andExpect(jsonPath("$[1].format").value(TD.CompVer.FORMAT))
                .andExpect(jsonPath("$[1].qualityGrade").value(TD.CompVer.QUALITY_GRADE))
                .andExpect(jsonPath("$[1].versionValidated").value(TD.CompVer.VERSION_VALIDATED))
                .andExpect(jsonPath("$[1].versionValidationError").value(TD.CompVer.VERSION_VALIDATION_ERROR))
                .andExpect(jsonPath("$[1].versionAvoid").value(TD.CompVer.VERSION_AVOID))
                .andExpect(jsonPath("$[1].component.id").value(TD.Component.ID))
                .andExpect(jsonPath("$[1].component.name").value(TD.Component.NAME))
                .andExpect(jsonPath("$[1].component.componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$[1].component.platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$[1].component.assetInsightId").value(TD.Component.ASSET_INSIGHT_ID));
    }

    @Test
    public void getComponentVersionsByComponentIdReturn400WhenComponentIdIsNotCorrect() throws Exception {
        // given
        final String notCorrectId = "text";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component/{notCorrectId}/versions", notCorrectId)
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getComponentVersionsByComponentIdReturn404WhenComponentNotFound() throws Exception {
        // given
        doThrow(new ComponentNotFoundException("")).when(componentVersionServiceMock).getComponentVersionsByComponentId(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component/555/versions")
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print())
                .andExpect(status().isNotFound());
    }


    /******************************************************************
     * TESTS FOR: - GET /component-versions/{Id}
     ******************************************************************/
    @Test
    public void getComponentVersionByIdReturn200() throws Exception {
        // given
        when(componentVersionServiceMock.getComponentVersionById(anyLong())).thenReturn(TD.createComponentVersionEntity());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component-versions/1")
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TD.CompVer.ID))
                .andExpect(jsonPath("$.version").value(TD.CompVer.VERSION))
                .andExpect(jsonPath("$.packageUrl").value(TD.CompVer.PACKAGE_URL))
                .andExpect(jsonPath("$.format").value(TD.CompVer.FORMAT))
                .andExpect(jsonPath("$.qualityGrade").value(TD.CompVer.QUALITY_GRADE))
                .andExpect(jsonPath("$.versionValidated").value(TD.CompVer.VERSION_VALIDATED))
                .andExpect(jsonPath("$.versionValidationError").value(TD.CompVer.VERSION_VALIDATION_ERROR))
                .andExpect(jsonPath("$.versionAvoid").value(TD.CompVer.VERSION_AVOID))
                .andExpect(jsonPath("$.component.id").value(TD.Component.ID))
                .andExpect(jsonPath("$.component.name").value(TD.Component.NAME))
                .andExpect(jsonPath("$.component.componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$.component.platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$.component.assetInsightId").value(TD.Component.ASSET_INSIGHT_ID));
    }

    @Test
    public void getComponentVersionByIdReturn400() throws Exception {
        // given
        String notCorrectId = "idIsText";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component-versions/{id}", notCorrectId)
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void getComponentVersionByIdReturn404() throws Exception {
        // given
        doThrow(new ComponentVersionNotFoundException("")).when(componentVersionServiceMock).getComponentVersionById(anyLong());
        final Integer componentVersionIdNotExist = 2;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component-versions/{Id}", componentVersionIdNotExist)
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }


    /******************************************************************
     * TESTS FOR: - PUT /component-versions/{Id}
     ******************************************************************/
    @Test
    public void updateComponentVersionReturn200() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/component-versions/101")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"packageUrl\": \"http://package.url\",\n" +
                        "  \"format\": \"R10K\",\n" +
                        "  \"version\": \"<major>.<minor>.<patch>-<build>-<label>\",\n" +
                        "  \"qualityGrade\": \"DEVELOPMENT\",\n" +
                        "  \"versionValidated\": \"NEW\",\n" +
                        "  \"versionValidationError\": \"some string\",\n" +
                        "  \"versionAvoid\": \"BAD_RELEASE\"\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void updateComponentVersionReturn400WhenReqParamIdIsNotCorrect() throws Exception {
        // given
        final String notCorrectId = "idIsText";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/component-versions/{id}", notCorrectId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"packageUrl\": \"http://package.url\",\n" +
                        "  \"format\": \"R10K\",\n" +
                        "  \"version\": \"<major>.<minor>.<patch>-<build>-<label>\",\n" +
                        "  \"qualityGrade\": \"DEVELOPMENT\",\n" +
                        "  \"versionValidated\": \"NEW\",\n" +
                        "  \"versionValidationError\": \"some string\",\n" +
                        "  \"versionAvoid\": \"BAD_RELEASE\"\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateComponentVersionReturn400WhenReqParamIdAndDtoIdAreNotEquals() throws Exception {
        // given
        doThrow(new ReqParamIdAndDtoIdNotEqualsException("")).when(componentVersionServiceMock).updateComponentVersion(anyLong(), any());
        final Long reqParamId = 202L;
        final Long dtoId = 101L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/component-versions/{id}", reqParamId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\":" + dtoId + ",\n" +
                        "  \"packageUrl\": \"http://package.url\",\n" +
                        "  \"format\": \"R10K\",\n" +
                        "  \"version\": \"<major>.<minor>.<patch>-<build>-<label>\",\n" +
                        "  \"qualityGrade\": \"DEVELOPMENT\",\n" +
                        "  \"versionValidated\": \"NEW\",\n" +
                        "  \"versionValidationError\": \"some string\",\n" +
                        "  \"versionAvoid\": \"BAD_RELEASE\"\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateComponentVersionReturn404() throws Exception {
        // given
        doThrow(new ComponentVersionNotFoundException("")).when(componentVersionServiceMock).updateComponentVersion(anyLong(), any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/component-versions/101")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\n" +
                        "  \"id\": 101,\n" +
                        "  \"packageUrl\": \"http://package.url\",\n" +
                        "  \"format\": \"R10K\",\n" +
                        "  \"version\": \"<major>.<minor>.<patch>-<build>-<label>\",\n" +
                        "  \"qualityGrade\": \"DEVELOPMENT\",\n" +
                        "  \"versionValidated\": \"NEW\",\n" +
                        "  \"versionValidationError\": \"some string\",\n" +
                        "  \"versionAvoid\": \"BAD_RELEASE\"\n" +
                        "}");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }


    /**************************************************************************
     * TESTS FOR: - GET /component-versions?page=..&limit=..&search=..
     **************************************************************************/
    @Test
    public void searchComponentVersionsReturn200() throws Exception {
        // given
        when(componentVersionServiceMock.searchComponentVersions(anyInt(), anyInt(), any())).thenReturn(List.of(TD.createComponentVersionEntity(), TD.createComponentVersionEntity()));

        // when
        ResultActions result = mockMvc.perform(get("/component-versions")
                .param("page", "1")
                .param("limit", "2")
                .param("search", "componentName"));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TD.CompVer.ID))
                .andExpect(jsonPath("$[0].version").value(TD.CompVer.VERSION))
                .andExpect(jsonPath("$[0].packageUrl").value(TD.CompVer.PACKAGE_URL))
                .andExpect(jsonPath("$[0].format").value(TD.CompVer.FORMAT))
                .andExpect(jsonPath("$[0].qualityGrade").value(TD.CompVer.QUALITY_GRADE))
                .andExpect(jsonPath("$[0].versionValidated").value(TD.CompVer.VERSION_VALIDATED))
                .andExpect(jsonPath("$[0].versionValidationError").value(TD.CompVer.VERSION_VALIDATION_ERROR))
                .andExpect(jsonPath("$[0].versionAvoid").value(TD.CompVer.VERSION_AVOID))
                .andExpect(jsonPath("$[0].component.id").value(TD.Component.ID))
                .andExpect(jsonPath("$[0].component.name").value(TD.Component.NAME))
                .andExpect(jsonPath("$[0].component.componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$[0].component.platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$[0].component.assetInsightId").value(TD.Component.ASSET_INSIGHT_ID))
                .andExpect(jsonPath("$[1].id").value(TD.CompVer.ID))
                .andExpect(jsonPath("$[1].version").value(TD.CompVer.VERSION))
                .andExpect(jsonPath("$[1].packageUrl").value(TD.CompVer.PACKAGE_URL))
                .andExpect(jsonPath("$[1].format").value(TD.CompVer.FORMAT))
                .andExpect(jsonPath("$[1].qualityGrade").value(TD.CompVer.QUALITY_GRADE))
                .andExpect(jsonPath("$[1].versionValidated").value(TD.CompVer.VERSION_VALIDATED))
                .andExpect(jsonPath("$[1].versionValidationError").value(TD.CompVer.VERSION_VALIDATION_ERROR))
                .andExpect(jsonPath("$[1].versionAvoid").value(TD.CompVer.VERSION_AVOID))
                .andExpect(jsonPath("$[1].component.id").value(TD.Component.ID))
                .andExpect(jsonPath("$[1].component.name").value(TD.Component.NAME))
                .andExpect(jsonPath("$[1].component.componentGroup").value(TD.CompGroup.NAME))
                .andExpect(jsonPath("$[1].component.platform").value(TD.Platform.NAME))
                .andExpect(jsonPath("$[1].component.assetInsightId").value(TD.Component.ASSET_INSIGHT_ID));
    }

    @Test
    public void searchComponentVersionsReturn400WhenQueryParamSearchIsMissed() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component-versions");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void searchComponentVersionsReturn400WhenQueryParamPageHasText() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component-versions")
                .param("page", "text")
                .param("search", "componentName");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void searchComponentVersionsReturn400WhenQueryParamLimitHasText() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/component-versions")
                .param("limit", "text")
                .param("search", "componentName");

        // when
        ResultActions result = mockMvc.perform(requestBuilder);

        // then
        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void searchComponentVersionsReturn404() throws Exception {
        // given
        when(componentVersionServiceMock.searchComponentVersions(anyInt(), anyInt(), any())).thenThrow(new ComponentVersionsNotFoundException(""));

        // when
        ResultActions result = mockMvc.perform(get("/component-versions").param("search", "componentName"));

        // then
        result.andDo(print()).andExpect(status().isNotFound());
    }
}
