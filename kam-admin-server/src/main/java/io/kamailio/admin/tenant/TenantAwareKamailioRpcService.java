package io.kamailio.admin.tenant;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.config.KamailioProperties;
import io.kamailio.admin.rpc.KamailioRpcServiceImpl;
import io.kamailio.admin.tenant.entity.Tenant;
import io.kamailio.admin.tenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 按当前租户路由到对应 Kamailio RPC：若租户配置了 rpc_url 则使用该地址，否则使用默认。
 * 租户 RPC 客户端使用有界、带 TTL 的缓存，避免无界增长。
 */
@Service
@Primary
public class TenantAwareKamailioRpcService implements KamailioRpcService {

    private final KamailioRpcService defaultRpc;
    private final TenantRepository tenantRepository;
    private final Cache<String, KamailioRpcService> rpcByUrl;
    private final int rpcTimeoutMs;

    public TenantAwareKamailioRpcService(
            @Qualifier("kamailioRpcServiceImpl") KamailioRpcService defaultRpc,
            TenantRepository tenantRepository,
            KamailioProperties kamailioProperties,
            @Value("${kam-admin.tenant-rpc-cache.max-size:256}") int cacheMaxSize,
            @Value("${kam-admin.tenant-rpc-cache.expire-minutes:30}") int cacheExpireMinutes) {
        this.defaultRpc = defaultRpc;
        this.tenantRepository = tenantRepository;
        this.rpcTimeoutMs = Math.max(1000, kamailioProperties.getRpc().getTimeout());
        this.rpcByUrl = Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterAccess(cacheExpireMinutes, TimeUnit.MINUTES)
                .build();
    }

    private KamailioRpcService delegate() {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) return defaultRpc;
        Optional<Tenant> t = tenantRepository.findById(tenantId);
        if (t.isEmpty()) return defaultRpc;
        String rpcUrl = t.get().getRpcUrl();
        if (rpcUrl == null || rpcUrl.isBlank()) return defaultRpc;
        return rpcByUrl.get(rpcUrl, url -> new KamailioRpcServiceImpl(url, rpcTimeoutMs));
    }

    @Override
    public <T> Mono<T> call(String method, Object... params) {
        return delegate().call(method, params);
    }

    @Override
    public Mono<Boolean> ping() {
        return delegate().ping();
    }
}
