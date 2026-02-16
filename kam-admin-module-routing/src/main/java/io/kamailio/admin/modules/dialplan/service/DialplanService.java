package io.kamailio.admin.modules.dialplan.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.dialplan.entity.Dialplan;
import io.kamailio.admin.modules.dialplan.repository.DialplanRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DialplanService {
    private final DialplanRepository repository;
    private final KamailioRpcService kamailioRpc;

    public DialplanService(DialplanRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Dialplan> findAll(PaginationDto dto, Integer dpid) {
        var sort = Sort.by("dpid", "priority");
        var pageable = PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), sort);
        var page = dpid != null ? repository.findByDpid(dpid, pageable) : repository.findAll(pageable);
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public Dialplan create(Dialplan data) { return repository.save(data); }

    @Transactional
    public Dialplan update(Integer id, Dialplan data) {
        var item = repository.findById(id).orElseThrow(() -> new RuntimeException("规则不存在"));
        if (data.getDpid() != null) item.setDpid(data.getDpid());
        if (data.getPriority() != null) item.setPriority(data.getPriority());
        if (data.getMatchOp() != null) item.setMatchOp(data.getMatchOp());
        if (data.getMatchExp() != null) item.setMatchExp(data.getMatchExp());
        if (data.getMatchLen() != null) item.setMatchLen(data.getMatchLen());
        if (data.getSubstExp() != null) item.setSubstExp(data.getSubstExp());
        if (data.getReplExp() != null) item.setReplExp(data.getReplExp());
        if (data.getAttrs() != null) item.setAttrs(data.getAttrs());
        return repository.save(item);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    public void reload() { kamailioRpc.dialplanReload().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object translate(int dpid, String input) { return kamailioRpc.dialplanTranslate(dpid, input).block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object dump(Integer dpid) { return kamailioRpc.dialplanDump(dpid).block(RpcTimeouts.DEFAULT_BLOCK); }
}
