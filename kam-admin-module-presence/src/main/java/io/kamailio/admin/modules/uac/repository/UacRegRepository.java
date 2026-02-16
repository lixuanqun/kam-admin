package io.kamailio.admin.modules.uac.repository;

import io.kamailio.admin.modules.uac.entity.UacReg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UacRegRepository extends JpaRepository<UacReg, Integer> {
    Page<UacReg> findByLUsernameContaining(String keyword, Pageable pageable);
}
