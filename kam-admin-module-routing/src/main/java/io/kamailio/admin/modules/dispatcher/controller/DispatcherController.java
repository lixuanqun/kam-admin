package io.kamailio.admin.modules.dispatcher.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.dispatcher.dto.CreateDispatcherDto;
import io.kamailio.admin.modules.dispatcher.dto.UpdateDispatcherDto;
import io.kamailio.admin.modules.dispatcher.entity.Dispatcher;
import io.kamailio.admin.modules.dispatcher.service.DispatcherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "调度器管理")
@RestController
@RequestMapping("/api/dispatchers")
public class DispatcherController {

    private final DispatcherService service;

    public DispatcherController(DispatcherService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "创建调度目标")
    public ResponseEntity<ApiResponse<Dispatcher>> create(@Valid @RequestBody CreateDispatcherDto dto) {
        var entity = service.create(dto);
        return ResponseEntity.ok(ApiResponse.success(entity, "创建成功"));
    }

    @GetMapping
    @Operation(summary = "获取调度目标列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Dispatcher>>> findAll(PaginationDto dto) {
        var result = service.findAll(dto);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/setids")
    @Operation(summary = "获取所有调度组 ID")
    public ResponseEntity<ApiResponse<List<Integer>>> getSetIds() {
        var ids = service.getSetIds();
        return ResponseEntity.ok(ApiResponse.success(ids));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取调度器统计信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        var stats = service.getStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/status")
    @Operation(summary = "获取调度器实时状态")
    public ResponseEntity<ApiResponse<Object>> getStatus() {
        var status = service.getStatus();
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @GetMapping("/setid/{setid}")
    @Operation(summary = "根据调度组获取调度目标")
    public ResponseEntity<ApiResponse<List<Dispatcher>>> findBySetId(@PathVariable Integer setid) {
        var list = service.findBySetId(setid);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取调度目标详情")
    public ResponseEntity<ApiResponse<Dispatcher>> findOne(@PathVariable Integer id) {
        var entity = service.findOne(id);
        return ResponseEntity.ok(ApiResponse.success(entity));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "更新调度目标")
    public ResponseEntity<ApiResponse<Dispatcher>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateDispatcherDto dto) {
        var entity = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(entity, "更新成功"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除调度目标")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载调度器配置")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @PostMapping("/set-state")
    @Operation(summary = "设置调度目标状态 (a=active,i=inactive,p=probing,d=disabled,t=trying)")
    public ResponseEntity<ApiResponse<Void>> setState(@RequestBody SetStateRequest body) {
        service.setState(body.getState(), body.getGroup(), body.getAddress());
        return ResponseEntity.ok(ApiResponse.success(null, "状态设置成功"));
    }

    public static class SetStateRequest {
        private String state;
        private Integer group;
        private String address;

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public Integer getGroup() { return group; }
        public void setGroup(Integer group) { this.group = group; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
}
