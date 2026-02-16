package io.kamailio.admin.modules.htable.repository;

import io.kamailio.admin.modules.htable.entity.Htable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HtableRepository extends JpaRepository<Htable, Integer> {
    Page<Htable> findByKeyNameContaining(String keyword, Pageable pageable);
}
