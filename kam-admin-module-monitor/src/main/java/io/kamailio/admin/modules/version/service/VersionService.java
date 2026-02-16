package io.kamailio.admin.modules.version.service;

import io.kamailio.admin.modules.version.entity.Version;
import io.kamailio.admin.modules.version.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VersionService {
    private final VersionRepository repository;

    public VersionService(VersionRepository repository) { this.repository = repository; }

    public List<Version> findAll() {
        return repository.findAll(org.springframework.data.domain.Sort.by("tableName"));
    }

    public Integer getVersion(String tableName) {
        return repository.findByTableName(tableName).map(Version::getTableVersion).orElse(null);
    }

    public Map<String, Object> getStats() {
        var tables = repository.findAll();
        var list = tables.stream().map(t -> Map.<String, Object>of("name", t.getTableName(), "version", t.getTableVersion())).toList();
        return Map.of("totalTables", tables.size(), "tables", list);
    }
}
