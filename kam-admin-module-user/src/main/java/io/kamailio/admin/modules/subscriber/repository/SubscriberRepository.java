package io.kamailio.admin.modules.subscriber.repository;

import io.kamailio.admin.modules.subscriber.entity.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {

    Optional<Subscriber> findByUsernameAndDomain(String username, String domain);

    Page<Subscriber> findByUsernameContaining(String keyword, Pageable pageable);

    @Query("SELECT s.domain, COUNT(s) FROM Subscriber s GROUP BY s.domain")
    List<Object[]> countByDomain();
}
