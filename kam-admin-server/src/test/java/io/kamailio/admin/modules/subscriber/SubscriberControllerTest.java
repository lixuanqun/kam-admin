package io.kamailio.admin.modules.subscriber;

import io.kamailio.admin.ControllerTestBase;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.subscriber.entity.Subscriber;
import io.kamailio.admin.modules.subscriber.service.SubscriberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(io.kamailio.admin.modules.subscriber.controller.SubscriberController.class)
class SubscriberControllerTest extends ControllerTestBase {

    @MockBean
    private SubscriberService service;

    @Test
    void findAll_returnsOkWithCode0() throws Exception {
        when(service.findAll(any(PaginationDto.class)))
                .thenReturn(new PaginatedResult<>(List.of(), 0L, 1, 20));
        ResultActions actions = mockMvc.perform(get("/api/subscribers").param("page", "1").param("limit", "20"));
        expectSuccess(actions).andExpect(jsonPath("$.data.items").isArray());
    }

    @Test
    void getStats_returnsOkWithCode0() throws Exception {
        when(service.getStats()).thenReturn(Map.of("total", 0));
        ResultActions actions = mockMvc.perform(get("/api/subscribers/stats"));
        expectSuccess(actions).andExpect(jsonPath("$.data.total").value(0));
    }
}
