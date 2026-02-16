package io.kamailio.admin.modules.domain.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.domain.dto.CreateDomainDto;
import io.kamailio.admin.modules.domain.dto.UpdateDomainDto;
import io.kamailio.admin.modules.domain.entity.Domain;
import io.kamailio.admin.modules.domain.service.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "域管理")
@RestController
@RequestMapping("/api/domains")
public class DomainController {
    private final DomainService service;

    public DomainController(DomainService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "创建域")
    public ResponseEntity<ApiResponse<Domain>> create(@Valid @RequestBody CreateDomainDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.create(dto), "创建成功"));
    }

    @GetMapping
    @Operation(summary = "获取域列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Domain>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有域（不分页）")
    public ResponseEntity<ApiResponse<List<Domain>>> findAllDomains() {
        return ResponseEntity.ok(ApiResponse.success(service.findAllDomains()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取域详情")
    public ResponseEntity<ApiResponse<Domain>> findOne(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(service.findOne(id)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "更新域")
    public ResponseEntity<ApiResponse<Domain>> update(@PathVariable Integer id, @Valid @RequestBody UpdateDomainDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, dto), "更新成功"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除域")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载域配置")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/dump")
    @Operation(summary = "导出内存域列表")
    public ResponseEntity<ApiResponse<Object>> dump() {
        return ResponseEntity.ok(ApiResponse.success(service.dump()));
    }
}
