package io.kamailio.admin.modules.rtpengine.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.modules.rtpengine.service.RtpengineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "RTPEngine/NAT")
@RestController
@RequestMapping("/api/rtpengine")
public class RtpengineController {
    private final RtpengineService service;

    public RtpengineController(RtpengineService service) { this.service = service; }

    @GetMapping("/show")
    @Operation(summary = "显示 RTPEngine 状态")
    public ResponseEntity<ApiResponse<Object>> showAll() {
        return ResponseEntity.ok(ApiResponse.success(service.showAll()));
    }

    @PostMapping("/reload")
    @Operation(summary = "重载 RTPEngine")
    public ResponseEntity<ApiResponse<Void>> reload() {
        service.reload();
        return ResponseEntity.ok(ApiResponse.success(null, "重载成功"));
    }

    @PostMapping("/enable")
    @Operation(summary = "启用/禁用 RTPEngine")
    public ResponseEntity<ApiResponse<Void>> enable(@RequestBody Map<String, Object> body) {
        service.enable((String) body.get("url"), ((Number) body.get("flag")).intValue());
        return ResponseEntity.ok(ApiResponse.success(null, "操作成功"));
    }

    @PostMapping("/ping")
    @Operation(summary = "Ping RTPEngine")
    public ResponseEntity<ApiResponse<Object>> ping(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success(service.ping(body.get("url"))));
    }

    @PostMapping("/nat-ping")
    @Operation(summary = "启用/禁用 NAT Ping")
    public ResponseEntity<ApiResponse<Void>> enableNatPing(@RequestBody Map<String, Object> body) {
        int flag = body != null && body.get("flag") != null ? ((Number) body.get("flag")).intValue() : 0;
        service.enableNatPing(flag);
        return ResponseEntity.ok(ApiResponse.success(null, "操作成功"));
    }

    @GetMapping("/status")
    @Operation(summary = "获取状态")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus() {
        return ResponseEntity.ok(ApiResponse.success(service.getStatus()));
    }
}
