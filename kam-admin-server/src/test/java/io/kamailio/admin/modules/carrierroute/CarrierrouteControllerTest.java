package io.kamailio.admin.modules.carrierroute;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.modules.carrierroute.service.CarrierrouteService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(io.kamailio.admin.modules.carrierroute.controller.CarrierrouteController.class)
class CarrierrouteControllerTest extends ControllerTestBase {

    @MockBean
    private CarrierrouteService service;

    @Test
    void findAllCarriers_returnsOkWithCode0() throws Exception {
        when(service.findAllCarriers()).thenReturn(List.of());
        ResultActions actions = mockMvc.perform(get("/api/carrierroute/carriers"));
        expectSuccess(actions).andExpect(jsonPath("$.data").isArray());
    }
}
