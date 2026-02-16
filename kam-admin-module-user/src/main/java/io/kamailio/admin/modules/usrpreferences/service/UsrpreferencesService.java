package io.kamailio.admin.modules.usrpreferences.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.usrpreferences.entity.UsrPreferences;
import io.kamailio.admin.modules.usrpreferences.repository.UsrPreferencesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsrpreferencesService {
    private final UsrPreferencesRepository repository;

    public UsrpreferencesService(UsrPreferencesRepository repository) {
        this.repository = repository;
    }

    public PaginatedResult<UsrPreferences> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? repository.findByUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")))
                : repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public List<UsrPreferences> findByUser(String username, String domain) {
        return repository.findByUsernameAndDomain(username, domain);
    }

    @Transactional
    public UsrPreferences create(UsrPreferences data) { return repository.save(data); }

    @Transactional
    public UsrPreferences update(Integer id, UsrPreferences data) {
        var item = repository.findById(id).orElseThrow(() -> new RuntimeException("记录不存在"));
        if (data.getUuid() != null) item.setUuid(data.getUuid());
        if (data.getUsername() != null) item.setUsername(data.getUsername());
        if (data.getDomain() != null) item.setDomain(data.getDomain());
        if (data.getAttribute() != null) item.setAttribute(data.getAttribute());
        if (data.getType() != null) item.setType(data.getType());
        if (data.getValue() != null) item.setValue(data.getValue());
        return repository.save(item);
    }

    @Transactional
    public void remove(Integer id) { repository.deleteById(id); }

    @Transactional
    public void removeByUser(String username, String domain) {
        repository.deleteByUsernameAndDomain(username, domain);
    }
}
