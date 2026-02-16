package io.kamailio.admin.modules.pdt.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.pdt.entity.Pdt;
import io.kamailio.admin.modules.pdt.service.PdtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "前缀域转换")
@RestController
@RequestMapping("/api/pdt")
public class PdtController {
    private final PdtService service;

    public PdtController(PdtService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取 PDT 列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Pdt>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Pdt>> create(@RequestBody Pdt data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Pdt>> update(@PathVariable Integer id, @RequestBody Pdt data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载 PDT")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/list")
    @Operation(summary = "获取内存列表")
    public ResponseEntity<ApiResponse<Object>> list() {
        return ResponseEntity.ok(ApiResponse.success(service.list()));
    }
}
