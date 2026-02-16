package io.kamailio.admin.modules.version.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "version")
public class Version {
    @Id
    @Column(name = "table_name", length = 32)
    private String tableName;
    @Column(name = "table_version", nullable = false)
    private Integer tableVersion = 0;

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public Integer getTableVersion() { return tableVersion; }
    public void setTableVersion(Integer tableVersion) { this.tableVersion = tableVersion; }
}
