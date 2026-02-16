package io.kamailio.admin.modules.uac;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.uac.service.UacService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.uac.controller.UacController.class)
class UacControllerTest extends ControllerTestBase {

    @MockBean
    private UacService service;

    @Test
    void findAll_returnsOkWithCode0() throws Exception {
        when(service.findAll(any(PaginationDto.class)))
                .thenReturn(new PaginatedResult<>(List.of(), 0L, 1, 20));
        ResultActions actions = mockMvc.perform(get("/api/uac/registrations").param("page", "1").param("limit", "20"));
        expectSuccess(actions).andExpect(jsonPath("$.data.items").isArray());
    }
}
