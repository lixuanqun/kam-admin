package io.kamailio.admin.auth;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * OAuth2 鉴权：校验 access_token（占位，可接 OAuth2 授权服务或 introspection 端点）。
 */
@Component
public class OAuth2AuthResolver implements AuthResolver {

    @Override
    public AuthMethod getMethod() {
        return AuthMethod.OAUTH2;
    }

    @Override
    public AuthResult resolve(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return AuthResult.fail("Missing or invalid Authorization header");
        }
        String token = auth.substring(7).trim();
        if (token.isEmpty()) {
            return AuthResult.fail("Empty token");
        }
        // TODO: 调用 OAuth2 introspection 或自验签
        return AuthResult.fail("OAuth2 validation not implemented");
    }
}
