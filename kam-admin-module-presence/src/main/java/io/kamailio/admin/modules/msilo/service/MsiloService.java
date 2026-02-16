package io.kamailio.admin.modules.msilo.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.msilo.entity.Silo;
import io.kamailio.admin.modules.msilo.repository.SiloRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsiloService {
    private final SiloRepository repository;
    private final KamailioRpcService kamailioRpc;

    public MsiloService(SiloRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Silo> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "incTime")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "incTime")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public List<Silo> findByUser(String username, String domain) {
        return repository.findByUsernameAndDomainOrderByIncTimeDesc(username, domain);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    @Transactional
    public int cleanExpired() {
        int now = (int) (System.currentTimeMillis() / 1000);
        return repository.deleteByExpTimeBefore(now);
    }

    public Map<String, Object> getStats() {
        int now = (int) (System.currentTimeMillis() / 1000);
        long total = repository.count();
        long expired = repository.countByExpTimeBefore(now);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("expired", expired);
        result.put("active", total - expired);
        return result;
    }

    public void dump() { kamailioRpc.msiloDump().block(RpcTimeouts.DEFAULT_BLOCK); }
}
