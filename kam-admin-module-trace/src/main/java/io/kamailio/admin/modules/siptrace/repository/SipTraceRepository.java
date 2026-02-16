package io.kamailio.admin.modules.siptrace.repository;

import io.kamailio.admin.modules.siptrace.entity.SipTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SipTraceRepository extends JpaRepository<SipTrace, Integer>, JpaSpecificationExecutor<SipTrace> {
    List<SipTrace> findByCallidOrderByTimeStampAscTimeUsAsc(String callid);
    long countByTimeStampGreaterThanEqual(Instant since);

    @Modifying
    @Query("DELETE FROM SipTrace t WHERE t.timeStamp < :before")
    int deleteByTimeStampBefore(@Param("before") Instant before);
}
