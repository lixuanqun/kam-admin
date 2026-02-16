package io.kamailio.admin.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthResolver implements AuthResolver {

    @Value("${open-api.keys:}")
    private String allowedKeysConfig;

    @Override
    public AuthMethod getMethod() {
        return AuthMethod.API_KEY;
    }

    @Override
    public AuthResult resolve(HttpServletRequest request) {
        String key = request.getHeader("X-API-Key");
        if (key == null || key.isBlank()) {
            return AuthResult.fail("Missing X-API-Key");
        }
        String config = allowedKeysConfig != null ? allowedKeysConfig.trim() : "";
        if (!config.isEmpty() && config.contains(key)) {
            String mask = key.length() > 8 ? key.substring(0, 8) + "..." : key;
            return AuthResult.ok("api-key:" + mask);
        }
        return AuthResult.fail("Invalid API Key");
    }
}
