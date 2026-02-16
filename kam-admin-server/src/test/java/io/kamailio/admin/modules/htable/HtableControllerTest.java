package io.kamailio.admin.modules.htable;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.htable.service.HtableService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.htable.controller.HtableController.class)
class HtableControllerTest extends ControllerTestBase {

    @MockBean
    private HtableService service;

    @Test
    void findAll_returnsOkWithCode0() throws Exception {
        when(service.findAll(any(PaginationDto.class)))
                .thenReturn(new PaginatedResult<>(List.of(), 0L, 1, 20));
        ResultActions actions = mockMvc.perform(get("/api/htable").param("page", "1").param("limit", "20"));
        expectSuccess(actions).andExpect(jsonPath("$.data.items").isArray());
    }
}
