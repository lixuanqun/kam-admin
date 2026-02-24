package io.kamailio.admin.modules.monitoring.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.acc.service.AccService;
import io.kamailio.admin.modules.dispatcher.service.DispatcherService;
import io.kamailio.admin.modules.location.service.LocationService;
import io.kamailio.admin.modules.subscriber.service.SubscriberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MonitoringService {
    private final KamailioRpcService kamailioRpc;
    private final SubscriberService subscriberService;
    private final LocationService locationService;
    private final AccService accService;
    private final DispatcherService dispatcherService;

    public MonitoringService(KamailioRpcService kamailioRpc, SubscriberService subscriberService,
                            LocationService locationService, AccService accService,
                            DispatcherService dispatcherService) {
        this.kamailioRpc = kamailioRpc;
        this.subscriberService = subscriberService;
        this.locationService = locationService;
        this.accService = accService;
        this.dispatcherService = dispatcherService;
    }

    public Map<String, Object> checkHealth() {
        if (Boolean.FALSE.equals(kamailioRpc.ping().block(RpcTimeouts.DEFAULT_BLOCK)))
            return Map.of("status", "offline");
        try {
            Object uptime = kamailioRpc.getUptime().block(RpcTimeouts.DEFAULT_BLOCK);
            Object coreInfo = kamailioRpc.getCoreInfo().block(RpcTimeouts.DEFAULT_BLOCK);
            String version = coreInfo instanceof Map ? (String) ((Map<?, ?>) coreInfo).get("version") : null;
            return Map.of("status", "online", "uptime", uptime != null ? uptime : "", "version", version != null ? version : "");
        } catch (Exception e) {
            return Map.of("status", "offline");
        }
    }

    public Map<String, Object> getDashboardData() {
        var subscriberStats = subscriberService.getStats();
        long onlineCount = locationService.getOnlineCount();
        var todayStats = accService.getTodayStats();
        var health = checkHealth();
        return Map.of(
                "subscriberCount", subscriberStats.get("total"),
                "onlineCount", onlineCount,
                "todayCalls", todayStats.get("totalCalls"),
                "todayMissedCalls", todayStats.get("missedCalls"),
                "successRate", todayStats.get("successRate"),
                "kamailioStatus", health.get("status")
        );
    }

    public Object getStatistics(String group) {
        return kamailioRpc.getStatistics(group).block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Object getCoreInfo() {
        return kamailioRpc.getCoreInfo().block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Map<String, Object> getActiveDialogs() {
        try {
            Object count = kamailioRpc.getDialogCount().block(RpcTimeouts.DEFAULT_BLOCK);
            Object dialogs = kamailioRpc.getDialogList().block(RpcTimeouts.DEFAULT_BLOCK);
            int c = 0;
            if (count instanceof Map<?, ?> m) { Object v = m.get("active"); if (v instanceof Number n) c = n.intValue(); }
            return Map.of("count", c, "dialogs", dialogs != null ? dialogs : List.of());
        } catch (Exception e) {
            return Map.of("count", 0, "dialogs", List.of());
        }
    }

    public Map<String, Object> checkModules() {
        var mods = List.of("core", "usrloc", "dispatcher", "drouting", "domain", "permissions", "dialog", "cfg_rpc", "statistics", "tls", "pike");
        var rpcs = List.of("core.version", "ul.dump", "dispatcher.list", "drouting.gw_status", "domain.dump", "permissions.addressDump", "dlg.stats_active", "cfg.list", "stats.get_statistics", "tls.list", "pike.list");
        var results = new ArrayList<Map<String, Object>>();
        boolean connected = false;
        String version = null;
        for (int i = 0; i < mods.size(); i++) {
            try {
                Object r = kamailioRpc.call(rpcs.get(i), rpcs.get(i).equals("stats.get_statistics") ? new Object[]{"all"} : new Object[]{}).block(RpcTimeouts.DEFAULT_BLOCK);
                if (i == 0 && r != null) { connected = true; version = r.toString(); }
                results.add(Map.of("name", mods.get(i), "available", true));
            } catch (Exception e) {
                results.add(Map.of("name", mods.get(i), "available", false, "error", e.getMessage()));
            }
        }
        return Map.of("connected", connected, "version", version != null ? version : "", "modules", results);
    }

    public Map<String, Object> getSystemOverview() {
        var health = checkHealth();
        var subscriberStats = subscriberService.getStats();
        long onlineCount = locationService.getOnlineCount();
        long dispatcherCount = ((Number) dispatcherService.getStats().get("total")).longValue();
        int activeDialogs = 0;
        try {
            Object dc = kamailioRpc.getDialogCount().block(RpcTimeouts.DEFAULT_BLOCK);
            if (dc instanceof Map<?, ?> m) { Object v = m.get("active"); if (v instanceof Number n) activeDialogs = n.intValue(); }
        } catch (Exception ignored) {}
        return Map.of(
                "kamailio", Map.of("status", health.get("status"), "version", health.get("version"), "uptime", health.get("uptime")),
                "database", Map.of("subscribers", subscriberStats.get("total"), "domains", ((List<?>) subscriberStats.get("domains")).size(), "dispatchers", dispatcherCount),
                "realtime", Map.of("onlineUsers", onlineCount, "activeDialogs", activeDialogs)
        );
    }
}
