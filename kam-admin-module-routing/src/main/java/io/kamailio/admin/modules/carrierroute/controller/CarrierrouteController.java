package io.kamailio.admin.modules.carrierroute.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.carrierroute.entity.CarrierFailureRoute;
import io.kamailio.admin.modules.carrierroute.entity.CarrierName;
import io.kamailio.admin.modules.carrierroute.entity.DomainName;
import io.kamailio.admin.modules.carrierroute.service.CarrierrouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "运营商路由")
@RestController
@RequestMapping("/api/carrierroute")
public class CarrierrouteController {
    private final CarrierrouteService service;

    public CarrierrouteController(CarrierrouteService service) { this.service = service; }

    @GetMapping("/carriers")
    @Operation(summary = "获取运营商列表")
    public ResponseEntity<ApiResponse<List<CarrierName>>> findAllCarriers() {
        return ResponseEntity.ok(ApiResponse.success(service.findAllCarriers()));
    }

    @PostMapping("/carriers")
    public ResponseEntity<ApiResponse<CarrierName>> createCarrier(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success(service.createCarrier(body.get("carrier")), "创建成功"));
    }

    @DeleteMapping("/carriers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCarrier(@PathVariable Integer id) {
        service.deleteCarrier(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/domains")
    @Operation(summary = "获取域列表")
    public ResponseEntity<ApiResponse<List<DomainName>>> findAllDomains() {
        return ResponseEntity.ok(ApiResponse.success(service.findAllDomains()));
    }

    @PostMapping("/domains")
    public ResponseEntity<ApiResponse<DomainName>> createDomain(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success(service.createDomain(body.get("domain")), "创建成功"));
    }

    @DeleteMapping("/domains/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDomain(@PathVariable Integer id) {
        service.deleteDomain(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/failure-routes")
    @Operation(summary = "获取失败路由列表")
    public ResponseEntity<ApiResponse<PaginatedResult<CarrierFailureRoute>>> findAllFailureRoutes(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllFailureRoutes(dto)));
    }

    @PostMapping("/failure-routes")
    public ResponseEntity<ApiResponse<CarrierFailureRoute>> createFailureRoute(@RequestBody CarrierFailureRoute data) {
        return ResponseEntity.ok(ApiResponse.success(service.createFailureRoute(data), "创建成功"));
    }

    @DeleteMapping("/failure-routes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFailureRoute(@PathVariable Integer id) {
        service.deleteFailureRoute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载路由")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/dump")
    @Operation(summary = "导出路由")
    public ResponseEntity<ApiResponse<Object>> dumpRoutes() {
        return ResponseEntity.ok(ApiResponse.success(service.dumpRoutes()));
    }
}
