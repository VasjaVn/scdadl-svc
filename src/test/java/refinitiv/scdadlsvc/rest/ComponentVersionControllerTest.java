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
import refinitiv.scdadlsvc.service.ComponentVersionService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComponentVersionController.class)
public class ComponentVersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComponentVersionService componentVersionServiceMock;

    @Test
    public void getComponentVersionsByComponentIdReturn200() throws Exception {

    }

    @Test
    public void createComponentVersionReturn201() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/component/1/versions")
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
        result.andDo(print()).andExpect(status().isCreated());
    }
}
