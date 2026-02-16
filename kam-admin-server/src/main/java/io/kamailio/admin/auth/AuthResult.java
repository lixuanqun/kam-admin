package io.kamailio.admin.auth;

import java.util.Optional;

/**
 * 鉴权结果：成功带主体标识，失败带原因。
 */
public record AuthResult(
        boolean success,
        String principal,
        String reason
) {
    public static AuthResult ok(String principal) {
        return new AuthResult(true, principal, null);
    }

    public static AuthResult fail(String reason) {
        return new AuthResult(false, null, reason);
    }

    public Optional<String> getPrincipal() {
        return Optional.ofNullable(principal);
    }
}
