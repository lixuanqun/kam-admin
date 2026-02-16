package io.kamailio.admin.modules.system.controller;

import io.kamailio.admin.common.dto.ApiResponse;
import io.kamailio.admin.modules.system.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统管理")
@RestController
@RequestMapping("/api/system")
public class SystemController {
    private final SystemService service;

    public SystemController(SystemService service) { this.service = service; }

    @GetMapping("/core-info") public ResponseEntity<ApiResponse<Object>> getCoreInfo() { return ResponseEntity.ok(ApiResponse.success(service.getCoreInfo())); }
    @GetMapping("/version") public ResponseEntity<ApiResponse<Object>> getVersion() { return ResponseEntity.ok(ApiResponse.success(service.getVersion())); }
    @GetMapping("/uptime") public ResponseEntity<ApiResponse<Object>> getUptime() { return ResponseEntity.ok(ApiResponse.success(service.getUptime())); }
    @GetMapping("/shmmem") public ResponseEntity<ApiResponse<Object>> getSharedMemory() { return ResponseEntity.ok(ApiResponse.success(service.getSharedMemory())); }
    @GetMapping("/psx") public ResponseEntity<ApiResponse<Object>> getProcessList() { return ResponseEntity.ok(ApiResponse.success(service.getProcessList())); }
    @GetMapping("/config") public ResponseEntity<ApiResponse<Object>> getConfig(@RequestParam String group, @RequestParam String name) { return ResponseEntity.ok(ApiResponse.success(service.getConfig(group, name))); }
    @PostMapping("/config") public ResponseEntity<ApiResponse<Void>> setConfigNow(@RequestBody MapBody body) { service.setConfigNow(body.getGroup(), body.getName(), body.getValue()); return ResponseEntity.ok(ApiResponse.success(null)); }
    @GetMapping("/config/list") public ResponseEntity<ApiResponse<Object>> listConfig() { return ResponseEntity.ok(ApiResponse.success(service.listConfig())); }
    @GetMapping("/config/diff") public ResponseEntity<ApiResponse<Object>> diffConfig() { return ResponseEntity.ok(ApiResponse.success(service.diffConfig())); }
    @GetMapping("/tls/list") public ResponseEntity<ApiResponse<Object>> getTlsList() { return ResponseEntity.ok(ApiResponse.success(service.getTlsList())); }
    @GetMapping("/tls/info") public ResponseEntity<ApiResponse<Object>> getTlsInfo() { return ResponseEntity.ok(ApiResponse.success(service.getTlsInfo())); }
    @PostMapping("/tls/reload") public ResponseEntity<ApiResponse<Void>> reloadTls() { service.reloadTls(); return ResponseEntity.ok(ApiResponse.success(null)); }
    @GetMapping("/pike/list") public ResponseEntity<ApiResponse<Object>> getPikeList() { return ResponseEntity.ok(ApiResponse.success(service.getPikeList())); }
    @GetMapping("/pike/top") public ResponseEntity<ApiResponse<Object>> getPikeTop(@RequestParam(defaultValue = "10") int limit) { return ResponseEntity.ok(ApiResponse.success(service.getPikeTop(limit))); }
    @GetMapping("/statistics") public ResponseEntity<ApiResponse<Object>> getStatistics(@RequestParam(required = false) String group) { return ResponseEntity.ok(ApiResponse.success(service.getStatistics(group))); }
    @PostMapping("/statistics/reset") public ResponseEntity<ApiResponse<Void>> resetStatistics(@RequestParam String name) { service.resetStatistics(name); return ResponseEntity.ok(ApiResponse.success(null)); }
    @PostMapping("/statistics/clear") public ResponseEntity<ApiResponse<Void>> clearStatistics(@RequestParam String name) { service.clearStatistics(name); return ResponseEntity.ok(ApiResponse.success(null)); }
    @GetMapping("/modules") public ResponseEntity<ApiResponse<Object>> getModulesList() { return ResponseEntity.ok(ApiResponse.success(service.getModulesList())); }
    @GetMapping("/status") public ResponseEntity<ApiResponse<Object>> getSystemStatus() { return ResponseEntity.ok(ApiResponse.success(service.getSystemStatus())); }

    public static class MapBody {
        private String group, name;
        private Object value;
        public String getGroup() { return group; }
        public void setGroup(String group) { this.group = group; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
    }
}
