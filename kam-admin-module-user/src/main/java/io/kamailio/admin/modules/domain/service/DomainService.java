package io.kamailio.admin.modules.domain.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.exception.BusinessException;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.domain.dto.CreateDomainDto;
import io.kamailio.admin.modules.domain.dto.UpdateDomainDto;
import io.kamailio.admin.modules.domain.entity.Domain;
import io.kamailio.admin.modules.domain.repository.DomainRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DomainService {
    private final DomainRepository repository;
    private final KamailioRpcService kamailioRpc;

    public DomainService(DomainRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    private void reloadDomain() {
        kamailioRpc.reloadDomain().subscribe(v -> {}, e -> {});
    }

    @Transactional
    public Domain create(CreateDomainDto dto) {
        if (repository.findByDomain(dto.getDomain()).isPresent())
            throw new BusinessException("域已存在");
        var d = new Domain();
        d.setDomain(dto.getDomain());
        d.setDid(dto.getDid());
        d.setLastModified(Instant.now());
        var saved = repository.save(d);
        reloadDomain();
        return saved;
    }

    public PaginatedResult<Domain> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByDomainContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public List<Domain> findAllDomains() {
        return repository.findAll(Sort.by("domain"));
    }

    public Domain findOne(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("域不存在"));
    }

    @Transactional
    public Domain update(Integer id, UpdateDomainDto dto) {
        var d = findOne(id);
        if (dto.getDid() != null) d.setDid(dto.getDid());
        d.setLastModified(Instant.now());
        var saved = repository.save(d);
        reloadDomain();
        return saved;
    }

    @Transactional
    public void remove(Integer id) {
        repository.delete(findOne(id));
        reloadDomain();
    }

    public void reload() {
        kamailioRpc.reloadDomain().block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Object dump() {
        return kamailioRpc.call("domain.dump").block(RpcTimeouts.DEFAULT_BLOCK);
    }
}
