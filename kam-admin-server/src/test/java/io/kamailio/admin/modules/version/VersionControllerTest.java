package io.kamailio.admin.modules.version;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.modules.version.service.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.version.controller.VersionController.class)
class VersionControllerTest extends ControllerTestBase {

    @MockBean
    private VersionService service;

    @Test
    void list_returnsOkWithCode0() throws Exception {
        when(service.list()).thenReturn(List.of());
        ResultActions actions = mockMvc.perform(get("/api/version"));
        expectSuccess(actions).andExpect(jsonPath("$.data").isArray());
    }
}
