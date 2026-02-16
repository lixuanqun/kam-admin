package io.kamailio.admin.modules.msilo.repository;

import io.kamailio.admin.modules.msilo.entity.Silo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiloRepository extends JpaRepository<Silo, Integer> {
    Page<Silo> findByUsernameContaining(String keyword, Pageable pageable);
    List<Silo> findByUsernameAndDomainOrderByIncTimeDesc(String username, String domain);

    @Query("SELECT COUNT(s) FROM Silo s WHERE s.expTime < :now")
    long countByExpTimeBefore(@Param("now") int now);

    @Modifying
    @Query("DELETE FROM Silo s WHERE s.expTime < :now")
    int deleteByExpTimeBefore(@Param("now") int now);
}
