package io.kamailio.admin.modules.lcr.repository;

import io.kamailio.admin.modules.lcr.entity.LcrRuleTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LcrRuleTargetRepository extends JpaRepository<LcrRuleTarget, Integer> {
}
