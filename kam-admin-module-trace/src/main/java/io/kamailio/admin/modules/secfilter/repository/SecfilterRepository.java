package io.kamailio.admin.modules.secfilter.repository;

import io.kamailio.admin.modules.secfilter.entity.Secfilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecfilterRepository extends JpaRepository<Secfilter, Integer> {
    Page<Secfilter> findByDataContaining(String keyword, Pageable pageable);
}
