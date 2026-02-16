package io.kamailio.admin.modules.presence.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.presence.entity.ActiveWatchers;
import io.kamailio.admin.modules.presence.entity.Presentity;
import io.kamailio.admin.modules.presence.service.PresenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "存在服务")
@RestController
@RequestMapping("/api/presence")
public class PresenceController {
    private final PresenceService service;

    public PresenceController(PresenceService service) { this.service = service; }

    @GetMapping("/presentities")
    @Operation(summary = "获取 Presentity 列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Presentity>>> findAllPresentities(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllPresentities(dto)));
    }

    @GetMapping("/watchers")
    @Operation(summary = "获取活动 Watcher 列表")
    public ResponseEntity<ApiResponse<PaginatedResult<ActiveWatchers>>> findAllWatchers(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllWatchers(dto)));
    }

    @PostMapping("/cleanup")
    @Operation(summary = "清理过期记录")
    public ResponseEntity<ApiResponse<Void>> cleanExpired() {
        service.cleanExpired();
        return ResponseEntity.ok(ApiResponse.success(null, "清理成功"));
    }

    @PostMapping("/refresh-watchers")
    @Operation(summary = "刷新 Watchers")
    public ResponseEntity<ApiResponse<Void>> refreshWatchers(@RequestBody Map<String, String> body) {
        service.refreshWatchers(body.get("presentityUri"), body.get("event"));
        return ResponseEntity.ok(ApiResponse.success(null, "刷新成功"));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }
}
