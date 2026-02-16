package io.kamailio.admin.tenant;

import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.rpc.KamailioRpcServiceImpl;
import io.kamailio.admin.tenant.entity.Tenant;
import io.kamailio.admin.tenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按当前租户路由到对应 Kamailio RPC：若租户配置了 rpc_url 则使用该地址，否则使用默认。
 */
@Service
@Primary
public class TenantAwareKamailioRpcService implements KamailioRpcService {

    private final KamailioRpcService defaultRpc;
    private final TenantRepository tenantRepository;
    private final Map<String, KamailioRpcService> rpcByUrl = new ConcurrentHashMap<>();

    public TenantAwareKamailioRpcService(
            @Qualifier("kamailioRpcServiceImpl") KamailioRpcService defaultRpc,
            TenantRepository tenantRepository) {
        this.defaultRpc = defaultRpc;
        this.tenantRepository = tenantRepository;
    }

    private KamailioRpcService delegate() {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) return defaultRpc;
        Optional<Tenant> t = tenantRepository.findById(tenantId);
        if (t.isEmpty()) return defaultRpc;
        String rpcUrl = t.get().getRpcUrl();
        if (rpcUrl == null || rpcUrl.isBlank()) return defaultRpc;
        return rpcByUrl.computeIfAbsent(rpcUrl, KamailioRpcServiceImpl::new);
    }

    @Override
    public <T> Mono<T> call(String method, Object... params) {
        return delegate().call(method, params);
    }

    @Override
    public Mono<Object> getStatistics(String group) { return delegate().getStatistics(group); }
    @Override
    public Mono<Object> getCoreInfo() { return delegate().getCoreInfo(); }
    @Override
    public Mono<Object> getUptime() { return delegate().getUptime(); }
    @Override
    public Mono<Object> reloadDispatcher() { return delegate().reloadDispatcher(); }
    @Override
    public Mono<Object> getDispatcherList() { return delegate().getDispatcherList(); }
    @Override
    public Mono<Object> getUserLocation(String table) { return delegate().getUserLocation(table); }
    @Override
    public Mono<Object> deleteUserLocation(String table, String aor) { return delegate().deleteUserLocation(table, aor); }
    @Override
    public Mono<Object> lookupUser(String table, String aor) { return delegate().lookupUser(table, aor); }
    @Override
    public Mono<Object> removeContact(String table, String aor, String contact) { return delegate().removeContact(table, aor, contact); }
    @Override
    public Mono<Object> flushUsrloc() { return delegate().flushUsrloc(); }
    @Override
    public Mono<Object> reloadPermissions() { return delegate().reloadPermissions(); }
    @Override
    public Mono<Object> reloadDomain() { return delegate().reloadDomain(); }
    @Override
    public Mono<Object> getDialogCount() { return delegate().getDialogCount(); }
    @Override
    public Mono<Object> getDialogList() { return delegate().getDialogList(); }
    @Override
    public Mono<Object> endDialog(int hashEntry, int hashId) { return delegate().endDialog(hashEntry, hashId); }
    @Override
    public Mono<Object> getDialogDetail(int hashEntry, int hashId) { return delegate().getDialogDetail(hashEntry, hashId); }
    @Override
    public Mono<Object> bridgeDialog(String from, String to) { return delegate().bridgeDialog(from, to); }
    @Override
    public Mono<Object> droutingReload() { return delegate().droutingReload(); }
    @Override
    public Mono<Object> droutingGwStatus() { return delegate().droutingGwStatus(); }
    @Override
    public Mono<Object> droutingCarrierStatus() { return delegate().droutingCarrierStatus(); }
    @Override
    public Mono<Object> htableGet(String table, String key) { return delegate().htableGet(table, key); }
    @Override
    public Mono<Object> htableSets(String table, String key, String value) { return delegate().htableSets(table, key, value); }
    @Override
    public Mono<Object> htableSeti(String table, String key, int value) { return delegate().htableSeti(table, key, value); }
    @Override
    public Mono<Object> htableDelete(String table, String key) { return delegate().htableDelete(table, key); }
    @Override
    public Mono<Object> htableDump(String table) { return delegate().htableDump(table); }
    @Override
    public Mono<Object> htableReload(String table) { return delegate().htableReload(table); }
    @Override
    public Mono<Object> htableListTables() { return delegate().htableListTables(); }
    @Override
    public Mono<Object> htableStats() { return delegate().htableStats(); }
    @Override
    public Mono<Object> lcrReload() { return delegate().lcrReload(); }
    @Override
    public Mono<Object> lcrDumpGws() { return delegate().lcrDumpGws(); }
    @Override
    public Mono<Object> lcrDumpRules() { return delegate().lcrDumpRules(); }
    @Override
    public Mono<Object> crReloadRoutes() { return delegate().crReloadRoutes(); }
    @Override
    public Mono<Object> crDumpRoutes() { return delegate().crDumpRoutes(); }
    @Override
    public Mono<Object> dialplanReload() { return delegate().dialplanReload(); }
    @Override
    public Mono<Object> dialplanTranslate(int dpid, String input) { return delegate().dialplanTranslate(dpid, input); }
    @Override
    public Mono<Object> dialplanDump(Integer dpid) { return delegate().dialplanDump(dpid); }
    @Override
    public Mono<Object> mtreeReload(String tname) { return delegate().mtreeReload(tname); }
    @Override
    public Mono<Object> mtreeMatch(String tname, String prefix) { return delegate().mtreeMatch(tname, prefix); }
    @Override
    public Mono<Object> mtreeSummary(String tname) { return delegate().mtreeSummary(tname); }
    @Override
    public Mono<Object> pdtReload() { return delegate().pdtReload(); }
    @Override
    public Mono<Object> pdtList() { return delegate().pdtList(); }
    @Override
    public Mono<Object> uacRegReload() { return delegate().uacRegReload(); }
    @Override
    public Mono<Object> uacRegInfo(String lUuid) { return delegate().uacRegInfo(lUuid); }
    @Override
    public Mono<Object> uacRegRefresh(String lUuid) { return delegate().uacRegRefresh(lUuid); }
    @Override
    public Mono<Object> uacRegEnable(String lUuid, int flag) { return delegate().uacRegEnable(lUuid, flag); }
    @Override
    public Mono<Object> uacRegDump() { return delegate().uacRegDump(); }
    @Override
    public Mono<Object> presenceCleanup() { return delegate().presenceCleanup(); }
    @Override
    public Mono<Object> presenceRefreshWatchers(String presentityUri, String event) { return delegate().presenceRefreshWatchers(presentityUri, event); }
    @Override
    public Mono<Object> msiloDump() { return delegate().msiloDump(); }
    @Override
    public Mono<Object> secfilterReload() { return delegate().secfilterReload(); }
    @Override
    public Mono<Object> secfilterPrint() { return delegate().secfilterPrint(); }
    @Override
    public Mono<Object> secfilterStats() { return delegate().secfilterStats(); }
    @Override
    public Mono<Object> secfilterStatsReset() { return delegate().secfilterStatsReset(); }
    @Override
    public Mono<Object> secfilterAddBl(int type, String data) { return delegate().secfilterAddBl(type, data); }
    @Override
    public Mono<Object> secfilterAddWl(int type, String data) { return delegate().secfilterAddWl(type, data); }
    @Override
    public Mono<Object> rtpengineShow(String scope) { return delegate().rtpengineShow(scope); }
    @Override
    public Mono<Object> rtpengineReload() { return delegate().rtpengineReload(); }
    @Override
    public Mono<Object> rtpengineEnable(String url, int flag) { return delegate().rtpengineEnable(url, flag); }
    @Override
    public Mono<Object> rtpenginePing(String url) { return delegate().rtpenginePing(url); }
    @Override
    public Mono<Object> nathelperEnablePing(int flag) { return delegate().nathelperEnablePing(flag); }
    @Override
    public Mono<Boolean> ping() { return delegate().ping(); }
}
