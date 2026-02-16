package io.kamailio.admin.modules.siptrace.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.modules.siptrace.entity.SipTrace;
import io.kamailio.admin.modules.siptrace.service.SiptraceService;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Tag(name = "SIP 跟踪")
@RestController
@RequestMapping("/api/siptrace")
public class SiptraceController {
    private final SiptraceService service;

    public SiptraceController(SiptraceService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取 SIP 跟踪记录")
    public ResponseEntity<ApiResponse<PaginatedResult<SipTrace>>> findAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String callid,
            @RequestParam(required = false) String tracedUser,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String fromIp,
            @RequestParam(required = false) String toIp,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(page, limit, callid, tracedUser, method, fromIp, toIp, startTime, endTime)));
    }

    @GetMapping("/call")
    @Operation(summary = "获取呼叫的完整跟踪")
    public ResponseEntity<ApiResponse<List<SipTrace>>> getByCallId(@RequestParam String callid) {
        return ResponseEntity.ok(ApiResponse.success(service.getByCallId(callid)));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }

    @PostMapping("/cleanup")
    @Operation(summary = "清理旧记录")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cleanup(@RequestBody Map<String, Integer> body) {
        int days = body != null && body.containsKey("days") ? body.get("days") : 30;
        Instant beforeDate = Instant.now().minus(days, ChronoUnit.DAYS);
        int count = service.cleanup(beforeDate);
        return ResponseEntity.ok(ApiResponse.success(Map.of("deleted", count), "清理了 " + count + " 条记录"));
    }
}
