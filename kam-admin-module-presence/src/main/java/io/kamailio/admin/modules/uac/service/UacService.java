package io.kamailio.admin.modules.uac.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.uac.entity.UacReg;
import io.kamailio.admin.modules.uac.repository.UacRegRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UacService {
    private final UacRegRepository uacRegRepository;
    private final KamailioRpcService kamailioRpc;

    public UacService(UacRegRepository uacRegRepository, KamailioRpcService kamailioRpc) {
        this.uacRegRepository = uacRegRepository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<UacReg> findAll(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? uacRegRepository.findByLUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : uacRegRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public UacReg create(UacReg data) { return uacRegRepository.save(data); }

    @Transactional
    public UacReg update(Integer id, UacReg data) {
        var item = uacRegRepository.findById(id).orElseThrow(() -> new RuntimeException("注册记录不存在"));
        if (data.getLUuid() != null) item.setLUuid(data.getLUuid());
        if (data.getLUsername() != null) item.setLUsername(data.getLUsername());
        if (data.getLDomain() != null) item.setLDomain(data.getLDomain());
        if (data.getRUsername() != null) item.setRUsername(data.getRUsername());
        if (data.getRDomain() != null) item.setRDomain(data.getRDomain());
        if (data.getRealm() != null) item.setRealm(data.getRealm());
        if (data.getAuthUsername() != null) item.setAuthUsername(data.getAuthUsername());
        if (data.getAuthPassword() != null) item.setAuthPassword(data.getAuthPassword());
        if (data.getAuthProxy() != null) item.setAuthProxy(data.getAuthProxy());
        if (data.getExpires() != null) item.setExpires(data.getExpires());
        if (data.getFlags() != null) item.setFlags(data.getFlags());
        if (data.getRegDelay() != null) item.setRegDelay(data.getRegDelay());
        if (data.getContactAddr() != null) item.setContactAddr(data.getContactAddr());
        if (data.getSocket() != null) item.setSocket(data.getSocket());
        return uacRegRepository.save(item);
    }

    @Transactional
    public void remove(Integer id) { uacRegRepository.deleteById(id); }

    public void reload() { kamailioRpc.uacRegReload().block(); }
    public Object getInfo(String lUuid) { return kamailioRpc.uacRegInfo(lUuid).block(); }
    public void refresh(String lUuid) { kamailioRpc.uacRegRefresh(lUuid).block(); }
    public Object dumpList() { return kamailioRpc.uacRegDump().block(); }
}
