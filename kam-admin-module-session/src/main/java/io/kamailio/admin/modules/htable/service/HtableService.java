package io.kamailio.admin.modules.htable.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.htable.entity.Htable;
import io.kamailio.admin.modules.htable.repository.HtableRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HtableService {
    private final HtableRepository repository;
    private final KamailioRpcService kamailioRpc;

    public HtableService(HtableRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<Htable> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByKeyNameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public Htable create(Htable data) { return repository.save(data); }

    @Transactional
    public Htable update(Integer id, Htable data) {
        var item = repository.findById(id).orElseThrow(() -> new RuntimeException("记录不存在"));
        if (data.getKeyName() != null) item.setKeyName(data.getKeyName());
        if (data.getKeyType() != null) item.setKeyType(data.getKeyType());
        if (data.getValueType() != null) item.setValueType(data.getValueType());
        if (data.getKeyValue() != null) item.setKeyValue(data.getKeyValue());
        if (data.getExpires() != null) item.setExpires(data.getExpires());
        return repository.save(item);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    public Object getValue(String table, String key) { return kamailioRpc.htableGet(table, key).block(); }
    public void setString(String table, String key, String value) { kamailioRpc.htableSets(table, key, value).block(); }
    public void setInt(String table, String key, int value) { kamailioRpc.htableSeti(table, key, value).block(); }
    public void deleteKey(String table, String key) { kamailioRpc.htableDelete(table, key).block(); }
    public Object dump(String table) { return kamailioRpc.htableDump(table).block(); }
    public void reload(String table) { kamailioRpc.htableReload(table).block(); }
    public Object listTables() { return kamailioRpc.htableListTables().block(); }
    public Object stats() { return kamailioRpc.htableStats().block(); }
}
