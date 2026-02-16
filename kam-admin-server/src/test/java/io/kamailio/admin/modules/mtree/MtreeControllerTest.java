package io.kamailio.admin.modules.mtree;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.mtree.service.MtreeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(io.kamailio.admin.modules.mtree.controller.MtreeController.class)
class MtreeControllerTest extends ControllerTestBase {

    @MockBean
    private MtreeService service;

    @Test
    void findAll_returnsOkWithCode0() throws Exception {
        when(service.findAll(any(PaginationDto.class), eq(null)))
                .thenReturn(new PaginatedResult<>(List.of(), 0L, 1, 20));
        ResultActions actions = mockMvc.perform(get("/api/mtree").param("page", "1").param("limit", "20"));
        expectSuccess(actions).andExpect(jsonPath("$.data.items").isArray());
    }
}
