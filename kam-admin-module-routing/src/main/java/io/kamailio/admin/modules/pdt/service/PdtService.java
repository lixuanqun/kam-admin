package io.kamailio.admin.modules.pdt.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.pdt.entity.Pdt;
import io.kamailio.admin.modules.pdt.repository.PdtRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PdtService {
    private final PdtRepository repository;
    private final KamailioRpcService kamailioRpc;

    public PdtService(PdtRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Pdt> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByPrefixContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("sdomain", "prefix")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("sdomain", "prefix")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public Pdt create(Pdt data) { return repository.save(data); }

    @Transactional
    public Pdt update(Integer id, Pdt data) {
        var item = repository.findById(id).orElseThrow(() -> new RuntimeException("记录不存在"));
        if (data.getSdomain() != null) item.setSdomain(data.getSdomain());
        if (data.getPrefix() != null) item.setPrefix(data.getPrefix());
        if (data.getDomain() != null) item.setDomain(data.getDomain());
        return repository.save(item);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    public void reload() { kamailioRpc.pdtReload().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object list() { return kamailioRpc.pdtList().block(RpcTimeouts.DEFAULT_BLOCK); }
}
