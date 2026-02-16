package io.kamailio.admin.modules.lcr.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.lcr.entity.LcrGw;
import io.kamailio.admin.modules.lcr.entity.LcrRule;
import io.kamailio.admin.modules.lcr.entity.LcrRuleTarget;
import io.kamailio.admin.modules.lcr.service.LcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LCR 路由")
@RestController
@RequestMapping("/api/lcr")
public class LcrController {
    private final LcrService service;

    public LcrController(LcrService service) { this.service = service; }

    @GetMapping("/gateways")
    @Operation(summary = "获取网关列表")
    public ResponseEntity<ApiResponse<PaginatedResult<LcrGw>>> findAllGateways(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllGateways(dto)));
    }

    @PostMapping("/gateways")
    public ResponseEntity<ApiResponse<LcrGw>> createGateway(@RequestBody LcrGw data) {
        return ResponseEntity.ok(ApiResponse.success(service.createGateway(data), "创建成功"));
    }

    @PatchMapping("/gateways/{id}")
    public ResponseEntity<ApiResponse<LcrGw>> updateGateway(@PathVariable Integer id, @RequestBody LcrGw data) {
        return ResponseEntity.ok(ApiResponse.success(service.updateGateway(id, data), "更新成功"));
    }

    @DeleteMapping("/gateways/{id}")
    public ResponseEntity<ApiResponse<Void>> removeGateway(@PathVariable Integer id) {
        service.removeGateway(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/rules")
    @Operation(summary = "获取规则列表")
    public ResponseEntity<ApiResponse<PaginatedResult<LcrRule>>> findAllRules(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllRules(dto)));
    }

    @PostMapping("/rules")
    public ResponseEntity<ApiResponse<LcrRule>> createRule(@RequestBody LcrRule data) {
        return ResponseEntity.ok(ApiResponse.success(service.createRule(data), "创建成功"));
    }

    @PatchMapping("/rules/{id}")
    public ResponseEntity<ApiResponse<LcrRule>> updateRule(@PathVariable Integer id, @RequestBody LcrRule data) {
        return ResponseEntity.ok(ApiResponse.success(service.updateRule(id, data), "更新成功"));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<ApiResponse<Void>> removeRule(@PathVariable Integer id) {
        service.removeRule(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/targets")
    @Operation(summary = "获取目标列表")
    public ResponseEntity<ApiResponse<PaginatedResult<LcrRuleTarget>>> findAllTargets(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllTargets(dto)));
    }

    @PostMapping("/targets")
    public ResponseEntity<ApiResponse<LcrRuleTarget>> createTarget(@RequestBody LcrRuleTarget data) {
        return ResponseEntity.ok(ApiResponse.success(service.createTarget(data), "创建成功"));
    }

    @PatchMapping("/targets/{id}")
    public ResponseEntity<ApiResponse<LcrRuleTarget>> updateTarget(@PathVariable Integer id, @RequestBody LcrRuleTarget data) {
        return ResponseEntity.ok(ApiResponse.success(service.updateTarget(id, data), "更新成功"));
    }

    @DeleteMapping("/targets/{id}")
    public ResponseEntity<ApiResponse<Void>> removeTarget(@PathVariable Integer id) {
        service.removeTarget(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载 LCR")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/dump-gws")
    @Operation(summary = "导出网关")
    public ResponseEntity<ApiResponse<Object>> dumpGws() {
        return ResponseEntity.ok(ApiResponse.success(service.dumpGws()));
    }

    @GetMapping("/dump-rules")
    @Operation(summary = "导出规则")
    public ResponseEntity<ApiResponse<Object>> dumpRules() {
        return ResponseEntity.ok(ApiResponse.success(service.dumpRules()));
    }
}
