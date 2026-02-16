package io.kamailio.admin.modules.htable.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.htable.entity.Htable;
import io.kamailio.admin.modules.htable.service.HtableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "哈希表")
@RestController
@RequestMapping("/api/htable")
public class HtableController {
    private final HtableService service;

    public HtableController(HtableService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取哈希表记录（数据库）")
    public ResponseEntity<ApiResponse<PaginatedResult<Htable>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Htable>> create(@RequestBody Htable data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Htable>> update(@PathVariable Integer id, @RequestBody Htable data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/rpc/get")
    @Operation(summary = "获取内存值")
    public ResponseEntity<ApiResponse<Object>> getValue(@RequestParam String table, @RequestParam String key) {
        return ResponseEntity.ok(ApiResponse.success(service.getValue(table, key)));
    }

    @PostMapping("/rpc/sets")
    @Operation(summary = "设置字符串值")
    public ResponseEntity<ApiResponse<Void>> setString(@RequestBody Map<String, String> body) {
        service.setString(body.get("table"), body.get("key"), body.get("value"));
        return ResponseEntity.ok(ApiResponse.success(null, "设置成功"));
    }

    @PostMapping("/rpc/seti")
    @Operation(summary = "设置整数值")
    public ResponseEntity<ApiResponse<Void>> setInt(@RequestBody Map<String, Object> body) {
        service.setInt((String) body.get("table"), (String) body.get("key"), ((Number) body.get("value")).intValue());
        return ResponseEntity.ok(ApiResponse.success(null, "设置成功"));
    }

    @PostMapping("/rpc/delete")
    @Operation(summary = "删除键")
    public ResponseEntity<ApiResponse<Void>> deleteKey(@RequestBody Map<String, String> body) {
        service.deleteKey(body.get("table"), body.get("key"));
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/rpc/dump")
    @Operation(summary = "导出表")
    public ResponseEntity<ApiResponse<Object>> dump(@RequestParam String table) {
        return ResponseEntity.ok(ApiResponse.success(service.dump(table)));
    }

    @PostMapping("/rpc/reload")
    @Operation(summary = "重载表")
    public ResponseEntity<ApiResponse<Void>> reload(@RequestBody Map<String, String> body) {
        service.reload(body.get("table"));
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/rpc/tables")
    @Operation(summary = "列出所有表")
    public ResponseEntity<ApiResponse<Object>> listTables() {
        return ResponseEntity.ok(ApiResponse.success(service.listTables()));
    }

    @GetMapping("/rpc/stats")
    @Operation(summary = "获取哈希表统计")
    public ResponseEntity<ApiResponse<Object>> stats() {
        return ResponseEntity.ok(ApiResponse.success(service.stats()));
    }
}
