package io.kamailio.admin.modules.subscriber.service;

import io.kamailio.admin.common.exception.BusinessException;
import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.common.dto.PaginationDto;
import io.kamailio.admin.modules.subscriber.dto.CreateSubscriberDto;
import io.kamailio.admin.modules.subscriber.dto.UpdateSubscriberDto;
import io.kamailio.admin.modules.subscriber.entity.Subscriber;
import io.kamailio.admin.modules.subscriber.repository.SubscriberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Service
public class SubscriberService {

    private final SubscriberRepository repository;

    public SubscriberService(SubscriberRepository repository) {
        this.repository = repository;
    }

    private static String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateHa1(String username, String domain, String password) {
        return md5Hex(username + ":" + domain + ":" + password);
    }

    private String generateHa1b(String username, String domain, String password) {
        return md5Hex(username + "@" + domain + ":" + domain + ":" + password);
    }

    @Transactional
    public Subscriber create(CreateSubscriberDto dto) {
        if (repository.findByUsernameAndDomain(dto.getUsername(), dto.getDomain()).isPresent()) {
            throw new BusinessException("用户已存在");
        }
        var s = new Subscriber();
        s.setUsername(dto.getUsername());
        s.setDomain(dto.getDomain());
        s.setPassword(dto.getPassword());
        s.setHa1(generateHa1(dto.getUsername(), dto.getDomain(), dto.getPassword()));
        s.setHa1b(generateHa1b(dto.getUsername(), dto.getDomain(), dto.getPassword()));
        s.setEmailAddress(dto.getEmailAddress());
        s.setRpid(dto.getRpid());
        return repository.save(s);
    }

    public PaginatedResult<Subscriber> findAll(PaginationDto dto) {
        Page<Subscriber> page;
        if (dto.getKeyword() != null && !dto.getKeyword().isBlank()) {
            page = repository.findByUsernameContaining(dto.getKeyword(),
                    PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")));
        } else {
            page = repository.findAll(PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), Sort.by(Sort.Direction.DESC, "id")));
        }
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public Subscriber findOne(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("用户不存在"));
    }

    public Subscriber findByUsernameAndDomain(String username, String domain) {
        return repository.findByUsernameAndDomain(username, domain).orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Transactional
    public Subscriber update(Integer id, UpdateSubscriberDto dto) {
        var s = findOne(id);
        if (dto.getPassword() != null) {
            s.setPassword(dto.getPassword());
            s.setHa1(generateHa1(s.getUsername(), s.getDomain(), dto.getPassword()));
            s.setHa1b(generateHa1b(s.getUsername(), s.getDomain(), dto.getPassword()));
        }
        if (dto.getEmailAddress() != null) s.setEmailAddress(dto.getEmailAddress());
        if (dto.getRpid() != null) s.setRpid(dto.getRpid());
        return repository.save(s);
    }

    @Transactional
    public void remove(Integer id) {
        repository.delete(findOne(id));
    }

    @Transactional
    public void batchRemove(List<Integer> ids) {
        repository.deleteAllById(ids);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStats() {
        long total = repository.count();
        var rows = repository.countByDomain();
        var domains = rows.stream().map(r -> Map.<String, Object>of("domain", r[0], "count", r[1])).toList();
        return Map.of("total", total, "domains", domains);
    }
}
