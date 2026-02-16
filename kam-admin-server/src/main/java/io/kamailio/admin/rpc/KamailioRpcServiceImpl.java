package io.kamailio.admin.rpc;

import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.config.KamailioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KamailioRpcServiceImpl implements KamailioRpcService {

    private static final Logger log = LoggerFactory.getLogger(KamailioRpcServiceImpl.class);

    private final WebClient webClient;
    private final AtomicInteger requestId = new AtomicInteger(0);

    public KamailioRpcServiceImpl(KamailioProperties properties) {
        var rpc = properties.getRpc();
        String baseUrl = "http://" + rpc.getHost() + ":" + rpc.getPort() + rpc.getPath();
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /** 用于多租户：按租户 RPC 地址创建实例（同一 baseUrl 可复用，由调用方缓存）。 */
    public KamailioRpcServiceImpl(String baseUrl) {
        String url = baseUrl != null && !baseUrl.isBlank() ? baseUrl.trim() : "http://localhost:5060/RPC";
        if (!url.startsWith("http")) url = "http://" + url;
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> call(String method, Object... params) {
        int id = requestId.incrementAndGet();
        Map<String, Object> payload = Map.of(
                "jsonrpc", "2.0",
                "method", method,
                "params", params != null ? params : new Object[0],
                "id", id
        );

        return webClient.post()
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(resp -> {
                    if (resp.containsKey("error")) {
                        var err = (Map<?, ?>) resp.get("error");
                        String msg = String.valueOf(err.get("message"));
                        log.error("RPC call failed: {} - {}", method, msg);
                        return Mono.error(new RuntimeException("RPC Error: " + msg));
                    }
                    return Mono.just((T) resp.get("result"));
                })
                .onErrorResume(e -> {
                    log.error("RPC call failed: {}", method, e);
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<Object> getStatistics(String group) {
        Object[] params = group != null ? new Object[]{group} : new Object[]{"all"};
        return call("stats.get_statistics", params);
    }

    @Override
    public Mono<Object> getCoreInfo() {
        return call("core.info");
    }

    @Override
    public Mono<Object> getUptime() {
        return call("core.uptime");
    }

    @Override
    public Mono<Object> reloadDispatcher() {
        return call("dispatcher.reload");
    }

    @Override
    public Mono<Object> getDispatcherList() {
        return call("dispatcher.list");
    }

    @Override
    public Mono<Object> getUserLocation(String table) {
        Object[] params = table != null ? new Object[]{table} : new Object[]{};
        return call("ul.dump", params);
    }

    @Override
    public Mono<Object> deleteUserLocation(String table, String aor) {
        return call("ul.rm", table, aor);
    }

    @Override
    public Mono<Object> lookupUser(String table, String aor) {
        return call("ul.lookup", table, aor);
    }

    @Override
    public Mono<Object> removeContact(String table, String aor, String contact) {
        return call("ul.rm_contact", table, aor, contact);
    }

    @Override
    public Mono<Object> flushUsrloc() {
        return call("ul.flush");
    }

    @Override
    public Mono<Object> reloadPermissions() {
        return call("permissions.addressReload");
    }

    @Override
    public Mono<Object> reloadDomain() {
        return call("domain.reload");
    }

    @Override
    public Mono<Object> getDialogCount() {
        return call("dlg.stats_active");
    }

    @Override
    public Mono<Object> getDialogList() {
        return call("dlg.list");
    }

    @Override
    public Mono<Object> endDialog(int hashEntry, int hashId) {
        return call("dlg.end_dlg", hashEntry, hashId);
    }

    @Override
    public Mono<Object> getDialogDetail(int hashEntry, int hashId) {
        return call("dlg.dlg_list", hashEntry, hashId);
    }

    @Override
    public Mono<Object> bridgeDialog(String from, String to) {
        return call("dlg.bridge_dlg", from, to);
    }

    @Override
    public Mono<Object> droutingReload() {
        return call("drouting.reload");
    }

    @Override
    public Mono<Object> droutingGwStatus() {
        return call("drouting.gw_status");
    }

    @Override
    public Mono<Object> droutingCarrierStatus() {
        return call("drouting.carrier_status");
    }

    @Override
    public Mono<Object> htableGet(String table, String key) {
        return call("htable.get", table, key);
    }

    @Override
    public Mono<Object> htableSets(String table, String key, String value) {
        return call("htable.sets", table, key, value);
    }

    @Override
    public Mono<Object> htableSeti(String table, String key, int value) {
        return call("htable.seti", table, key, value);
    }

    @Override
    public Mono<Object> htableDelete(String table, String key) {
        return call("htable.delete", table, key);
    }

    @Override
    public Mono<Object> htableDump(String table) {
        return call("htable.dump", table);
    }

    @Override
    public Mono<Object> htableReload(String table) {
        return call("htable.reload", table);
    }

    @Override
    public Mono<Object> htableListTables() {
        return call("htable.listTables");
    }

    @Override
    public Mono<Object> htableStats() {
        return call("htable.stats");
    }

    @Override
    public Mono<Object> lcrReload() {
        return call("lcr.reload");
    }

    @Override
    public Mono<Object> lcrDumpGws() {
        return call("lcr.dump_gws");
    }

    @Override
    public Mono<Object> lcrDumpRules() {
        return call("lcr.dump_rules");
    }

    @Override
    public Mono<Object> crReloadRoutes() {
        return call("cr.reload_routes");
    }

    @Override
    public Mono<Object> crDumpRoutes() {
        return call("cr.dump_routes");
    }

    @Override
    public Mono<Object> dialplanReload() {
        return call("dialplan.reload");
    }

    @Override
    public Mono<Object> dialplanTranslate(int dpid, String input) {
        return call("dialplan.translate", dpid, input);
    }

    @Override
    public Mono<Object> dialplanDump(Integer dpid) {
        return dpid != null ? call("dialplan.dump", dpid) : call("dialplan.dump");
    }

    @Override
    public Mono<Object> mtreeReload(String tname) {
        return call("mtree.reload", tname);
    }

    @Override
    public Mono<Object> mtreeMatch(String tname, String prefix) {
        return call("mtree.match", tname, prefix);
    }

    @Override
    public Mono<Object> mtreeSummary(String tname) {
        return tname != null ? call("mtree.summary", tname) : call("mtree.summary");
    }

    @Override
    public Mono<Object> pdtReload() {
        return call("pdt.reload");
    }

    @Override
    public Mono<Object> pdtList() {
        return call("pdt.list");
    }

    @Override
    public Mono<Object> uacRegReload() {
        return call("uac.reg_reload");
    }

    @Override
    public Mono<Object> uacRegInfo(String lUuid) {
        return call("uac.reg_info", lUuid);
    }

    @Override
    public Mono<Object> uacRegRefresh(String lUuid) {
        return call("uac.reg_refresh", lUuid);
    }

    @Override
    public Mono<Object> uacRegEnable(String lUuid, int flag) {
        return call("uac.reg_enable", lUuid, flag);
    }

    @Override
    public Mono<Object> uacRegDump() {
        return call("uac.reg_dump");
    }

    @Override
    public Mono<Object> presenceCleanup() {
        return call("presence.cleanup");
    }

    @Override
    public Mono<Object> presenceRefreshWatchers(String presentityUri, String event) {
        return call("presence.refreshWatchers", presentityUri, event, 0);
    }

    @Override
    public Mono<Object> msiloDump() {
        return call("msilo.dump");
    }

    @Override
    public Mono<Object> secfilterReload() {
        return call("secfilter.reload");
    }

    @Override
    public Mono<Object> secfilterPrint() {
        return call("secfilter.print");
    }

    @Override
    public Mono<Object> secfilterStats() {
        return call("secfilter.stats");
    }

    @Override
    public Mono<Object> secfilterStatsReset() {
        return call("secfilter.stats_reset");
    }

    @Override
    public Mono<Object> secfilterAddBl(int type, String data) {
        return call("secfilter.add_bl", type, data);
    }

    @Override
    public Mono<Object> secfilterAddWl(int type, String data) {
        return call("secfilter.add_wl", type, data);
    }

    @Override
    public Mono<Object> rtpengineShow(String scope) {
        return call("rtpengine.show", scope != null ? scope : "all");
    }

    @Override
    public Mono<Object> rtpengineReload() {
        return call("rtpengine.reload");
    }

    @Override
    public Mono<Object> rtpengineEnable(String url, int flag) {
        return call("rtpengine.enable", url, flag);
    }

    @Override
    public Mono<Object> rtpenginePing(String url) {
        return call("rtpengine.ping", url);
    }

    @Override
    public Mono<Object> nathelperEnablePing(int flag) {
        return call("nathelper.enable_ping", flag);
    }

    @Override
    public Mono<Boolean> ping() {
        return call("core.version")
                .thenReturn(true)
                .onErrorReturn(false);
    }
}
