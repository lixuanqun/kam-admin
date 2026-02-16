package io.kamailio.admin.tenant;

import io.kamailio.admin.tenant.entity.Tenant;
import io.kamailio.admin.tenant.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动时若未存在任何租户，则插入默认租户（与 multi-tenant.default-tenant-code 一致）。
 */
@Component
@Order(100)
public class TenantInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(TenantInitializer.class);

    @Value("${multi-tenant.init-default-tenant:true}")
    private boolean initDefaultTenant;

    @Value("${multi-tenant.default-tenant-code:default}")
    private String defaultTenantCode;

    private final TenantRepository tenantRepository;

    public TenantInitializer(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!initDefaultTenant) return;
        try {
            if (tenantRepository.count() > 0) return;
            if (tenantRepository.existsByTenantCode(defaultTenantCode)) return;
            Tenant t = new Tenant();
            t.setTenantCode(defaultTenantCode);
            t.setName("默认租户");
            t.setStatus("ACTIVE");
            tenantRepository.save(t);
            log.info("多租户：已初始化默认租户 tenant_code={}", defaultTenantCode);
        } catch (Exception e) {
            log.debug("多租户：跳过默认租户初始化（可能 tenant 表尚未创建）: {}", e.getMessage());
        }
    }
}
