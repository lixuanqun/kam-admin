package io.kamailio.admin.modules.drouting.repository;

import io.kamailio.admin.modules.drouting.entity.DrGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrGroupRepository extends JpaRepository<DrGroup, Integer> {
    Page<DrGroup> findByUsernameContaining(String keyword, Pageable pageable);
}
