package io.kamailio.admin.modules.permissions.service;

import io.kamailio.admin.common.RpcTimeouts;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.modules.permissions.dto.CreateAddressDto;
import io.kamailio.admin.modules.permissions.dto.CreateTrustedDto;
import io.kamailio.admin.modules.permissions.entity.Address;
import io.kamailio.admin.modules.permissions.entity.Trusted;
import io.kamailio.admin.modules.permissions.repository.AddressRepository;
import io.kamailio.admin.modules.permissions.repository.TrustedRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PermissionsService {
    private final AddressRepository addressRepository;
    private final TrustedRepository trustedRepository;
    private final KamailioRpcService kamailioRpc;

    public PermissionsService(AddressRepository addressRepository, TrustedRepository trustedRepository, KamailioRpcService kamailioRpc) {
        this.addressRepository = addressRepository;
        this.trustedRepository = trustedRepository;
        this.kamailioRpc = kamailioRpc;
    }

    private void reloadPermissions() {
        kamailioRpc.reloadPermissions().subscribe(v -> {}, e -> {});
    }

    @Transactional
    public Address createAddress(CreateAddressDto dto) {
        var a = new Address();
        a.setGrp(dto.getGrp() != null ? dto.getGrp() : 1);
        a.setIpAddr(dto.getIpAddr());
        a.setMask(dto.getMask() != null ? dto.getMask() : 32);
        a.setPort(dto.getPort() != null ? dto.getPort() : 0);
        a.setTag(dto.getTag());
        var saved = addressRepository.save(a);
        reloadPermissions();
        return saved;
    }

    public PaginatedResult<Address> findAllAddresses(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? addressRepository.findByIpAddrContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("grp", "id")))
                : addressRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by("grp", "id")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public Address findOneAddress(Integer id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("地址不存在"));
    }

    @Transactional
    public Address updateAddress(Integer id, CreateAddressDto dto) {
        var a = findOneAddress(id);
        if (dto.getGrp() != null) a.setGrp(dto.getGrp());
        if (dto.getIpAddr() != null) a.setIpAddr(dto.getIpAddr());
        if (dto.getMask() != null) a.setMask(dto.getMask());
        if (dto.getPort() != null) a.setPort(dto.getPort());
        if (dto.getTag() != null) a.setTag(dto.getTag());
        var saved = addressRepository.save(a);
        reloadPermissions();
        return saved;
    }

    @Transactional
    public void removeAddress(Integer id) {
        addressRepository.delete(findOneAddress(id));
        reloadPermissions();
    }

    @Transactional
    public Trusted createTrusted(CreateTrustedDto dto) {
        var t = new Trusted();
        t.setSrcIp(dto.getSrcIp());
        t.setProto(dto.getProto());
        t.setFromPattern(dto.getFromPattern());
        t.setRuriPattern(dto.getRuriPattern());
        t.setTag(dto.getTag());
        t.setPriority(dto.getPriority() != null ? dto.getPriority() : 0);
        return trustedRepository.save(t);
    }

    public PaginatedResult<Trusted> findAllTrusted(PaginationDto dto) {
        var page = dto.getKeyword() != null && !dto.getKeyword().isBlank()
                ? trustedRepository.findBySrcIpContaining(dto.getKeyword(), PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "priority").and(Sort.by("id"))))
                : trustedRepository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "priority").and(Sort.by("id"))));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public Trusted findOneTrusted(Integer id) {
        return trustedRepository.findById(id).orElseThrow(() -> new RuntimeException("信任地址不存在"));
    }

    @Transactional
    public Trusted updateTrusted(Integer id, CreateTrustedDto dto) {
        var t = findOneTrusted(id);
        if (dto.getSrcIp() != null) t.setSrcIp(dto.getSrcIp());
        if (dto.getProto() != null) t.setProto(dto.getProto());
        if (dto.getFromPattern() != null) t.setFromPattern(dto.getFromPattern());
        if (dto.getRuriPattern() != null) t.setRuriPattern(dto.getRuriPattern());
        if (dto.getTag() != null) t.setTag(dto.getTag());
        if (dto.getPriority() != null) t.setPriority(dto.getPriority());
        return trustedRepository.save(t);
    }

    @Transactional
    public void removeTrusted(Integer id) {
        trustedRepository.delete(findOneTrusted(id));
    }

    public void reload() {
        kamailioRpc.reloadPermissions().block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public void reloadTrusted() {
        kamailioRpc.call("permissions.trustedReload").block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Object addressDump() {
        return kamailioRpc.call("permissions.addressDump").block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Object subnetDump() {
        return kamailioRpc.call("permissions.subnetDump").block(RpcTimeouts.DEFAULT_BLOCK);
    }

    public Map<String, Object> getStats() {
        long addressCount = addressRepository.count();
        long trustedCount = trustedRepository.count();
        var addressGroups = addressRepository.countByGrp().stream()
                .map(r -> Map.<String, Object>of("grp", r[0], "count", r[1])).toList();
        return Map.of("addressCount", addressCount, "trustedCount", trustedCount, "addressGroups", addressGroups);
    }
}
