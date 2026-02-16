package io.kamailio.admin.modules.drouting.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.drouting.entity.DrCarrier;
import io.kamailio.admin.modules.drouting.entity.DrGateway;
import io.kamailio.admin.modules.drouting.entity.DrGroup;
import io.kamailio.admin.modules.drouting.entity.DrRule;
import io.kamailio.admin.modules.drouting.repository.DrCarrierRepository;
import io.kamailio.admin.modules.drouting.repository.DrGatewayRepository;
import io.kamailio.admin.modules.drouting.repository.DrGroupRepository;
import io.kamailio.admin.modules.drouting.repository.DrRuleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class DroutingService {
    private final DrGatewayRepository gatewayRepository;
    private final DrRuleRepository ruleRepository;
    private final DrGroupRepository groupRepository;
    private final DrCarrierRepository carrierRepository;
    private final KamailioRpcService kamailioRpc;

    public DroutingService(DrGatewayRepository gatewayRepository, DrRuleRepository ruleRepository,
                           DrGroupRepository groupRepository, DrCarrierRepository carrierRepository,
                           KamailioRpcService kamailioRpc) {
        this.gatewayRepository = gatewayRepository;
        this.ruleRepository = ruleRepository;
        this.groupRepository = groupRepository;
        this.carrierRepository = carrierRepository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<DrGateway> findAllGateways(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? gatewayRepository.findByGwidContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : gatewayRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }
    @Transactional
    public DrGateway createGateway(DrGateway data) {
        return gatewayRepository.save(data);
    }
    public DrGateway updateGateway(Integer id, DrGateway data) {
        var g = gatewayRepository.findById(id).orElseThrow(() -> new RuntimeException("网关不存在"));
        if (data.getGwid() != null) g.setGwid(data.getGwid());
        if (data.getType() != null) g.setType(data.getType());
        if (data.getAddress() != null) g.setAddress(data.getAddress());
        if (data.getStrip() != null) g.setStrip(data.getStrip());
        if (data.getPriPrefix() != null) g.setPriPrefix(data.getPriPrefix());
        if (data.getAttrs() != null) g.setAttrs(data.getAttrs());
        if (data.getProbeMode() != null) g.setProbeMode(data.getProbeMode());
        if (data.getState() != null) g.setState(data.getState());
        if (data.getSocket() != null) g.setSocket(data.getSocket());
        if (data.getDescription() != null) g.setDescription(data.getDescription());
        return gatewayRepository.save(g);
    }
    @Transactional
    public void removeGateway(Integer id) {
        gatewayRepository.delete(gatewayRepository.findById(id).orElseThrow(() -> new RuntimeException("网关不存在")));
    }

    public PaginatedResult<DrRule> findAllRules(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? ruleRepository.findByPrefixContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "priority").and(Sort.by("ruleid"))))
                : ruleRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "priority").and(Sort.by("ruleid"))));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }
    @Transactional
    public DrRule createRule(DrRule data) { return ruleRepository.save(data); }
    public DrRule updateRule(Integer id, DrRule data) {
        var r = ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("规则不存在"));
        if (data.getGroupid() != null) r.setGroupid(data.getGroupid());
        if (data.getPrefix() != null) r.setPrefix(data.getPrefix());
        if (data.getTimerec() != null) r.setTimerec(data.getTimerec());
        if (data.getPriority() != null) r.setPriority(data.getPriority());
        if (data.getRouteid() != null) r.setRouteid(data.getRouteid());
        if (data.getGwlist() != null) r.setGwlist(data.getGwlist());
        if (data.getSortAlg() != null) r.setSortAlg(data.getSortAlg());
        if (data.getSortProfile() != null) r.setSortProfile(data.getSortProfile());
        if (data.getAttrs() != null) r.setAttrs(data.getAttrs());
        if (data.getDescription() != null) r.setDescription(data.getDescription());
        return ruleRepository.save(r);
    }
    @Transactional
    public void removeRule(Integer id) {
        ruleRepository.delete(ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("规则不存在")));
    }

    public PaginatedResult<DrGroup> findAllGroups(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? groupRepository.findByUsernameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("groupid", "id")))
                : groupRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("groupid", "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }
    @Transactional
    public DrGroup createGroup(DrGroup data) { return groupRepository.save(data); }
    public DrGroup updateGroup(Integer id, DrGroup data) {
        var g = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("分组不存在"));
        if (data.getUsername() != null) g.setUsername(data.getUsername());
        if (data.getDomain() != null) g.setDomain(data.getDomain());
        if (data.getGroupid() != null) g.setGroupid(data.getGroupid());
        if (data.getDescription() != null) g.setDescription(data.getDescription());
        return groupRepository.save(g);
    }
    @Transactional
    public void removeGroup(Integer id) {
        groupRepository.delete(groupRepository.findById(id).orElseThrow(() -> new RuntimeException("分组不存在")));
    }

    public PaginatedResult<DrCarrier> findAllCarriers(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? carrierRepository.findByCarrieridContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : carrierRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }
    @Transactional
    public DrCarrier createCarrier(DrCarrier data) { return carrierRepository.save(data); }
    public DrCarrier updateCarrier(Integer id, DrCarrier data) {
        var c = carrierRepository.findById(id).orElseThrow(() -> new RuntimeException("运营商不存在"));
        if (data.getCarrierid() != null) c.setCarrierid(data.getCarrierid());
        if (data.getGwlist() != null) c.setGwlist(data.getGwlist());
        if (data.getFlags() != null) c.setFlags(data.getFlags());
        if (data.getSortAlg() != null) c.setSortAlg(data.getSortAlg());
        if (data.getState() != null) c.setState(data.getState());
        if (data.getAttrs() != null) c.setAttrs(data.getAttrs());
        if (data.getDescription() != null) c.setDescription(data.getDescription());
        return carrierRepository.save(c);
    }
    @Transactional
    public void removeCarrier(Integer id) {
        carrierRepository.delete(carrierRepository.findById(id).orElseThrow(() -> new RuntimeException("运营商不存在")));
    }

    public void reload() { kamailioRpc.droutingReload().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getGwStatus() { return kamailioRpc.droutingGwStatus().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Object getCarrierStatus() { return kamailioRpc.droutingCarrierStatus().block(RpcTimeouts.DEFAULT_BLOCK); }
    public Map<String, Object> getStats() {
        return Map.of(
                "gateways", gatewayRepository.count(),
                "rules", ruleRepository.count(),
                "groups", groupRepository.count(),
                "carriers", carrierRepository.count()
        );
    }
}
