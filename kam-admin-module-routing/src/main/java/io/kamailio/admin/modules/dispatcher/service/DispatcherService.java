package io.kamailio.admin.modules.dispatcher.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.dispatcher.dto.CreateDispatcherDto;
import io.kamailio.admin.modules.dispatcher.dto.UpdateDispatcherDto;
import io.kamailio.admin.modules.dispatcher.entity.Dispatcher;
import io.kamailio.admin.modules.dispatcher.repository.DispatcherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
public class DispatcherService {

    private final DispatcherRepository repository;
    private final KamailioRpcService kamailioRpc;

    public DispatcherService(DispatcherRepository repository, KamailioRpcService kamailioRpc) {
        this.repository = repository;
        this.kamailioRpc = kamailioRpc;
    }

    private void reloadDispatcher() {
        kamailioRpc.reloadDispatcher().subscribe(
                v -> {},
                e -> {}
        );
    }

    @Transactional
    public Dispatcher create(CreateDispatcherDto dto) {
        var entity = new Dispatcher();
        entity.setSetid(dto.getSetid());
        entity.setDestination(dto.getDestination());
        entity.setFlags(dto.flagsOrDefault());
        entity.setPriority(dto.priorityOrDefault());
        entity.setAttrs(dto.attrsOrDefault());
        entity.setDescription(dto.descriptionOrDefault());
        var saved = repository.save(entity);
        reloadDispatcher();
        return saved;
    }

    public PaginatedResult<Dispatcher> findAll(PaginationDto dto) {
        Page<Dispatcher> page;
        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            page = repository.findByDestinationContaining(dto.getKeyword(),
                    PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(),
                            Sort.by(Sort.Direction.ASC, "setid")
                                    .and(Sort.by(Sort.Direction.DESC, "priority"))
                                    .and(Sort.by(Sort.Direction.ASC, "id"))));
        } else {
            page = repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(),
                    Sort.by(Sort.Direction.ASC, "setid")
                            .and(Sort.by(Sort.Direction.DESC, "priority"))
                            .and(Sort.by(Sort.Direction.ASC, "id"))));
        }
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(),
                dto.pageOrDefault(), dto.limitOrDefault());
    }

    public List<Dispatcher> findBySetId(Integer setid) {
        return repository.findBySetidOrderByPriorityDescIdAsc(setid);
    }

    public List<Integer> getSetIds() {
        return repository.findDistinctSetIds();
    }

    public Dispatcher findOne(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("调度目标不存在"));
    }

    @Transactional
    public Dispatcher update(Integer id, UpdateDispatcherDto dto) {
        var entity = findOne(id);
        if (dto.getSetid() != null) entity.setSetid(dto.getSetid());
        if (dto.getDestination() != null) entity.setDestination(dto.getDestination());
        if (dto.getFlags() != null) entity.setFlags(dto.getFlags());
        if (dto.getPriority() != null) entity.setPriority(dto.getPriority());
        if (dto.getAttrs() != null) entity.setAttrs(dto.getAttrs());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        var saved = repository.save(entity);
        reloadDispatcher();
        return saved;
    }

    @Transactional
    public void remove(Integer id) {
        repository.delete(findOne(id));
        reloadDispatcher();
    }

    public void reload() {
        kamailioRpc.reloadDispatcher().block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Object getStatus() {
        return kamailioRpc.getDispatcherList().block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public void setState(String state, Integer group, String address) {
        kamailioRpc.call("dispatcher.set_state", state, group, address).block(RpcTimeouts.DEFAULT_BLOCK);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStats() {
        var total = repository.count();
        var rows = repository.countBySetId();
        var groups = rows.stream()
                .map(r -> Map.<String, Object>of("setid", r[0], "count", r[1]))
                .toList();
        return Map.of("total", total, "groups", groups);
    }
}
