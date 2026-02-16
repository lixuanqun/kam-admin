package io.kamailio.admin.modules.acc.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.modules.acc.dto.QueryAccDto;
import io.kamailio.admin.modules.acc.entity.Acc;
import io.kamailio.admin.modules.acc.entity.MissedCall;
import io.kamailio.admin.modules.acc.service.AccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "计费记录")
@RestController
@RequestMapping("/api/acc")
public class AccController {
    private final AccService service;

    public AccController(AccService service) { this.service = service; }

    @GetMapping("/cdr")
    @Operation(summary = "获取 CDR 记录")
    public ResponseEntity<ApiResponse<PaginatedResult<Acc>>> findAllAcc(QueryAccDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllAcc(dto)));
    }

    @GetMapping("/missed-calls")
    @Operation(summary = "获取未接来电")
    public ResponseEntity<ApiResponse<PaginatedResult<MissedCall>>> findAllMissedCalls(QueryAccDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllMissedCalls(dto)));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats(@RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        return ResponseEntity.ok(ApiResponse.success(service.getStats(startTime, endTime)));
    }

    @GetMapping("/today")
    @Operation(summary = "获取今日统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTodayStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getTodayStats()));
    }
}
