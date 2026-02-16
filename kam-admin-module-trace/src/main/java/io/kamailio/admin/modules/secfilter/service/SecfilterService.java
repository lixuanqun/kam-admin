package io.kamailio.admin.modules.secfilter.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.secfilter.entity.Secfilter;
import io.kamailio.admin.modules.secfilter.repository.SecfilterRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecfilterService {
    private final SecfilterRepository repository;
    private final KamailioRpcService kamailioRpc;

    public SecfilterService(SecfilterRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Secfilter> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByDataContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("type", "id")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("type", "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public Secfilter create(Secfilter data) { return repository.save(data); }

    @Transactional
    public Secfilter update(Integer id, Secfilter data) {
        var item = repository.findById(id).orElseThrow(() -> new RuntimeException("记录不存在"));
        if (data.getAction() != null) item.setAction(data.getAction());
        if (data.getType() != null) item.setType(data.getType());
        if (data.getData() != null) item.setData(data.getData());
        return repository.save(item);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    public void reload() { kamailioRpc.secfilterReload().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object print() { return kamailioRpc.secfilterPrint().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object stats() { return kamailioRpc.secfilterStats().block(RpcTimeouts.DEFAULT_BLOCK); }
    public void statsReset() { kamailioRpc.secfilterStatsReset().block(RpcTimeouts.DEFAULT_BLOCK); }
    public void addBlacklist(int type, String data) { kamailioRpc.secfilterAddBl(type, data).block(RpcTimeouts.DEFAULT_BLOCK); }
    public void addWhitelist(int type, String data) { kamailioRpc.secfilterAddWl(type, data).block(RpcTimeouts.DEFAULT_BLOCK); }
}
