package io.kamailio.admin.modules.msilo.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.msilo.entity.Silo;
import io.kamailio.admin.modules.msilo.service.MsiloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "离线消息")
@RestController
@RequestMapping("/api/msilo")
public class MsiloController {
    private final MsiloService service;

    public MsiloController(MsiloService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取离线消息列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Silo>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @GetMapping("/user")
    @Operation(summary = "获取用户的离线消息")
    public ResponseEntity<ApiResponse<List<Silo>>> findByUser(@RequestParam String username, @RequestParam String domain) {
        return ResponseEntity.ok(ApiResponse.success(service.findByUser(username, domain)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除离线消息")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/cleanup")
    @Operation(summary = "清理过期消息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cleanExpired() {
        int count = service.cleanExpired();
        return ResponseEntity.ok(ApiResponse.success(Map.of("deleted", count), "清理了 " + count + " 条过期消息"));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }

    @PostMapping("/dump")
    @Operation(summary = "导出消息")
    public ResponseEntity<ApiResponse<Void>> dump() {
        service.dump();
        return ResponseEntity.ok(ApiResponse.success(null, "导出成功"));
    }
}
