package io.kamailio.admin.modules.userdata.repository;

import io.kamailio.admin.modules.userdata.entity.DbAliases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbAliasesRepository extends JpaRepository<DbAliases, Integer> {
    Page<DbAliases> findByAliasUsernameContaining(String keyword, Pageable pageable);
}
