package io.kamailio.admin.modules.userdata.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.userdata.entity.DbAliases;
import io.kamailio.admin.modules.userdata.entity.Grp;
import io.kamailio.admin.modules.userdata.entity.SpeedDial;
import io.kamailio.admin.modules.userdata.repository.DbAliasesRepository;
import io.kamailio.admin.modules.userdata.repository.GrpRepository;
import io.kamailio.admin.modules.userdata.repository.SpeedDialRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserdataService {
    private final DbAliasesRepository aliasRepository;
    private final GrpRepository grpRepository;
    private final SpeedDialRepository speedDialRepository;

    public UserdataService(DbAliasesRepository aliasRepository, GrpRepository grpRepository, SpeedDialRepository speedDialRepository) {
        this.aliasRepository = aliasRepository;
        this.grpRepository = grpRepository;
        this.speedDialRepository = speedDialRepository;
    }

    public PaginatedResult<DbAliases> findAllAliases(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? aliasRepository.findByAliasUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : aliasRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public DbAliases createAlias(DbAliases data) { return aliasRepository.save(data); }

    @Transactional
    public DbAliases updateAlias(Integer id, DbAliases data) {
        var item = aliasRepository.findById(id).orElseThrow(() -> new RuntimeException("别名不存在"));
        if (data.getAliasUsername() != null) item.setAliasUsername(data.getAliasUsername());
        if (data.getAliasDomain() != null) item.setAliasDomain(data.getAliasDomain());
        if (data.getUsername() != null) item.setUsername(data.getUsername());
        if (data.getDomain() != null) item.setDomain(data.getDomain());
        return aliasRepository.save(item);
    }

    @Transactional
    public void removeAlias(Integer id) { aliasRepository.deleteById(id); }

    public PaginatedResult<Grp> findAllGroups(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? grpRepository.findByUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : grpRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public Grp createGroup(Grp data) { return grpRepository.save(data); }

    @Transactional
    public Grp updateGroup(Integer id, Grp data) {
        var item = grpRepository.findById(id).orElseThrow(() -> new RuntimeException("分组不存在"));
        if (data.getUsername() != null) item.setUsername(data.getUsername());
        if (data.getDomain() != null) item.setDomain(data.getDomain());
        if (data.getGrp() != null) item.setGrp(data.getGrp());
        return grpRepository.save(item);
    }

    @Transactional
    public void removeGroup(Integer id) { grpRepository.deleteById(id); }

    public PaginatedResult<SpeedDial> findAllSpeedDials(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? speedDialRepository.findByUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : speedDialRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public SpeedDial createSpeedDial(SpeedDial data) { return speedDialRepository.save(data); }

    @Transactional
    public SpeedDial updateSpeedDial(Integer id, SpeedDial data) {
        var item = speedDialRepository.findById(id).orElseThrow(() -> new RuntimeException("快捷拨号不存在"));
        if (data.getUsername() != null) item.setUsername(data.getUsername());
        if (data.getDomain() != null) item.setDomain(data.getDomain());
        if (data.getSdUsername() != null) item.setSdUsername(data.getSdUsername());
        if (data.getSdDomain() != null) item.setSdDomain(data.getSdDomain());
        if (data.getNewUri() != null) item.setNewUri(data.getNewUri());
        if (data.getDescription() != null) item.setDescription(data.getDescription());
        return speedDialRepository.save(item);
    }

    @Transactional
    public void removeSpeedDial(Integer id) { speedDialRepository.deleteById(id); }
}
