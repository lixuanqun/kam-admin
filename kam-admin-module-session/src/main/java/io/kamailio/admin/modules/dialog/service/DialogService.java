package io.kamailio.admin.modules.dialog.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.dialog.entity.Dialog;
import io.kamailio.admin.modules.dialog.repository.DialogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DialogService {
    private final DialogRepository repository;
    private final KamailioRpcService kamailioRpc;

    public DialogService(DialogRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Dialog> findAll(PaginationDto dto) {
        var page = repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "startTime")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public Object getActiveDialogs() {
        try {
            return kamailioRpc.getDialogList().block();
        } catch (Exception e) {
            return new Object[0];
        }
    }

    public Object getDialogStats() {
        try {
            return kamailioRpc.getDialogCount().block();
        } catch (Exception e) {
            Map<String, Object> m = new HashMap<>();
            m.put("active", 0);
            return m;
        }
    }

    public void endDialog(int hashEntry, int hashId) {
        kamailioRpc.endDialog(hashEntry, hashId).block();
    }

    public Object getDialogDetail(int hashEntry, int hashId) {
        return kamailioRpc.getDialogDetail(hashEntry, hashId).block();
    }

    public void bridgeDialog(String from, String to) {
        kamailioRpc.bridgeDialog(from, to).block();
    }

    public Map<String, Object> getStats() {
        long dbCount = repository.count();
        Object memStats = getDialogStats();
        Map<String, Object> result = new HashMap<>();
        result.put("dbCount", dbCount);
        if (memStats instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) memStats;
            for (Map.Entry<?, ?> e : m.entrySet()) result.put(String.valueOf(e.getKey()), e.getValue());
        }
        return result;
    }
}
