package io.kamailio.admin.modules.topos.repository;

import io.kamailio.admin.modules.topos.entity.ToposD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface ToposDRepository extends JpaRepository<ToposD, Integer> {
    Page<ToposD> findByACallidContaining(String keyword, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ToposD t WHERE t.rectime < :before")
    int deleteByRectimeBefore(@Param("before") Instant before);
}
