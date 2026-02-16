package io.kamailio.admin.modules.domain.repository;

import io.kamailio.admin.modules.domain.entity.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Integer> {
    Optional<Domain> findByDomain(String domain);
    Page<Domain> findByDomainContaining(String keyword, Pageable pageable);
}
