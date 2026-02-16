package io.kamailio.admin.modules.drouting.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.drouting.entity.DrCarrier;
import io.kamailio.admin.modules.drouting.entity.DrGateway;
import io.kamailio.admin.modules.drouting.entity.DrGroup;
import io.kamailio.admin.modules.drouting.entity.DrRule;
import io.kamailio.admin.modules.drouting.service.DroutingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "动态路由")
@RestController
@RequestMapping("/api/drouting")
public class DroutingController {
    private final DroutingService service;

    public DroutingController(DroutingService service) { this.service = service; }

    @GetMapping("/gateways") public ResponseEntity<ApiResponse<PaginatedResult<DrGateway>>> findAllGateways(PaginationDto dto) { return ResponseEntity.ok(ApiResponse.success(service.findAllGateways(dto))); }
    @PostMapping("/gateways") public ResponseEntity<ApiResponse<DrGateway>> createGateway(@RequestBody DrGateway data) { return ResponseEntity.ok(ApiResponse.success(service.createGateway(data), "创建成功")); }
    @PatchMapping("/gateways/{id}") public ResponseEntity<ApiResponse<DrGateway>> updateGateway(@PathVariable Integer id, @RequestBody DrGateway data) { return ResponseEntity.ok(ApiResponse.success(service.updateGateway(id, data), "更新成功")); }
    @DeleteMapping("/gateways/{id}") public ResponseEntity<ApiResponse<Void>> removeGateway(@PathVariable Integer id) { service.removeGateway(id); return ResponseEntity.ok(ApiResponse.success(null, "删除成功")); }

    @GetMapping("/rules") public ResponseEntity<ApiResponse<PaginatedResult<DrRule>>> findAllRules(PaginationDto dto) { return ResponseEntity.ok(ApiResponse.success(service.findAllRules(dto))); }
    @PostMapping("/rules") public ResponseEntity<ApiResponse<DrRule>> createRule(@RequestBody DrRule data) { return ResponseEntity.ok(ApiResponse.success(service.createRule(data), "创建成功")); }
    @PatchMapping("/rules/{id}") public ResponseEntity<ApiResponse<DrRule>> updateRule(@PathVariable Integer id, @RequestBody DrRule data) { return ResponseEntity.ok(ApiResponse.success(service.updateRule(id, data), "更新成功")); }
    @DeleteMapping("/rules/{id}") public ResponseEntity<ApiResponse<Void>> removeRule(@PathVariable Integer id) { service.removeRule(id); return ResponseEntity.ok(ApiResponse.success(null, "删除成功")); }

    @GetMapping("/groups") public ResponseEntity<ApiResponse<PaginatedResult<DrGroup>>> findAllGroups(PaginationDto dto) { return ResponseEntity.ok(ApiResponse.success(service.findAllGroups(dto))); }
    @PostMapping("/groups") public ResponseEntity<ApiResponse<DrGroup>> createGroup(@RequestBody DrGroup data) { return ResponseEntity.ok(ApiResponse.success(service.createGroup(data), "创建成功")); }
    @PatchMapping("/groups/{id}") public ResponseEntity<ApiResponse<DrGroup>> updateGroup(@PathVariable Integer id, @RequestBody DrGroup data) { return ResponseEntity.ok(ApiResponse.success(service.updateGroup(id, data), "更新成功")); }
    @DeleteMapping("/groups/{id}") public ResponseEntity<ApiResponse<Void>> removeGroup(@PathVariable Integer id) { service.removeGroup(id); return ResponseEntity.ok(ApiResponse.success(null, "删除成功")); }

    @GetMapping("/carriers") public ResponseEntity<ApiResponse<PaginatedResult<DrCarrier>>> findAllCarriers(PaginationDto dto) { return ResponseEntity.ok(ApiResponse.success(service.findAllCarriers(dto))); }
    @PostMapping("/carriers") public ResponseEntity<ApiResponse<DrCarrier>> createCarrier(@RequestBody DrCarrier data) { return ResponseEntity.ok(ApiResponse.success(service.createCarrier(data), "创建成功")); }
    @PatchMapping("/carriers/{id}") public ResponseEntity<ApiResponse<DrCarrier>> updateCarrier(@PathVariable Integer id, @RequestBody DrCarrier data) { return ResponseEntity.ok(ApiResponse.success(service.updateCarrier(id, data), "更新成功")); }
    @DeleteMapping("/carriers/{id}") public ResponseEntity<ApiResponse<Void>> removeCarrier(@PathVariable Integer id) { service.removeCarrier(id); return ResponseEntity.ok(ApiResponse.success(null, "删除成功")); }

    @PostMapping("/reload") public ResponseEntity<ApiResponse<Void>> reload() { service.reload(); return ResponseEntity.ok(ApiResponse.success(null, "重载成功")); }
    @GetMapping("/stats") public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() { return ResponseEntity.ok(ApiResponse.success(service.getStats())); }
    @GetMapping("/gw-status") public ResponseEntity<ApiResponse<Object>> getGwStatus() { return ResponseEntity.ok(ApiResponse.success(service.getGwStatus())); }
    @GetMapping("/carrier-status") public ResponseEntity<ApiResponse<Object>> getCarrierStatus() { return ResponseEntity.ok(ApiResponse.success(service.getCarrierStatus())); }
}
