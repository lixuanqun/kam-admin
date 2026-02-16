package io.kamailio.admin.modules.rtpengine.service;

import io.kamailio.admin.common.service.KamailioRpcService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RtpengineService {
    private final KamailioRpcService kamailioRpc;

    public RtpengineService(KamailioRpcService kamailioRpc) {
        this.kamailioRpc = kamailioRpc;
    }

    public Object showAll() {
        return kamailioRpc.rtpengineShow("all").block();
    }

    public void reload() {
        kamailioRpc.rtpengineReload().block();
    }

    public void enable(String url, int flag) {
        kamailioRpc.rtpengineEnable(url, flag).block();
    }

    public Object ping(String url) {
        return kamailioRpc.rtpenginePing(url).block();
    }

    public void enableNatPing(int flag) {
        kamailioRpc.nathelperEnablePing(flag).block();
    }

    public Map<String, Object> getStatus() {
        try {
            Object rtpengine = showAll();
            Map<String, Object> result = new HashMap<>();
            result.put("rtpengine", rtpengine);
            result.put("status", "ok");
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            return result;
        }
    }
}
