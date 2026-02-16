package io.kamailio.admin.modules.monitoring.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.modules.monitoring.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "监控统计")
@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {
    private final MonitoringService service;

    public MonitoringController(MonitoringService service) { this.service = service; }

    @GetMapping("/health")
    @Operation(summary = "检查 Kamailio 服务状态")
    public ResponseEntity<ApiResponse<Object>> checkHealth() {
        return ResponseEntity.ok(ApiResponse.success(service.checkHealth()));
    }

    @GetMapping("/check-modules")
    @Operation(summary = "检查 Kamailio 关键模块是否可用")
    public ResponseEntity<ApiResponse<Object>> checkModules() {
        return ResponseEntity.ok(ApiResponse.success(service.checkModules()));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "获取仪表盘数据")
    public ResponseEntity<ApiResponse<Object>> getDashboardData() {
        return ResponseEntity.ok(ApiResponse.success(service.getDashboardData()));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取 Kamailio 统计信息")
    public ResponseEntity<ApiResponse<Object>> getStatistics(@RequestParam(required = false) String group) {
        return ResponseEntity.ok(ApiResponse.success(service.getStatistics(group)));
    }

    @GetMapping("/core-info")
    @Operation(summary = "获取核心信息")
    public ResponseEntity<ApiResponse<Object>> getCoreInfo() {
        return ResponseEntity.ok(ApiResponse.success(service.getCoreInfo()));
    }

    @GetMapping("/dialogs")
    @Operation(summary = "获取活动对话")
    public ResponseEntity<ApiResponse<Object>> getActiveDialogs() {
        return ResponseEntity.ok(ApiResponse.success(service.getActiveDialogs()));
    }

    @GetMapping("/overview")
    @Operation(summary = "获取系统概览")
    public ResponseEntity<ApiResponse<Object>> getSystemOverview() {
        return ResponseEntity.ok(ApiResponse.success(service.getSystemOverview()));
    }
}
