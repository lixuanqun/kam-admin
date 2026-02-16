package io.kamailio.admin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kamailio.admin.common.dto.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 统一鉴权过滤器：按路径选择鉴权方式（/open/** → API_KEY，/api/** → SESSION 等），
 * 将鉴权结果写入 request 属性 AUTH_PRINCIPAL / AUTH_METHOD。
 */
public class AuthFilter extends OncePerRequestFilter {

    public static final String AUTH_PRINCIPAL = "AUTH_PRINCIPAL";
    public static final String AUTH_METHOD = "AUTH_METHOD";

    private final List<AuthResolver> resolvers;
    private final ObjectMapper objectMapper;
    private final String openApiAuthMethod;
    private final String consoleAuthMethod;

    public AuthFilter(List<AuthResolver> resolvers, ObjectMapper objectMapper,
                      String openApiAuthMethod, String consoleAuthMethod) {
        this.resolvers = resolvers;
        this.objectMapper = objectMapper;
        this.openApiAuthMethod = openApiAuthMethod != null ? openApiAuthMethod : "API_KEY";
        this.consoleAuthMethod = consoleAuthMethod != null ? consoleAuthMethod : "SESSION";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        AuthMethod method = path.startsWith("/open/") ? AuthMethod.valueOf(openApiAuthMethod)
                : path.startsWith("/api/") ? AuthMethod.valueOf(consoleAuthMethod)
                : AuthMethod.NONE;

        if (method == AuthMethod.NONE) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthResolver resolver = resolvers.stream()
                .filter(r -> r.getMethod() == method)
                .findFirst()
                .orElse(null);

        if (resolver == null) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthResult result = resolver.resolve(request);
        request.setAttribute(AUTH_METHOD, method.name());
        if (result.success()) {
            request.setAttribute(AUTH_PRINCIPAL, result.principal());
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(
                    ApiResponse.error(result.reason() != null ? result.reason() : "Unauthorized", 401)));
        }
    }
}
