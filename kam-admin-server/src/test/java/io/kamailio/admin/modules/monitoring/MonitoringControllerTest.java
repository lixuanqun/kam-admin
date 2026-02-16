package io.kamailio.admin.modules.monitoring;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.modules.monitoring.service.MonitoringService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.monitoring.controller.MonitoringController.class)
class MonitoringControllerTest extends ControllerTestBase {

    @MockBean
    private MonitoringService service;

    @Test
    void health_returnsOkWithCode0() throws Exception {
        when(service.health()).thenReturn(Map.of("status", "ok"));
        ResultActions actions = mockMvc.perform(get("/api/monitoring/health"));
        expectSuccess(actions).andExpect(jsonPath("$.data.status").value("ok"));
    }
}
