package io.kamailio.admin.modules.location.repository;

import io.kamailio.admin.modules.location.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Page<Location> findByExpiresAfterAndUsernameContaining(Instant expires, String keyword, Pageable pageable);
    Page<Location> findByExpiresAfter(Instant expires, Pageable pageable);
    long countByExpiresAfter(Instant expires);
    List<Location> findByUsernameAndExpiresAfterOrderByLastModifiedDesc(String username, Instant expires);
    @Query("SELECT l.domain, COUNT(l) FROM Location l WHERE l.expires > :now GROUP BY l.domain")
    List<Object[]> countByDomain(Instant now);
    @Query(value = "SELECT user_agent, COUNT(*) FROM location WHERE expires > ?1 GROUP BY user_agent ORDER BY COUNT(*) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> countByUserAgent(Instant now);
}
