package io.kamailio.admin.modules.carrierroute.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.carrierroute.entity.CarrierFailureRoute;
import io.kamailio.admin.modules.carrierroute.entity.CarrierName;
import io.kamailio.admin.modules.carrierroute.entity.DomainName;
import io.kamailio.admin.modules.carrierroute.repository.CarrierFailureRouteRepository;
import io.kamailio.admin.modules.carrierroute.repository.CarrierNameRepository;
import io.kamailio.admin.modules.carrierroute.repository.DomainNameRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarrierrouteService {
    private final CarrierNameRepository carrierRepository;
    private final DomainNameRepository domainRepository;
    private final CarrierFailureRouteRepository failureRouteRepository;
    private final KamailioRpcService kamailioRpc;

    public CarrierrouteService(CarrierNameRepository carrierRepository, DomainNameRepository domainRepository,
                               CarrierFailureRouteRepository failureRouteRepository, KamailioRpcService kamailioRpc) {
        this.carrierRepository = carrierRepository;
        this.domainRepository = domainRepository;
        this.failureRouteRepository = failureRouteRepository;
        this.kamailioRpc = kamailioRpc;
    }

    public List<CarrierName> findAllCarriers() {
        return carrierRepository.findAll(Sort.by("id"));
    }

    @Transactional
    public CarrierName createCarrier(String carrier) {
        var e = new CarrierName();
        e.setCarrier(carrier);
        return carrierRepository.save(e);
    }

    @Transactional
    public void deleteCarrier(Integer id) { carrierRepository.deleteById(id); }

    public List<DomainName> findAllDomains() {
        return domainRepository.findAll(Sort.by("id"));
    }

    @Transactional
    public DomainName createDomain(String domain) {
        var e = new DomainName();
        e.setDomain(domain);
        return domainRepository.save(e);
    }

    @Transactional
    public void deleteDomain(Integer id) { domainRepository.deleteById(id); }

    public PaginatedResult<CarrierFailureRoute> findAllFailureRoutes(PaginationDto dto) {
        var page = failureRouteRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    @Transactional
    public CarrierFailureRoute createFailureRoute(CarrierFailureRoute data) { return failureRouteRepository.save(data); }

    @Transactional
    public void deleteFailureRoute(Integer id) { failureRouteRepository.deleteById(id); }

    public void reload() { kamailioRpc.crReloadRoutes().block(); }
    public Object dumpRoutes() { return kamailioRpc.crDumpRoutes().block(); }
}
