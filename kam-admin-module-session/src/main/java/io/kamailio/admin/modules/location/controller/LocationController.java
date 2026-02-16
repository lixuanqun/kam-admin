package io.kamailio.admin.modules.location.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.location.entity.Location;
import io.kamailio.admin.modules.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "注册位置管理")
@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService service;

    public LocationController(LocationService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取注册位置列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Location>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @GetMapping("/memory")
    @Operation(summary = "从内存获取注册位置")
    public ResponseEntity<ApiResponse<Object>> getFromMemory(@RequestParam(required = false) String table) {
        return ResponseEntity.ok(ApiResponse.success(service.getFromMemory(table)));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取注册统计信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }

    @GetMapping("/online-count")
    @Operation(summary = "获取在线用户数量")
    public ResponseEntity<ApiResponse<Long>> getOnlineCount() {
        return ResponseEntity.ok(ApiResponse.success(service.getOnlineCount()));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "根据用户名查询注册")
    public ResponseEntity<ApiResponse<List<Location>>> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(ApiResponse.success(service.findByUsername(username)));
    }

    @DeleteMapping("/{username}/{domain}")
    @Operation(summary = "删除用户注册")
    public ResponseEntity<ApiResponse<Void>> deleteUserLocation(@PathVariable String username, @PathVariable String domain) {
        service.deleteUserLocation(username, domain);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/lookup")
    @Operation(summary = "精确查找指定用户注册")
    public ResponseEntity<ApiResponse<Object>> lookupUser(@RequestParam(required = false) String table, @RequestParam String aor) {
        return ResponseEntity.ok(ApiResponse.success(service.lookupUser(table, aor)));
    }

    @DeleteMapping("/contact")
    @Operation(summary = "删除指定联系地址")
    public ResponseEntity<ApiResponse<Void>> deleteContact(@RequestBody Map<String, String> body) {
        service.deleteContact(body.get("table"), body.get("aor"), body.get("contact"));
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/flush")
    @Operation(summary = "将 usrloc 内存缓存刷入数据库")
    public ResponseEntity<ApiResponse<Void>> flushUsrloc() {
        service.flushUsrloc();
        return ResponseEntity.ok(ApiResponse.success(null, "刷新成功"));
    }
}
