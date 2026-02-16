package io.kamailio.admin.modules.lcr.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.lcr.entity.LcrGw;
import io.kamailio.admin.modules.lcr.entity.LcrRule;
import io.kamailio.admin.modules.lcr.entity.LcrRuleTarget;
import io.kamailio.admin.modules.lcr.repository.LcrGwRepository;
import io.kamailio.admin.modules.lcr.repository.LcrRuleRepository;
import io.kamailio.admin.modules.lcr.repository.LcrRuleTargetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LcrService {
    private final LcrGwRepository gwRepository;
    private final LcrRuleRepository ruleRepository;
    private final LcrRuleTargetRepository targetRepository;
    private final KamailioRpcService kamailioRpc;

    public LcrService(LcrGwRepository gwRepository, LcrRuleRepository ruleRepository,
                      LcrRuleTargetRepository targetRepository, KamailioRpcService kamailioRpc) {
        this.gwRepository = gwRepository;
        this.ruleRepository = ruleRepository;
        this.targetRepository = targetRepository;
        this.kamailioRpc = kamailioRpc;
    }

    public PaginatedResult<LcrGw> findAllGateways(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? gwRepository.findByGwNameContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : gwRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public LcrGw createGateway(LcrGw data) { return gwRepository.save(data); }

    @Transactional
    public LcrGw updateGateway(Integer id, LcrGw data) {
        var gw = gwRepository.findById(id).orElseThrow(() -> new RuntimeException("网关不存在"));
        if (data.getLcrId() != null) gw.setLcrId(data.getLcrId());
        if (data.getGwName() != null) gw.setGwName(data.getGwName());
        if (data.getIpAddr() != null) gw.setIpAddr(data.getIpAddr());
        if (data.getHostname() != null) gw.setHostname(data.getHostname());
        if (data.getPort() != null) gw.setPort(data.getPort());
        if (data.getUriScheme() != null) gw.setUriScheme(data.getUriScheme());
        if (data.getTransport() != null) gw.setTransport(data.getTransport());
        if (data.getParams() != null) gw.setParams(data.getParams());
        if (data.getStrip() != null) gw.setStrip(data.getStrip());
        if (data.getPrefix() != null) gw.setPrefix(data.getPrefix());
        if (data.getTag() != null) gw.setTag(data.getTag());
        if (data.getFlags() != null) gw.setFlags(data.getFlags());
        if (data.getDefunct() != null) gw.setDefunct(data.getDefunct());
        return gwRepository.save(gw);
    }

    @Transactional
    public void removeGateway(Integer id) { gwRepository.deleteById(id); }

    public PaginatedResult<LcrRule> findAllRules(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? ruleRepository.findByPrefixContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")))
                : ruleRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public LcrRule createRule(LcrRule data) { return ruleRepository.save(data); }

    @Transactional
    public LcrRule updateRule(Integer id, LcrRule data) {
        var rule = ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("规则不存在"));
        if (data.getLcrId() != null) rule.setLcrId(data.getLcrId());
        if (data.getPrefix() != null) rule.setPrefix(data.getPrefix());
        if (data.getFromUri() != null) rule.setFromUri(data.getFromUri());
        if (data.getRequestUri() != null) rule.setRequestUri(data.getRequestUri());
        if (data.getMtTvalue() != null) rule.setMtTvalue(data.getMtTvalue());
        if (data.getStopper() != null) rule.setStopper(data.getStopper());
        if (data.getEnabled() != null) rule.setEnabled(data.getEnabled());
        return ruleRepository.save(rule);
    }

    @Transactional
    public void removeRule(Integer id) { ruleRepository.deleteById(id); }

    public PaginatedResult<LcrRuleTarget> findAllTargets(PaginationDto dto) {
        var page = targetRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("priority", "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public LcrRuleTarget createTarget(LcrRuleTarget data) { return targetRepository.save(data); }

    @Transactional
    public LcrRuleTarget updateTarget(Integer id, LcrRuleTarget data) {
        var target = targetRepository.findById(id).orElseThrow(() -> new RuntimeException("目标不存在"));
        if (data.getLcrId() != null) target.setLcrId(data.getLcrId());
        if (data.getRuleId() != null) target.setRuleId(data.getRuleId());
        if (data.getGwId() != null) target.setGwId(data.getGwId());
        if (data.getPriority() != null) target.setPriority(data.getPriority());
        if (data.getWeight() != null) target.setWeight(data.getWeight());
        return targetRepository.save(target);
    }

    @Transactional
    public void removeTarget(Integer id) { targetRepository.deleteById(id); }

    public void reload() { kamailioRpc.lcrReload().block(); }
    public Object dumpGws() { return kamailioRpc.lcrDumpGws().block(); }
    public Object dumpRules() { return kamailioRpc.lcrDumpRules().block(); }
}
