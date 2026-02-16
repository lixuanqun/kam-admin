package io.kamailio.admin.modules.lcr.repository;

import io.kamailio.admin.modules.lcr.entity.LcrRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LcrRuleRepository extends JpaRepository<LcrRule, Integer> {
    Page<LcrRule> findByPrefixContaining(String keyword, Pageable pageable);
}
