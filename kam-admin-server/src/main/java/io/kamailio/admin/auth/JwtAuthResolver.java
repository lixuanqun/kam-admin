package io.kamailio.admin.auth;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthResolver implements AuthResolver {

    @Override
    public AuthMethod getMethod() {
        return AuthMethod.JWT;
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
        return AuthResult.fail("JWT validation not implemented");
    }
}
