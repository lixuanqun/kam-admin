package io.kamailio.admin.tenant;

/**
 * 当前请求的租户上下文（ThreadLocal），在 TenantFilter 中设置、请求结束时清理。
 */
public final class TenantContext {

    private static final ThreadLocal<String> TENANT_CODE = new ThreadLocal<>();
    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    public static void setTenant(String tenantCode, Long tenantId) {
        TENANT_CODE.set(tenantCode);
        TENANT_ID.set(tenantId);
    }

    public static void setTenantCode(String tenantCode) {
        TENANT_CODE.set(tenantCode);
    }

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static String getTenantCode() {
        return TENANT_CODE.get();
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void clear() {
        TENANT_CODE.remove();
        TENANT_ID.remove();
    }
}
