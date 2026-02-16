package io.kamailio.admin.modules.permissions.repository;

import io.kamailio.admin.modules.permissions.entity.Trusted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrustedRepository extends JpaRepository<Trusted, Integer> {
    Page<Trusted> findBySrcIpContaining(String keyword, Pageable pageable);
}
