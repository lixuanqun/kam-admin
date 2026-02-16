package io.kamailio.admin;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 校验 OpenAPI/Swagger 文档端点可访问且返回合法 JSON。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void v3ApiDocs_returnsOkAndValidOpenApiJson() throws Exception {
        ResultActions actions = mockMvc.perform(get("/v3/api-docs"));
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.openapi").value(Matchers.startsWith("3.0")))
                .andExpect(jsonPath("$.paths").isMap());
    }

    @Test
    void swaggerUi_returnsOk() throws Exception {
        mockMvc.perform(get("/api/docs"))
                .andExpect(status().isOk());
    }
}
