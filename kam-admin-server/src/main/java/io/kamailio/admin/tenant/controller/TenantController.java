package io.kamailio.admin.tenant.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.tenant.entity.Tenant;
import io.kamailio.admin.tenant.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "租户管理", description = "多租户体系：租户 CRUD 与当前租户信息")
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    @Operation(summary = "租户列表")
    public ResponseEntity<ApiResponse<List<Tenant>>> list() {
        return ResponseEntity.ok(ApiResponse.success(tenantService.list()));
    }

    @GetMapping("/current")
    @Operation(summary = "当前请求的租户（由 X-Tenant-Code / X-Tenant-Id 或默认租户决定）")
    public ResponseEntity<ApiResponse<Map<String, Object>>> current() {
        String code = io.kamailio.admin.tenant.TenantContext.getTenantCode();
        Long id = io.kamailio.admin.tenant.TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "tenantCode", code != null ? code : "",
                "tenantId", id != null ? id : ""
        )));
    }

    @GetMapping("/{id}")
    @Operation(summary = "按 ID 查询租户")
    public ResponseEntity<ApiResponse<Tenant>> getById(@PathVariable Long id) {
        return tenantService.findById(id)
                .map(t -> ResponseEntity.ok(ApiResponse.success(t)))
                .orElse(ResponseEntity.ok(ApiResponse.error("租户不存在", 404)));
    }

    @PostMapping
    @Operation(summary = "新增租户")
    public ResponseEntity<ApiResponse<Tenant>> create(@RequestBody CreateTenantRequest request) {
        Tenant t = tenantService.create(request.getTenantCode(), request.getName());
        return ResponseEntity.ok(ApiResponse.success(t, "创建成功"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新租户")
    public ResponseEntity<ApiResponse<Tenant>> update(@PathVariable Long id,
                                                       @RequestBody UpdateTenantRequest request) {
        Tenant t = tenantService.update(id, request.getName(), request.getStatus(),
                request.getRpcUrl(), request.getDbUrl());
        return ResponseEntity.ok(ApiResponse.success(t, "更新成功"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除租户")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    public static class CreateTenantRequest {
        private String tenantCode;
        private String name;
        public String getTenantCode() { return tenantCode; }
        public void setTenantCode(String tenantCode) { this.tenantCode = tenantCode; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class UpdateTenantRequest {
        private String name;
        private String status;
        private String rpcUrl;
        private String dbUrl;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getRpcUrl() { return rpcUrl; }
        public void setRpcUrl(String rpcUrl) { this.rpcUrl = rpcUrl; }
        public String getDbUrl() { return dbUrl; }
        public void setDbUrl(String dbUrl) { this.dbUrl = dbUrl; }
    }
}
