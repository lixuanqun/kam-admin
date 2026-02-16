package io.kamailio.admin.modules.rtpengine;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.modules.rtpengine.service.RtpengineService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.rtpengine.controller.RtpengineController.class)
class RtpengineControllerTest extends ControllerTestBase {

    @MockBean
    private RtpengineService service;

    @Test
    void getStatus_returnsOkWithCode0() throws Exception {
        when(service.getStatus()).thenReturn(Map.of("status", "ok"));
        ResultActions actions = mockMvc.perform(get("/api/rtpengine/status"));
        expectSuccess(actions).andExpect(jsonPath("$.data.status").value("ok"));
    }
}
