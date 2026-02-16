package io.kamailio.admin.common.service;

import reactor.core.publisher.Mono;

/**
 * Kamailio JSON-RPC 调用接口定义，实现类在 kam-admin-server。
 */
public interface KamailioRpcService {

    <T> Mono<T> call(String method, Object... params);

    Mono<Object> getStatistics(String group);
    Mono<Object> getCoreInfo();
    Mono<Object> getUptime();
    Mono<Object> reloadDispatcher();
    Mono<Object> getDispatcherList();
    Mono<Object> getUserLocation(String table);
    Mono<Object> deleteUserLocation(String table, String aor);
    Mono<Object> lookupUser(String table, String aor);
    Mono<Object> removeContact(String table, String aor, String contact);
    Mono<Object> flushUsrloc();
    Mono<Object> reloadPermissions();
    Mono<Object> reloadDomain();
    Mono<Object> getDialogCount();
    Mono<Object> getDialogList();
    Mono<Object> endDialog(int hashEntry, int hashId);
    Mono<Object> getDialogDetail(int hashEntry, int hashId);
    Mono<Object> bridgeDialog(String from, String to);
    Mono<Object> droutingReload();
    Mono<Object> droutingGwStatus();
    Mono<Object> droutingCarrierStatus();
    Mono<Object> htableGet(String table, String key);
    Mono<Object> htableSets(String table, String key, String value);
    Mono<Object> htableSeti(String table, String key, int value);
    Mono<Object> htableDelete(String table, String key);
    Mono<Object> htableDump(String table);
    Mono<Object> htableReload(String table);
    Mono<Object> htableListTables();
    Mono<Object> htableStats();
    Mono<Object> lcrReload();
    Mono<Object> lcrDumpGws();
    Mono<Object> lcrDumpRules();
    Mono<Object> crReloadRoutes();
    Mono<Object> crDumpRoutes();
    Mono<Object> dialplanReload();
    Mono<Object> dialplanTranslate(int dpid, String input);
    Mono<Object> dialplanDump(Integer dpid);
    Mono<Object> mtreeReload(String tname);
    Mono<Object> mtreeMatch(String tname, String prefix);
    Mono<Object> mtreeSummary(String tname);
    Mono<Object> pdtReload();
    Mono<Object> pdtList();
    Mono<Object> uacRegReload();
    Mono<Object> uacRegInfo(String lUuid);
    Mono<Object> uacRegRefresh(String lUuid);
    Mono<Object> uacRegEnable(String lUuid, int flag);
    Mono<Object> uacRegDump();
    Mono<Object> presenceCleanup();
    Mono<Object> presenceRefreshWatchers(String presentityUri, String event);
    Mono<Object> msiloDump();
    Mono<Object> secfilterReload();
    Mono<Object> secfilterPrint();
    Mono<Object> secfilterStats();
    Mono<Object> secfilterStatsReset();
    Mono<Object> secfilterAddBl(int type, String data);
    Mono<Object> secfilterAddWl(int type, String data);
    Mono<Object> rtpengineShow(String scope);
    Mono<Object> rtpengineReload();
    Mono<Object> rtpengineEnable(String url, int flag);
    Mono<Object> rtpenginePing(String url);
    Mono<Object> nathelperEnablePing(int flag);
    Mono<Boolean> ping();
}
