package io.kamailio.admin.modules.version.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.modules.version.entity.Version;
import io.kamailio.admin.modules.version.service.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "版本")
@RestController
@RequestMapping("/api/version")
public class VersionController {
    private final VersionService service;

    public VersionController(VersionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "获取所有表版本")
    public ResponseEntity<ApiResponse<List<Version>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(service.findAll()));
    }

    @GetMapping("/{name}")
    @Operation(summary = "获取指定表版本")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getVersion(@PathVariable String name) {
        Map<String, Object> m = new HashMap<>();
        m.put("version", service.getVersion(name));
        return ResponseEntity.ok(ApiResponse.success(m));
    }

    @GetMapping("/stats")
    @Operation(summary = "获取版本统计")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(service.getStats()));
    }
}
