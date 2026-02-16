package io.kamailio.admin.auth;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionAuthResolver implements AuthResolver {

    private static final String SESSION_USER = "auth.user";

    @Override
    public AuthMethod getMethod() {
        return AuthMethod.SESSION;
    }

    @Override
    public AuthResult resolve(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return AuthResult.fail("No session");
        }
        Object user = session.getAttribute(SESSION_USER);
        if (user == null) {
            return AuthResult.fail("Not logged in");
        }
        return AuthResult.ok(String.valueOf(user));
    }

    public static void setSessionUser(HttpSession session, String userId) {
        if (session != null) {
            session.setAttribute(SESSION_USER, userId);
        }
    }
}
