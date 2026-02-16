-- kam-admin：索引优化 + 租户表（与 sql/02-kam-admin-mysql.sql 一致）
USE kamailio;

ALTER TABLE location ADD INDEX idx_username (username);
ALTER TABLE acc ADD INDEX idx_time (time);
ALTER TABLE sip_trace ADD INDEX idx_callid (callid);
ALTER TABLE sip_trace ADD INDEX idx_time_stamp (time_stamp);

CREATE TABLE IF NOT EXISTS tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_code VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    rpc_url VARCHAR(256) NULL,
    db_url VARCHAR(512) NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    UNIQUE KEY uk_tenant_code (tenant_code)
);

INSERT IGNORE INTO tenant (tenant_code, name, status, created_at, updated_at)
VALUES ('default', '默认租户', 'ACTIVE', NOW(), NOW());
