package io.kamailio.admin.modules.pdt.repository;

import io.kamailio.admin.modules.pdt.entity.Pdt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdtRepository extends JpaRepository<Pdt, Integer> {
    Page<Pdt> findByPrefixContaining(String keyword, Pageable pageable);
}
