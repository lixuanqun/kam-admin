package io.kamailio.admin.modules.acc.repository;

import io.kamailio.admin.modules.acc.entity.Acc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccRepository extends JpaRepository<Acc, Integer>, JpaSpecificationExecutor<Acc> {}
