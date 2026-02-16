package io.kamailio.admin.modules.usrpreferences.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.usrpreferences.entity.UsrPreferences;
import io.kamailio.admin.modules.usrpreferences.service.UsrpreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户偏好设置")
@RestController
@RequestMapping("/api/usrpreferences")
public class UsrpreferencesController {
    private final UsrpreferencesService service;

    public UsrpreferencesController(UsrpreferencesService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取用户偏好列表")
    public ResponseEntity<ApiResponse<PaginatedResult<UsrPreferences>>> findAll(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAll(dto)));
    }

    @GetMapping("/user")
    @Operation(summary = "获取指定用户偏好")
    public ResponseEntity<ApiResponse<List<UsrPreferences>>> findByUser(@RequestParam String username, @RequestParam String domain) {
        return ResponseEntity.ok(ApiResponse.success(service.findByUser(username, domain)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsrPreferences>> create(@RequestBody UsrPreferences data) {
        return ResponseEntity.ok(ApiResponse.success(service.create(data), "创建成功"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UsrPreferences>> update(@PathVariable Integer id, @RequestBody UsrPreferences data) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, data), "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }
}
