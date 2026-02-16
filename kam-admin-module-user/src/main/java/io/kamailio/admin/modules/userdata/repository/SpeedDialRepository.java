package io.kamailio.admin.modules.userdata.repository;

import io.kamailio.admin.modules.userdata.entity.SpeedDial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeedDialRepository extends JpaRepository<SpeedDial, Integer> {
    Page<SpeedDial> findByUsernameContaining(String keyword, Pageable pageable);
}
