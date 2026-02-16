package io.kamailio.admin.modules.location.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.location.entity.Location;
import io.kamailio.admin.modules.location.repository.LocationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class LocationService {
    private final LocationRepository repository;
    private final KamailioRpcService kamailioRpc;

    public LocationService(LocationRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Location> findAll(PaginationDto dto) {
        var now = Instant.now();
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByExpiresAfterAndUsernameContaining(now, dto.getKeyword(),
                PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "lastModified")))
                : repository.findByExpiresAfter(now, PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "lastModified")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public long getOnlineCount() {
        return repository.countByExpiresAfter(Instant.now());
    }

    public Object getFromMemory(String table) {
        return kamailioRpc.getUserLocation(table).block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public void deleteUserLocation(String username, String domain) {
        kamailioRpc.deleteUserLocation("location", "sip:" + username + "@" + domain).block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Object lookupUser(String table, String aor) {
        return kamailioRpc.lookupUser(table != null ? table : "location", aor).block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public void deleteContact(String table, String aor, String contact) {
        kamailioRpc.removeContact(table != null ? table : "location", aor, contact).block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public void flushUsrloc() {
        kamailioRpc.flushUsrloc().block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public List<Location> findByUsername(String username) {
        return repository.findByUsernameAndExpiresAfterOrderByLastModifiedDesc(username, Instant.now());
    }

    public Map<String, Object> getStats() {
        var now = Instant.now();
        long total = repository.countByExpiresAfter(now);
        var byDomain = repository.countByDomain(now).stream()
                .map(r -> Map.<String, Object>of("domain", r[0], "count", r[1])).toList();
        var byUserAgent = repository.countByUserAgent(now).stream()
                .map(r -> Map.<String, Object>of("userAgent", r[0], "count", r[1])).toList();
        return Map.of("total", total, "byDomain", byDomain, "byUserAgent", byUserAgent);
    }
}
