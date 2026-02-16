package io.kamailio.admin.modules.permissions.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.permissions.dto.CreateAddressDto;
import io.kamailio.admin.modules.permissions.dto.CreateTrustedDto;
import io.kamailio.admin.modules.permissions.entity.Address;
import io.kamailio.admin.modules.permissions.entity.Trusted;
import io.kamailio.admin.modules.permissions.service.PermissionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "权限管理")
@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {
    private final PermissionsService service;

    public PermissionsController(PermissionsService service) { this.service = service; }

    @PostMapping("/address")
    @Operation(summary = "创建地址白名单")
    public ResponseEntity<ApiResponse<Address>> createAddress(@Valid @RequestBody CreateAddressDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.createAddress(dto), "创建成功"));
    }

    @GetMapping("/address")
    @Operation(summary = "获取地址白名单列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Address>>> findAllAddresses(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllAddresses(dto)));
    }

    @GetMapping("/address/{id}")
    @Operation(summary = "获取地址详情")
    public ResponseEntity<ApiResponse<Address>> findOneAddress(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(service.findOneAddress(id)));
    }

    @PatchMapping("/address/{id}")
    @Operation(summary = "更新地址")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@PathVariable Integer id, @RequestBody CreateAddressDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.updateAddress(id, dto), "更新成功"));
    }

    @DeleteMapping("/address/{id}")
    @Operation(summary = "删除地址")
    public ResponseEntity<ApiResponse<Void>> removeAddress(@PathVariable Integer id) {
        service.removeAddress(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/trusted")
    @Operation(summary = "创建信任地址")
    public ResponseEntity<ApiResponse<Trusted>> createTrusted(@Valid @RequestBody CreateTrustedDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.createTrusted(dto), "创建成功"));
    }

    @GetMapping("/trusted")
    @Operation(summary = "获取信任地址列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Trusted>>> findAllTrusted(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllTrusted(dto)));
    }

    @GetMapping("/trusted/{id}")
    @Operation(summary = "获取信任地址详情")
    public ResponseEntity<ApiResponse<Trusted>> findOneTrusted(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(service.findOneTrusted(id)));
    }

    @PatchMapping("/trusted/{id}")
    @Operation(summary = "更新信任地址")
    public ResponseEntity<ApiResponse<Trusted>> updateTrusted(@PathVariable Integer id, @RequestBody CreateTrustedDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.updateTrusted(id, dto), "更新成功"));
    }

    @DeleteMapping("/trusted/{id}")
    @Operation(summary = "删除信任地址")
    public ResponseEntity<ApiResponse<Void>> removeTrusted(@PathVariable Integer id) {
        service.removeTrusted(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载权限配置")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取权限统计信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }

    @PostMapping("/reload-trusted")
    @Operation(summary = "重载信任源表")
    public ResponseEntity<ApiResponse<Void>> reloadTrusted() {
        service.reloadTrusted();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/address-dump")
    @Operation(summary = "导出内存地址表")
    public ResponseEntity<ApiResponse<Object>> addressDump() {
        return ResponseEntity.ok(ApiResponse.success(service.addressDump()));
    }

    @GetMapping("/subnet-dump")
    @Operation(summary = "导出内存子网表")
    public ResponseEntity<ApiResponse<Object>> subnetDump() {
        return ResponseEntity.ok(ApiResponse.success(service.subnetDump()));
    }
}
