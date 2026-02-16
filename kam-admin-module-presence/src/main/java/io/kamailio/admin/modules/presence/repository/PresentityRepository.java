package io.kamailio.admin.modules.presence.repository;

import io.kamailio.admin.modules.presence.entity.Presentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentityRepository extends JpaRepository<Presentity, Integer> {
    Page<Presentity> findByUsernameContaining(String keyword, Pageable pageable);
}
