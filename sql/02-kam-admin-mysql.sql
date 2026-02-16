-- kam-admin 模块：索引优化 + 租户表（MySQL）
-- 在 Kamailio schema 导入后执行；若索引已存在可忽略报错

-- 一、推荐索引（加速 Dashboard 常用查询）
ALTER TABLE location ADD INDEX idx_username (username);
ALTER TABLE acc ADD INDEX idx_time (time);
ALTER TABLE sip_trace ADD INDEX idx_callid (callid);
ALTER TABLE sip_trace ADD INDEX idx_time_stamp (time_stamp);

-- 二、多租户表（本模块自有）
CREATE TABLE IF NOT EXISTS tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_code VARCHAR(64) NOT NULL COMMENT '租户编码',
    name VARCHAR(128) NOT NULL COMMENT '租户名称',
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, DISABLED',
    rpc_url VARCHAR(256) NULL COMMENT 'Kamailio RPC 地址',
    db_url VARCHAR(512) NULL COMMENT '数据源 URL',
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    UNIQUE KEY uk_tenant_code (tenant_code)
) COMMENT '租户表';

INSERT IGNORE INTO tenant (tenant_code, name, status, created_at, updated_at)
VALUES ('default', '默认租户', 'ACTIVE', NOW(), NOW());
