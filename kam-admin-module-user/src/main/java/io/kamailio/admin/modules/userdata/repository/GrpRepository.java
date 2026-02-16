package io.kamailio.admin.modules.userdata.repository;

import io.kamailio.admin.modules.userdata.entity.Grp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrpRepository extends JpaRepository<Grp, Integer> {
    Page<Grp> findByUsernameContaining(String keyword, Pageable pageable);
}
