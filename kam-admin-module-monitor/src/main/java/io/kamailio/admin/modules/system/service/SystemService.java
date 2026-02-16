package io.kamailio.admin.modules.system.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.service.KamailioRpcService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemService {
    private final KamailioRpcService kamailioRpc;

    public SystemService(KamailioRpcService kamailioRpc) { this.kamailioRpc = kamailioRpc; }

    public Object getCoreInfo() { return kamailioRpc.getCoreInfo().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getVersion() { return kamailioRpc.call("core.version").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getUptime() { return kamailioRpc.getUptime().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getSharedMemory() { return kamailioRpc.call("core.shmmem").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getProcessList() { return kamailioRpc.call("core.psx").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getConfig(String group, String name) { return kamailioRpc.call("cfg.get", group, name).block(RpcTimeouts.DEFAULT_BLOCK); }
    public void setConfigNow(String group, String name, Object value) {
        if (value instanceof Number) kamailioRpc.call("cfg.set_now_int", group, name, value).block(RpcTimeouts.DEFAULT_BLOCK);
        else kamailioRpc.call("cfg.set_now_string", group, name, value.toString()).block(RpcTimeouts.DEFAULT_BLOCK);
    }
    public Object listConfig() { return kamailioRpc.call("cfg.list").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object diffConfig() { return kamailioRpc.call("cfg.diff").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getTlsList() { return kamailioRpc.call("tls.list").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getTlsInfo() { return kamailioRpc.call("tls.info").block(RpcTimeouts.DEFAULT_BLOCK); }
    public void reloadTls() { kamailioRpc.call("tls.reload").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getPikeList() { return kamailioRpc.call("pike.list").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getPikeTop(int limit) { return kamailioRpc.call("pike.top", limit).block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getStatistics(String group) { return kamailioRpc.getStatistics(group).block(RpcTimeouts.DEFAULT_BLOCK); }
    public void resetStatistics(String name) { kamailioRpc.call("stats.reset_statistics", name).block(RpcTimeouts.DEFAULT_BLOCK); }
    public void clearStatistics(String name) { kamailioRpc.call("stats.clear_statistics", name).block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getModulesList() { return kamailioRpc.call("core.modules").block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getSystemStatus() {
        Object info = null, uptime = null, memory = null, processes = null;
        try { info = getCoreInfo(); } catch (Exception ignored) {}
        try { uptime = getUptime(); } catch (Exception ignored) {}
        try { memory = getSharedMemory(); } catch (Exception ignored) {}
        try { processes = getProcessList(); } catch (Exception ignored) {}
        int processCount = processes instanceof java.util.List ? ((java.util.List<?>) processes).size() : 0;
        return Map.of("info", info, "uptime", uptime, "memory", memory, "processCount", processCount);
    }
}
