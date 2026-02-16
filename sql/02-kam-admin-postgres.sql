-- kam-admin 模块：索引优化 + 租户表（PostgreSQL）
-- 在 Kamailio schema 导入后执行

-- 一、推荐索引
CREATE INDEX IF NOT EXISTS idx_location_username ON location(username);
CREATE INDEX IF NOT EXISTS idx_acc_time ON acc(time);
CREATE INDEX IF NOT EXISTS idx_sip_trace_callid ON sip_trace(callid);
CREATE INDEX IF NOT EXISTS idx_sip_trace_time_stamp ON sip_trace(time_stamp);

-- 二、多租户表
CREATE TABLE IF NOT EXISTS tenant (
    id BIGSERIAL PRIMARY KEY,
    tenant_code VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    rpc_url VARCHAR(256) NULL,
    db_url VARCHAR(512) NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    CONSTRAINT uk_tenant_code UNIQUE (tenant_code)
);

COMMENT ON TABLE tenant IS '租户表';

INSERT INTO tenant (tenant_code, name, status, created_at, updated_at)
VALUES ('default', '默认租户', 'ACTIVE', NOW(), NOW())
ON CONFLICT (tenant_code) DO NOTHING;
