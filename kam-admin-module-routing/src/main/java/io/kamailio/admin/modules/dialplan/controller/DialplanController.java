package io.kamailio.admin.modules.dialplan.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.dialplan.entity.Dialplan;
import io.kamailio.admin.modules.dialplan.service.DialplanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "拨号计划")
@RestController
@RequestMapping("/api/dialplan")
public class DialplanController {
    private final DialplanService service;

    public DialplanController(DialplanService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取拨号规则列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Dialplan>>> findAll(PaginationDto dto, @RequestParam(required = false) Integer dpid) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto, dpid)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Dialplan>> create(@RequestBody Dialplan data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Dialplan>> update(@PathVariable Integer id, @RequestBody Dialplan data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载拨号计划")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @GetMapping("/translate")
    @Operation(summary = "测试翻译")
    public ResponseEntity<ApiResponse<Object>> translate(@RequestParam int dpid, @RequestParam String input) {
        return ResponseEntity.ok(ApiResponse.success(service.translate(dpid, input)));
    }

    @GetMapping("/dump")
    @Operation(summary = "导出规则")
    public ResponseEntity<ApiResponse<Object>> dump(@RequestParam(required = false) Integer dpid) {
        return ResponseEntity.ok(ApiResponse.success(service.dump(dpid)));
    }
}
