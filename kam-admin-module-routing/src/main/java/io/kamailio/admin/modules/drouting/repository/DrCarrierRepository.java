package io.kamailio.admin.modules.drouting.repository;

import io.kamailio.admin.modules.drouting.entity.DrCarrier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrCarrierRepository extends JpaRepository<DrCarrier, Integer> {
    Page<DrCarrier> findByCarrieridContaining(String keyword, Pageable pageable);
}
