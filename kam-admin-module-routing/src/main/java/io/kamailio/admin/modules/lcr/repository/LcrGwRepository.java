package io.kamailio.admin.modules.lcr.repository;

import io.kamailio.admin.modules.lcr.entity.LcrGw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LcrGwRepository extends JpaRepository<LcrGw, Integer> {
    Page<LcrGw> findByGwNameContaining(String keyword, Pageable pageable);
}
