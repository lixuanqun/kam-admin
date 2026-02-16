package io.kamailio.admin.modules.siptrace.service;

import io.kamailio.admin.common.dto.PaginatedResult;
import io.kamailio.admin.modules.siptrace.entity.SipTrace;
import io.kamailio.admin.modules.siptrace.repository.SipTraceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiptraceService {
    private final SipTraceRepository repository;

    public SiptraceService(SipTraceRepository repository) {
        this.repository = repository;
    }

    public PaginatedResult<SipTrace> findAll(Integer page, Integer limit, String callid, String tracedUser,
                                             String method, String fromIp, String toIp, String startTime, String endTime) {
        int p = page != null && page >= 1 ? page : 1;
        int l = limit != null && limit >= 1 ? Math.min(limit, 100) : 20;
        Specification<SipTrace> spec = (root, q, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (callid != null && !callid.isBlank())
                preds.add(cb.like(root.get("callid"), "%" + callid + "%"));
            if (tracedUser != null && !tracedUser.isBlank())
                preds.add(cb.like(root.get("tracedUser"), "%" + tracedUser + "%"));
            if (method != null && !method.isBlank())
                preds.add(cb.equal(root.get("method"), method));
            if (fromIp != null && !fromIp.isBlank())
                preds.add(cb.like(root.get("fromIp"), "%" + fromIp + "%"));
            if (toIp != null && !toIp.isBlank())
                preds.add(cb.like(root.get("toIp"), "%" + toIp + "%"));
            if (startTime != null && !startTime.isBlank())
                preds.add(cb.greaterThanOrEqualTo(root.get("timeStamp"), Instant.parse(startTime)));
            if (endTime != null && !endTime.isBlank())
                preds.add(cb.lessThanOrEqualTo(root.get("timeStamp"), Instant.parse(endTime)));
            return cb.and(preds.toArray(new Predicate[0]));
        };
        Page<SipTrace> pageResult = repository.findAll(spec, PageRequest.of(p - 1, l, Sort.by(Sort.Direction.DESC, "timeStamp").and(Sort.by(Sort.Direction.DESC, "timeUs"))));
        return new PaginatedResult<>(pageResult.getContent(), pageResult.getTotalElements(), p, l);
    }

    public List<SipTrace> getByCallId(String callid) {
        return repository.findByCallidOrderByTimeStampAscTimeUsAsc(callid);
    }

    public Map<String, Object> getStats() {
        long total = repository.count();
        Instant today = Instant.now().atZone(java.time.ZoneId.systemDefault()).toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant();
        long todayCount = repository.countByTimeStampGreaterThanEqual(today);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("todayCount", todayCount);
        result.put("methodStats", List.of());
        return result;
    }

    @Transactional
    public int cleanup(Instant beforeDate) {
        return repository.deleteByTimeStampBefore(beforeDate);
    }
}
