package io.kamailio.admin.tenant;

import io.kamailio.admin.common.exception.BusinessException;
import io.kamailio.admin.tenant.entity.Tenant;
import io.kamailio.admin.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> list() {
        return tenantRepository.findAll();
    }

    public Optional<Tenant> findByCode(String tenantCode) {
        return tenantRepository.findByTenantCode(tenantCode);
    }

    public Optional<Tenant> findById(Long id) {
        return tenantRepository.findById(id);
    }

    @Transactional
    public Tenant create(String tenantCode, String name) {
        if (tenantRepository.existsByTenantCode(tenantCode)) {
            throw new BusinessException("租户编码已存在: " + tenantCode);
        }
        Tenant t = new Tenant();
        t.setTenantCode(tenantCode);
        t.setName(name);
        t.setStatus("ACTIVE");
        return tenantRepository.save(t);
    }

    @Transactional
    public Tenant update(Long id, String name, String status, String rpcUrl, String dbUrl) {
        Tenant t = tenantRepository.findById(id)
                .orElseThrow(() -> new BusinessException("租户不存在: " + id));
        if (name != null) t.setName(name);
        if (status != null) t.setStatus(status);
        if (rpcUrl != null) t.setRpcUrl(rpcUrl);
        if (dbUrl != null) t.setDbUrl(dbUrl);
        return tenantRepository.save(t);
    }

    @Transactional
    public void delete(Long id) {
        if (!tenantRepository.existsById(id)) {
            throw new BusinessException("租户不存在: " + id);
        }
        tenantRepository.deleteById(id);
    }
}
