package io.kamailio.admin.auth;

/**
 * 鉴权方式枚举，与路径或配置对应。
 */
public enum AuthMethod {
    /** 开放 API：Header X-API-Key */
    API_KEY,
    /** 控制台：Session/Cookie */
    SESSION,
    /** Bearer JWT */
    JWT,
    /** OAuth2 access_token */
    OAUTH2,
    /** 无需鉴权（如健康检查） */
    NONE
}
