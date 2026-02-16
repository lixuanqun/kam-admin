package io.kamailio.admin.tenant;

import io.kamailio.admin.tenant.entity.Tenant;
import io.kamailio.admin.tenant.repository.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 从 Header（X-Tenant-Id / X-Tenant-Code）、子域或默认配置解析当前租户。
 */
@Component
@Order(1)
public class TenantResolver {

    private static final Logger log = LoggerFactory.getLogger(TenantResolver.class);

    @Value("${multi-tenant.default-tenant-code:default}")
    private String defaultTenantCode;

    @Value("${multi-tenant.subdomain-enabled:false}")
    private boolean subdomainEnabled;

    @Value("${multi-tenant.subdomain-suffix:}")
    private String subdomainSuffix;

    private final TenantRepository tenantRepository;

    public TenantResolver(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    /**
     * 解析当前请求的租户并写入 TenantContext；若未识别则使用默认租户。
     * 顺序：X-Tenant-Code -> X-Tenant-Id -> 子域（若启用）-> 默认租户。
     */
    public void resolve(HttpServletRequest request) {
        String code = request.getHeader("X-Tenant-Code");
        if (code == null || code.isBlank()) {
            String idStr = request.getHeader("X-Tenant-Id");
            if (idStr != null && !idStr.isBlank()) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    tenantRepository.findById(id).ifPresent(t -> setContext(t.getTenantCode(), t.getId()));
                    return;
                } catch (NumberFormatException e) {
                    log.debug("Invalid X-Tenant-Id: {}", idStr);
                }
            }
            if (subdomainEnabled && subdomainSuffix != null && !subdomainSuffix.isBlank()) {
                code = resolveFromSubdomain(request.getServerName());
            }
            if (code == null || code.isBlank()) {
                code = defaultTenantCode;
            }
        } else {
            code = code.trim();
        }
        Optional<Tenant> tenant = tenantRepository.findByTenantCodeAndStatus(code, "ACTIVE");
        if (tenant.isPresent()) {
            setContext(tenant.get().getTenantCode(), tenant.get().getId());
        } else {
            setContext(code, null);
        }
    }

    /** 从 Host 提取子域：如 tenant-a.dashboard.example.com + suffix .dashboard.example.com -> tenant-a */
    private String resolveFromSubdomain(String host) {
        if (host == null || host.isBlank()) return null;
        if (!host.endsWith(subdomainSuffix)) return null;
        String prefix = host.substring(0, host.length() - subdomainSuffix.length()).trim();
        if (prefix.isEmpty()) return null;
        int lastDot = prefix.lastIndexOf('.');
        return lastDot >= 0 ? prefix.substring(lastDot + 1) : prefix;
    }

    private void setContext(String tenantCode, Long tenantId) {
        TenantContext.setTenant(tenantCode, tenantId);
    }
}
