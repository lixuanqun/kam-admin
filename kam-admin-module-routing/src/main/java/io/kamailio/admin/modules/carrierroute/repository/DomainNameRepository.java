package io.kamailio.admin.modules.carrierroute.repository;

import io.kamailio.admin.modules.carrierroute.entity.DomainName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainNameRepository extends JpaRepository<DomainName, Integer> {
}
