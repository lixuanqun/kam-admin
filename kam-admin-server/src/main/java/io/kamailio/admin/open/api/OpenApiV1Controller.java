package io.kamailio.admin.open.api;

import io.kamailio.admin.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 对外开放 API 示例（/open/v1）。
 * 认证：Header X-API-Key；限流：基于 Redis，见 docs/architecture-api-cluster-nacos-redis.md。
 */
@Tag(name = "开放 API (Open API v1)", description = "对外提供，需 API Key 认证与限流")
@RestController
@RequestMapping("/open/v1")
public class OpenApiV1Controller {

    @GetMapping("/health")
    @Operation(summary = "开放接口健康检查")
    public ResponseEntity<ApiResponse<Object>> health() {
        return ResponseEntity.ok(ApiResponse.success(Map.of("status", "UP", "api", "open/v1")));
    }
}
