package io.kamailio.admin.modules.topos;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.topos.service.ToposService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.topos.controller.ToposController.class)
class ToposControllerTest extends ControllerTestBase {

    @MockBean
    private ToposService service;

    @Test
    void findAllDialogs_returnsOkWithCode0() throws Exception {
        when(service.findAllDialogs(any(PaginationDto.class)))
                .thenReturn(new PaginatedResult<>(List.of(), 0L, 1, 20));
        ResultActions actions = mockMvc.perform(get("/api/topos/dialogs").param("page", "1").param("limit", "20"));
        expectSuccess(actions).andExpect(jsonPath("$.data.items").isArray());
    }
}
