package io.kamailio.admin.modules.mtree.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.mtree.entity.Mtree;
import io.kamailio.admin.modules.mtree.service.MtreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "内存树")
@RestController
@RequestMapping("/api/mtree")
public class MtreeController {
    private final MtreeService service;

    public MtreeController(MtreeService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取 mtree 列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Mtree>>> findAll(PaginationDto dto, @RequestParam(required = false) String tname) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto, tname)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Mtree>> create(@RequestBody Mtree data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Mtree>> update(@PathVariable Integer id, @RequestBody Mtree data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载 mtree")
    public ResponseEntity<ApiResponse<Void>> reload(@RequestBody Map<String, String> body) {
        service.reload(body.get("tname"));
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/match")
    @Operation(summary = "匹配测试")
    public ResponseEntity<ApiResponse<Object>> match(@RequestParam String tname, @RequestParam String prefix) {
        return ResponseEntity.ok(ApiResponse.success(service.match(tname, prefix)));
    }

    @GetMapping("/summary")
    @Operation(summary = "获取摘要")
    public ResponseEntity<ApiResponse<Object>> summary(@RequestParam(required = false) String tname) {
        return ResponseEntity.ok(ApiResponse.success(service.summary(tname)));
    }
}
