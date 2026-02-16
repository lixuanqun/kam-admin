package io.kamailio.admin.modules.mtree.repository;

import io.kamailio.admin.modules.mtree.entity.Mtree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MtreeRepository extends JpaRepository<Mtree, Integer> {
    Page<Mtree> findByTprefixContaining(String keyword, Pageable pageable);
    Page<Mtree> findByTname(String tname, Pageable pageable);
}
