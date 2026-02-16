package io.kamailio.admin.common.service;

import reactor.core.publisher.Mono;

/**
 * Kamailio JSON-RPC 调用接口。仅需实现 {@link #call(String, Object...)} 与 {@link #ping()}，
 * 其余为 default 方法，基于 call() 封装，便于扩展且实现类无需重复委托。
 */
public interface KamailioRpcService {

    /**
     * 通用 JSON-RPC 调用。
     *
     * @param method Kamailio RPC 方法名，如 "dispatcher.reload"
     * @param params 参数列表，可为空
     * @return 结果 Mono，失败时 onError
     */
    <T> Mono<T> call(String method, Object... params);

    /**
     * 探测 Kamailio 是否可达（如 core.version）。
     */
    Mono<Boolean> ping();

    // ---------- 以下为 default 方法，基于 call() 封装 ----------

    default Mono<Object> getStatistics(String group) {
        Object[] params = group != null ? new Object[]{group} : new Object[]{"all"};
        return call("stats.get_statistics", (Object[]) params);
    }

    default Mono<Object> getCoreInfo() { return call("core.info"); }
    default Mono<Object> getUptime() { return call("core.uptime"); }
    default Mono<Object> reloadDispatcher() { return call("dispatcher.reload"); }
    default Mono<Object> getDispatcherList() { return call("dispatcher.list"); }
    default Mono<Object> getUserLocation(String table) {
        return call("ul.dump", table != null ? new Object[]{table} : new Object[]{});
    }
    default Mono<Object> deleteUserLocation(String table, String aor) { return call("ul.rm", table, aor); }
    default Mono<Object> lookupUser(String table, String aor) { return call("ul.lookup", table, aor); }
    default Mono<Object> removeContact(String table, String aor, String contact) {
        return call("ul.rm_contact", table, aor, contact);
    }
    default Mono<Object> flushUsrloc() { return call("ul.flush"); }
    default Mono<Object> reloadPermissions() { return call("permissions.addressReload"); }
    default Mono<Object> reloadDomain() { return call("domain.reload"); }
    default Mono<Object> getDialogCount() { return call("dlg.stats_active"); }
    default Mono<Object> getDialogList() { return call("dlg.list"); }
    default Mono<Object> endDialog(int hashEntry, int hashId) { return call("dlg.end_dlg", hashEntry, hashId); }
    default Mono<Object> getDialogDetail(int hashEntry, int hashId) { return call("dlg.dlg_list", hashEntry, hashId); }
    default Mono<Object> bridgeDialog(String from, String to) { return call("dlg.bridge_dlg", from, to); }
    default Mono<Object> droutingReload() { return call("drouting.reload"); }
    default Mono<Object> droutingGwStatus() { return call("drouting.gw_status"); }
    default Mono<Object> droutingCarrierStatus() { return call("drouting.carrier_status"); }
    default Mono<Object> htableGet(String table, String key) { return call("htable.get", table, key); }
    default Mono<Object> htableSets(String table, String key, String value) { return call("htable.sets", table, key, value); }
    default Mono<Object> htableSeti(String table, String key, int value) { return call("htable.seti", table, key, value); }
    default Mono<Object> htableDelete(String table, String key) { return call("htable.delete", table, key); }
    default Mono<Object> htableDump(String table) { return call("htable.dump", table); }
    default Mono<Object> htableReload(String table) { return call("htable.reload", table); }
    default Mono<Object> htableListTables() { return call("htable.listTables"); }
    default Mono<Object> htableStats() { return call("htable.stats"); }
    default Mono<Object> lcrReload() { return call("lcr.reload"); }
    default Mono<Object> lcrDumpGws() { return call("lcr.dump_gws"); }
    default Mono<Object> lcrDumpRules() { return call("lcr.dump_rules"); }
    default Mono<Object> crReloadRoutes() { return call("cr.reload_routes"); }
    default Mono<Object> crDumpRoutes() { return call("cr.dump_routes"); }
    default Mono<Object> dialplanReload() { return call("dialplan.reload"); }
    default Mono<Object> dialplanTranslate(int dpid, String input) { return call("dialplan.translate", dpid, input); }
    default Mono<Object> dialplanDump(Integer dpid) {
        return dpid != null ? call("dialplan.dump", dpid) : call("dialplan.dump");
    }
    default Mono<Object> mtreeReload(String tname) { return call("mtree.reload", tname); }
    default Mono<Object> mtreeMatch(String tname, String prefix) { return call("mtree.match", tname, prefix); }
    default Mono<Object> mtreeSummary(String tname) {
        return tname != null ? call("mtree.summary", tname) : call("mtree.summary");
    }
    default Mono<Object> pdtReload() { return call("pdt.reload"); }
    default Mono<Object> pdtList() { return call("pdt.list"); }
    default Mono<Object> uacRegReload() { return call("uac.reg_reload"); }
    default Mono<Object> uacRegInfo(String lUuid) { return call("uac.reg_info", lUuid); }
    default Mono<Object> uacRegRefresh(String lUuid) { return call("uac.reg_refresh", lUuid); }
    default Mono<Object> uacRegEnable(String lUuid, int flag) { return call("uac.reg_enable", lUuid, flag); }
    default Mono<Object> uacRegDump() { return call("uac.reg_dump"); }
    default Mono<Object> presenceCleanup() { return call("presence.cleanup"); }
    default Mono<Object> presenceRefreshWatchers(String presentityUri, String event) {
        return call("presence.refreshWatchers", presentityUri, event, 0);
    }
    default Mono<Object> msiloDump() { return call("msilo.dump"); }
    default Mono<Object> secfilterReload() { return call("secfilter.reload"); }
    default Mono<Object> secfilterPrint() { return call("secfilter.print"); }
    default Mono<Object> secfilterStats() { return call("secfilter.stats"); }
    default Mono<Object> secfilterStatsReset() { return call("secfilter.stats_reset"); }
    default Mono<Object> secfilterAddBl(int type, String data) { return call("secfilter.add_bl", type, data); }
    default Mono<Object> secfilterAddWl(int type, String data) { return call("secfilter.add_wl", type, data); }
    default Mono<Object> rtpengineShow(String scope) {
        return call("rtpengine.show", scope != null ? scope : "all");
    }
    default Mono<Object> rtpengineReload() { return call("rtpengine.reload"); }
    default Mono<Object> rtpengineEnable(String url, int flag) { return call("rtpengine.enable", url, flag); }
    default Mono<Object> rtpenginePing(String url) { return call("rtpengine.ping", url); }
    default Mono<Object> nathelperEnablePing(int flag) { return call("nathelper.enable_ping", flag); }
}
