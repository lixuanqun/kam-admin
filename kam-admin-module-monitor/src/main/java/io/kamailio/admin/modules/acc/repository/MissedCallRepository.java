package io.kamailio.admin.modules.acc.repository;

import io.kamailio.admin.modules.acc.entity.MissedCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MissedCallRepository extends JpaRepository<MissedCall, Integer>, JpaSpecificationExecutor<MissedCall> {}
