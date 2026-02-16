package io.kamailio.admin.modules.topos.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.topos.entity.ToposD;
import io.kamailio.admin.modules.topos.repository.ToposDRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ToposService {
    private final ToposDRepository repository;

    public ToposService(ToposDRepository repository) {
        this.repository = repository;
    }

    public PaginatedResult<ToposD> findAllDialogs(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByACallidContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "rectime")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "rectime")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public int cleanup(Instant beforeDate) {
        return repository.deleteByRectimeBefore(beforeDate);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("total", repository.count());
        return result;
    }
}
