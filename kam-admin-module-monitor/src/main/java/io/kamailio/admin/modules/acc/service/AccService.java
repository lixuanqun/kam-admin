package io.kamailio.admin.modules.acc.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.modules.acc.dto.QueryAccDto;
import io.kamailio.admin.modules.acc.entity.Acc;
import io.kamailio.admin.modules.acc.entity.MissedCall;
import io.kamailio.admin.modules.acc.repository.AccRepository;
import io.kamailio.admin.modules.acc.repository.MissedCallRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AccService {
    private final AccRepository accRepository;
    private final MissedCallRepository missedCallRepository;

    public AccService(AccRepository accRepository, MissedCallRepository missedCallRepository) {
        this.accRepository = accRepository;
        this.missedCallRepository = missedCallRepository;
    }

    public PaginatedResult<Acc> findAllAcc(QueryAccDto dto) {
        var spec = accSpec(dto);
        var page = accRepository.findAll(spec, PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "time")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    public PaginatedResult<MissedCall> findAllMissedCalls(QueryAccDto dto) {
        var spec = missedSpec(dto);
        var page = missedCallRepository.findAll(spec, PageRequest.of(dto.pageOrDefault() - 1, dto.limitOrDefault(), org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "time")));
        return new PaginatedResult<>(page.getContent(), page.getTotalElements(), dto.pageOrDefault(), dto.limitOrDefault());
    }

    private Specification<Acc> accSpec(QueryAccDto dto) {
        return (root, q, cb) -> {
            var preds = new ArrayList<Predicate>();
            if (dto.getSrcUser() != null && !dto.getSrcUser().isBlank())
                preds.add(cb.like(root.get("srcUser"), "%" + dto.getSrcUser() + "%"));
            if (dto.getDstUser() != null && !dto.getDstUser().isBlank())
                preds.add(cb.like(root.get("dstUser"), "%" + dto.getDstUser() + "%"));
            if (dto.getCallid() != null && !dto.getCallid().isBlank())
                preds.add(cb.like(root.get("callid"), "%" + dto.getCallid() + "%"));
            if (dto.getStartTime() != null)
                preds.add(cb.greaterThanOrEqualTo(root.get("time"), Instant.parse(dto.getStartTime())));
            if (dto.getEndTime() != null)
                preds.add(cb.lessThanOrEqualTo(root.get("time"), Instant.parse(dto.getEndTime())));
            if (dto.getSipCode() != null && !dto.getSipCode().isBlank())
                preds.add(cb.equal(root.get("sipCode"), dto.getSipCode()));
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

    private Specification<MissedCall> missedSpec(QueryAccDto dto) {
        return (root, q, cb) -> {
            var preds = new ArrayList<Predicate>();
            if (dto.getSrcUser() != null && !dto.getSrcUser().isBlank())
                preds.add(cb.like(root.get("srcUser"), "%" + dto.getSrcUser() + "%"));
            if (dto.getDstUser() != null && !dto.getDstUser().isBlank())
                preds.add(cb.like(root.get("dstUser"), "%" + dto.getDstUser() + "%"));
            if (dto.getStartTime() != null)
                preds.add(cb.greaterThanOrEqualTo(root.get("time"), Instant.parse(dto.getStartTime())));
            if (dto.getEndTime() != null)
                preds.add(cb.lessThanOrEqualTo(root.get("time"), Instant.parse(dto.getEndTime())));
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

    public Map<String, Object> getStats(String startTime, String endTime) {
        var st = startTime != null ? Instant.parse(startTime) : null;
        var et = endTime != null ? Instant.parse(endTime) : null;
        long totalCalls = st != null || et != null
                ? accRepository.count((Specification<Acc>) (root, q, cb) -> {
                    var p = new ArrayList<Predicate>();
                    if (st != null) p.add(cb.greaterThanOrEqualTo(root.get("time"), st));
                    if (et != null) p.add(cb.lessThanOrEqualTo(root.get("time"), et));
                    return cb.and(p.toArray(new Predicate[0]));
                })
                : accRepository.count();
        var missedSpec = (Specification<MissedCall>) (root, q, cb) -> {
            var p = new ArrayList<Predicate>();
            if (st != null) p.add(cb.greaterThanOrEqualTo(root.get("time"), st));
            if (et != null) p.add(cb.lessThanOrEqualTo(root.get("time"), et));
            return cb.and(p.toArray(new Predicate[0]));
        };
        long missedCalls = missedCallRepository.count(missedSpec);
        long successCalls = accRepository.count((Specification<Acc>) (root, q, cb) -> {
            var list = new ArrayList<Predicate>();
            list.add(cb.like(root.get("sipCode"), "2%"));
            if (st != null) list.add(cb.greaterThanOrEqualTo(root.get("time"), st));
            if (et != null) list.add(cb.lessThanOrEqualTo(root.get("time"), et));
            return cb.and(list.toArray(new Predicate[0]));
        });
        return Map.of(
                "totalCalls", totalCalls,
                "missedCalls", missedCalls,
                "successCalls", successCalls,
                "failedCalls", totalCalls - successCalls,
                "callsByHour", List.<Map<String, Object>>of(),
                "callsBySipCode", List.<Map<String, Object>>of()
        );
    }

    public Map<String, Object> getTodayStats() {
        var today = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        long totalCalls = accRepository.count((Specification<Acc>) (root, q, cb) -> cb.greaterThanOrEqualTo(root.get("time"), today));
        long missedCalls = missedCallRepository.count((Specification<MissedCall>) (root, q, cb) -> cb.greaterThanOrEqualTo(root.get("time"), today));
        long successCalls = accRepository.count((Specification<Acc>) (root, q, cb) -> cb.and(cb.like(root.get("sipCode"), "2%"), cb.greaterThanOrEqualTo(root.get("time"), today)));
        double successRate = totalCalls > 0 ? Math.round((successCalls * 100.0 / totalCalls) * 100) / 100.0 : 0;
        return Map.of("totalCalls", totalCalls, "missedCalls", missedCalls, "successRate", successRate);
    }
}
