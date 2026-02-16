package io.kamailio.admin.modules.drouting.repository;

import io.kamailio.admin.modules.drouting.entity.DrGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrGatewayRepository extends JpaRepository<DrGateway, Integer> {
    Page<DrGateway> findByGwidContaining(String keyword, Pageable pageable);
}
