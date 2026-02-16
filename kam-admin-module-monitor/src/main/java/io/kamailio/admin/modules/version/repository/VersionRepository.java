package io.kamailio.admin.modules.version.repository;

import io.kamailio.admin.modules.version.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, String> {
    Optional<Version> findByTableName(String tableName);
}
