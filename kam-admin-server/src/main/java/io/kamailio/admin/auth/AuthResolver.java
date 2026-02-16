package io.kamailio.admin.auth;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 鉴权解析器接口，每种鉴权方式一个实现。
 */
public interface AuthResolver {

    AuthMethod getMethod();

    AuthResult resolve(HttpServletRequest request);
}
