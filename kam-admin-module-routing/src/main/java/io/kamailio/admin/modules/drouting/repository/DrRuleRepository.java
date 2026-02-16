package io.kamailio.admin.modules.drouting.repository;

import io.kamailio.admin.modules.drouting.entity.DrRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrRuleRepository extends JpaRepository<DrRule, Integer> {
    Page<DrRule> findByPrefixContaining(String keyword, Pageable pageable);
}
