package io.kamailio.admin.modules.system;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.modules.system.service.SystemService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.system.controller.SystemController.class)
class SystemControllerTest extends ControllerTestBase {

    @MockBean
    private SystemService service;

    @Test
    void getCoreInfo_returnsOkWithCode0() throws Exception {
        when(service.getCoreInfo()).thenReturn(new Object());
        ResultActions actions = mockMvc.perform(get("/api/system/core-info"));
        expectSuccess(actions);
    }
}
