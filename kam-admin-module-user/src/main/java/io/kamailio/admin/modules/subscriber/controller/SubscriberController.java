package io.kamailio.admin.modules.subscriber.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.subscriber.dto.CreateSubscriberDto;
import io.kamailio.admin.modules.subscriber.dto.UpdateSubscriberDto;
import io.kamailio.admin.modules.subscriber.entity.Subscriber;
import io.kamailio.admin.modules.subscriber.service.SubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {

    private final SubscriberService service;

    public SubscriberController(SubscriberService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "创建用户")
    public ResponseEntity<ApiResponse<Subscriber>> create(@Valid @RequestBody CreateSubscriberDto dto) {
        var entity = service.create(dto);
        return ResponseEntity.ok(ApiResponse.success(entity, "创建成功"));
    }

    @GetMapping
    @Operation(summary = "获取用户列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Subscriber>>> findAll(PaginationDto dto) {
        var result = service.findAll(dto);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取用户统计信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    public ResponseEntity<ApiResponse<Subscriber>> findOne(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(service.findOne(id)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "更新用户")
    public ResponseEntity<ApiResponse<Subscriber>> update(@PathVariable Integer id, @Valid @RequestBody UpdateSubscriberDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, dto), "更新成功"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除用户")
    public ResponseEntity<ApiResponse<Void>> batchRemove(@RequestBody BatchDeleteRequest body) {
        service.batchRemove(body.getIds());
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    public static class BatchDeleteRequest {
        private List<Integer> ids;
        public List<Integer> getIds() { return ids; }
        public void setIds(List<Integer> ids) { this.ids = ids; }
    }
}
