package io.kamailio.admin.modules.dialplan.repository;

import io.kamailio.admin.modules.dialplan.entity.Dialplan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialplanRepository extends JpaRepository<Dialplan, Integer> {
    Page<Dialplan> findByDpid(Integer dpid, Pageable pageable);
}
