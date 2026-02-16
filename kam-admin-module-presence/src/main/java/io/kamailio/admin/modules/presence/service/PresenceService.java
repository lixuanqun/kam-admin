package io.kamailio.admin.modules.presence.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.presence.entity.ActiveWatchers;
import io.kamailio.admin.modules.presence.entity.Presentity;
import io.kamailio.admin.modules.presence.repository.ActiveWatchersRepository;
import io.kamailio.admin.modules.presence.repository.PresentityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PresenceService {
    private final PresentityRepository presentityRepository;
    private final ActiveWatchersRepository watchersRepository;
    private final KamailioRpcService kamailioRpc;

    public PresenceService(PresentityRepository presentityRepository, ActiveWatchersRepository watchersRepository, KamailioRpcService kamailioRpc) {
        this.presentityRepository = presentityRepository;
        this.watchersRepository = watchersRepository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Presentity> findAllPresentities(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? presentityRepository.findByUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")))
                : presentityRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public PaginatedResult<ActiveWatchers> findAllWatchers(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? watchersRepository.findByWatcherUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")))
                : watchersRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public void cleanExpired() { kamailioRpc.presenceCleanup().block(); }

    public void refreshWatchers(String presentityUri, String event) {
        kamailioRpc.presenceRefreshWatchers(presentityUri, event).block();
    }

    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("presentities", presentityRepository.count());
        result.put("watchers", watchersRepository.count());
        return result;
    }
}
