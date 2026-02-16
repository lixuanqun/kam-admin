package io.kamailio.admin.modules.userdata.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.userdata.entity.DbAliases;
import io.kamailio.admin.modules.userdata.entity.Grp;
import io.kamailio.admin.modules.userdata.entity.SpeedDial;
import io.kamailio.admin.modules.userdata.service.UserdataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户数据")
@RestController
@RequestMapping("/api/userdata")
public class UserdataController {
    private final UserdataService service;

    public UserdataController(UserdataService service) { this.service = service; }

    @GetMapping("/aliases")
    @Operation(summary = "获取别名列表")
    public ResponseEntity<ApiResponse<PaginatedResult<DbAliases>>> findAllAliases(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllAliases(dto)));
    }

    @PostMapping("/aliases")
    public ResponseEntity<ApiResponse<DbAliases>> createAlias(@RequestBody DbAliases data) {
        return ResponseEntity.ok(ApiResponse.success(service.createAlias(data), "创建成功"));
    }

    @PatchMapping("/aliases/{id}")
    public ResponseEntity<ApiResponse<DbAliases>> updateAlias(@PathVariable Integer id, @RequestBody DbAliases data) {
        return ResponseEntity.ok(ApiResponse.success(service.updateAlias(id, data), "更新成功"));
    }

    @DeleteMapping("/aliases/{id}")
    public ResponseEntity<ApiResponse<Void>> removeAlias(@PathVariable Integer id) {
        service.removeAlias(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/groups")
    @Operation(summary = "获取用户组列表")
    public ResponseEntity<ApiResponse<PaginatedResult<Grp>>> findAllGroups(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllGroups(dto)));
    }

    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<Grp>> createGroup(@RequestBody Grp data) {
        return ResponseEntity.ok(ApiResponse.success(service.createGroup(data), "创建成功"));
    }

    @PatchMapping("/groups/{id}")
    public ResponseEntity<ApiResponse<Grp>> updateGroup(@PathVariable Integer id, @RequestBody Grp data) {
        return ResponseEntity.ok(ApiResponse.success(service.updateGroup(id, data), "更新成功"));
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<ApiResponse<Void>> removeGroup(@PathVariable Integer id) {
        service.removeGroup(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/speed-dial")
    @Operation(summary = "获取快捷拨号列表")
    public ResponseEntity<ApiResponse<PaginatedResult<SpeedDial>>> findAllSpeedDials(PaginationDto dto) {
        return ResponseEntity.ok(ApiResponse.success(service.findAllSpeedDials(dto)));
    }

    @PostMapping("/speed-dial")
    public ResponseEntity<ApiResponse<SpeedDial>> createSpeedDial(@RequestBody SpeedDial data) {
        return ResponseEntity.ok(ApiResponse.success(service.createSpeedDial(data), "创建成功"));
    }

    @PatchMapping("/speed-dial/{id}")
    public ResponseEntity<ApiResponse<SpeedDial>> updateSpeedDial(@PathVariable Integer id, @RequestBody SpeedDial data) {
        return ResponseEntity.ok(ApiResponse.success(service.updateSpeedDial(id, data), "更新成功"));
    }

    @DeleteMapping("/speed-dial/{id}")
    public ResponseEntity<ApiResponse<Void>> removeSpeedDial(@PathVariable Integer id) {
        service.removeSpeedDial(id);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }
}
