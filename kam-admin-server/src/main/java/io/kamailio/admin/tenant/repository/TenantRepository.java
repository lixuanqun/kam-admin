package io.kamailio.admin.tenant.repository;

import io.kamailio.admin.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantCode(String tenantCode);

    Optional<Tenant> findByTenantCodeAndStatus(String tenantCode, String status);

    boolean existsByTenantCode(String tenantCode);
}
