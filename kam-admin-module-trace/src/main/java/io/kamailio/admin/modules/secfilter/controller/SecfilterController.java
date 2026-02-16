package io.kamailio.admin.modules.secfilter.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.secfilter.entity.Secfilter;
import io.kamailio.admin.modules.secfilter.service.SecfilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "安全过滤")
@RestController
@RequestMapping("/api/secfilter")
public class SecfilterController {
    private final SecfilterService service;

    public SecfilterController(SecfilterService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取安全过滤规则列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Secfilter>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Secfilter>> create(@RequestBody Secfilter data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Secfilter>> update(@PathVariable Integer id, @RequestBody Secfilter data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载安全过滤")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/print")
    @Operation(summary = "打印规则")
    public ResponseEntity<ApiResponse<Object>> print() {
        return ResponseEntity.ok(ApiResponse.success(service.print()));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取统计")
    public ResponseEntity<ApiResponse<Object>> stats() {
        return ResponseEntity.ok(ApiResponse.success(service.stats()));
    }

    @PostMapping("/stats/reset")
    @Operation(summary = "重置统计")
    public ResponseEntity<ApiResponse<Void>> statsReset() {
        service.statsReset();
        return ResponseEntity.ok(ApiResponse.success(null, "重置成功"));
    }

    @PostMapping("/add-bl")
    @Operation(summary = "添加黑名单")
    public ResponseEntity<ApiResponse<Void>> addBlacklist(@RequestBody Map<String, Object> body) {
        service.addBlacklist(((Number) body.get("type")).intValue(), (String) body.get("data"));
        return ResponseEntity.ok(ApiResponse.success(null, "添加成功"));
    }

    @PostMapping("/add-wl")
    @Operation(summary = "添加白名单")
    public ResponseEntity<ApiResponse<Void>> addWhitelist(@RequestBody Map<String, Object> body) {
        service.addWhitelist(((Number) body.get("type")).intValue(), (String) body.get("data"));
        return ResponseEntity.ok(ApiResponse.success(null, "添加成功"));
    }
}
