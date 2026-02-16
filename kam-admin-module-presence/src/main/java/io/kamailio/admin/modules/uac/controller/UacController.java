package io.kamailio.admin.modules.uac.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.uac.entity.UacReg;
import io.kamailio.admin.modules.uac.service.UacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UAC 注册")
@RestController
@RequestMapping("/api/uac")
public class UacController {
    private final UacService service;

    public UacController(UacService service) { this.service = service; }

    @GetMapping("/registrations")
    @Operation(summary = "获取 UAC 注册列表")
    public ResponseEntity<ApiResponse<PaginatedResult<UacReg>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @PostMapping("/registrations")
    public ResponseEntity<ApiResponse<UacReg>> create(@RequestBody UacReg data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/registrations/{id}")
    public ResponseEntity<ApiResponse<UacReg>> update(@PathVariable Integer id, @RequestBody UacReg data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/registrations/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载 UAC 注册")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/info/{lUuid}")
    @Operation(summary = "获取注册信息")
    public ResponseEntity<ApiResponse<Object>> getInfo(@PathVariable String lUuid) {
        return ResponseEntity.ok(ApiResponse.success(service.getInfo(lUuid)));
    }

    @PostMapping("/refresh/{lUuid}")
    @Operation(summary = "刷新注册")
    public ResponseEntity<ApiResponse<Void>> refresh(@PathVariable String lUuid) {
        service.refresh(lUuid);
        return ResponseEntity.ok(ApiResponse.success(null, "刷新成功"));
    }

    @GetMapping("/dump")
    @Operation(summary = "导出注册列表")
    public ResponseEntity<ApiResponse<Object>> dumpList() {
        return ResponseEntity.ok(ApiResponse.success(service.dumpList()));
    }
}
