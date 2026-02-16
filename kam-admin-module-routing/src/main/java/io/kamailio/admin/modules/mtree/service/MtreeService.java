package io.kamailio.admin.modules.mtree.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.mtree.entity.Mtree;
import io.kamailio.admin.modules.mtree.repository.MtreeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MtreeService {
    private final MtreeRepository repository;
    private final KamailioRpcService kamailioRpc;

    public MtreeService(MtreeRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Mtree> findAll(PaginationDto dto, String tname) {
        var sort = Sort.by("tname", "tprefix");
        var pageable = PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), sort);
        Page<Mtree> page;
        if (tname != null && !tname.isBlank()) {
            page = repository.findByTname(tname, pageable);
        } else if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            page = repository.findByTprefixContaining(dto.getKeyword(), pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public Mtree create(Mtree data) { return repository.save(data); }

    @Transactional
    public Mtree update(Integer id, Mtree data) {
        var item = repository.findById(id).orElseThrow(() -> new RuntimeException("记录不存在"));
        if (data.getTname() != null) item.setTname(data.getTname());
        if (data.getTprefix() != null) item.setTprefix(data.getTprefix());
        if (data.getTvalue() != null) item.setTvalue(data.getTvalue());
        return repository.save(item);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    public void reload(String tname) { kamailioRpc.mtreeReload(tname).block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object match(String tname, String prefix) { return kamailioRpc.mtreeMatch(tname, prefix).block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object summary(String tname) { return kamailioRpc.mtreeSummary(tname).block(RpcTimeouts.DEFAULT_BLOCK); }
}
