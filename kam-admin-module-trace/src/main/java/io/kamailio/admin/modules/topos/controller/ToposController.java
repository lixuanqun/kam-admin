package io.kamailio.admin.modules.topos.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.topos.entity.ToposD;
import io.kamailio.admin.modules.topos.service.ToposService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Tag(name = "Topos 拓扑隐藏")
@RestController
@RequestMapping("/api/topos")
public class ToposController {
    private final ToposService service;

    public ToposController(ToposService service) { this.service = service; }

    @GetMapping("/dialogs")
    @Operation(summary = "获取 Topos 对话列表")
    public ResponseEntity<ApiResponse<PaginatedResult<ToposD>>> findAllDialogs(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllDialogs(dto)));
    }

    @PostMapping("/cleanup")
    @Operation(summary = "清理旧记录")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cleanup(@RequestBody Map<String, Integer> body) {
        int days = body != null && body.containsKey("days") ? body.get("days") : 7;
        Instant beforeDate = Instant.now().minus(days, ChronoUnit.DAYS);
        int count = service.cleanup(beforeDate);
        return ResponseEntity.ok(ApiResponse.success(Map.of("deleted", count), "清理了 " + count + " 条记录"));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }
}
