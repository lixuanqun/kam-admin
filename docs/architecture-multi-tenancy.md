# 多租户架构设计

本文档描述 kam-admin 的**多租户体系**：租户模型、租户识别、数据与 RPC 隔离方式及实施要点。

---

## 一、目标与范围

- **租户**：一个独立使用 Dashboard 的客户/组织（如企业、运营商），其下数据与 Kamailio 配置相互隔离。
- **多租户能力**：同一套应用支持多租户；请求进入时识别当前租户，后续数据访问与 RPC 调用按该租户隔离。

---

## 二、租户识别方式

| 方式 | 说明 | 优先级（可配置） |
|------|------|------------------|
| **Header** | `X-Tenant-Id` 或 `X-Tenant-Code`，适合 API/开放接口 | 高 |
| **子域名** | 如 `tenant-a.dashboard.example.com` → 租户 code=tenant-a | 中 |
| **JWT / Session** | 登录态中携带 tenant_id，控制台使用 | 高 |
| **默认租户** | 未识别时使用配置的 default-tenant（单租户兼容） | 兜底 |

实现上由 **TenantResolver** 按配置顺序解析，结果写入 **TenantContext**（ThreadLocal），供 Service 与数据层使用。

---

## 三、数据隔离模式

### 3.1 当前实现状态（必读）

- **已实现**：**按租户 RPC 隔离**。请求进入后根据 TenantContext 选择该租户的 `rpc_url`，通过 **TenantAwareKamailioRpcService** 路由到对应 Kamailio 实例的 JSON-RPC；未配置 `rpc_url` 的租户使用全局默认 RPC。
- **未实现**：**按租户切换数据源**。当前 **DataSource 为单实例共享**，所有租户共用同一数据库（同一 schema），未根据 `tenant` 表的 `db_url` 或 schema 做动态 DataSource 路由。
- 因此现阶段为「**RPC 多租户 + 共享库**」；若需模式 A 的完整数据隔离（每租户独立 DB/schema），需在后续迭代中实现按 TenantContext 的 DataSource 路由与连接池管理。

### 3.2 模式 A：租户独立数据源（目标架构）

- 每个租户配置自己的 **Kamailio RPC 地址** 与 **数据库连接**（或共用 DB 但不同 **schema**）。
- 应用内无 `tenant_id` 列：同一张表在不同租户的 DB/schema 中各有一份。
- **优点**：隔离彻底、与 Kamailio 官方表结构一致、可对接“一租户一 Kamailio 实例”。
- **缺点**：需维护租户→连接映射，动态 DataSource/RPC 客户端略复杂。

### 3.3 模式 B：共享库 + tenant_id

- 单库单 schema，业务表增加 **tenant_id**，所有查询带 `WHERE tenant_id = ?`。
- **优点**：实现简单、易做跨租户统计。
- **缺点**：需改 Kamailio 相关表或仅对“应用自有表”加 tenant_id；若 Kamailio 表不改，则无法用此模式隔离 Kamailio 数据。

### 3.4 建议

- **当前**：保持「RPC 按租户、DB 共享」；租户表 `tenant` 中可配置 `rpc_url`，`db_url` 保留为扩展字段。
- **后续**：若需模式 A，再实现按 TenantContext 选择 DataSource（或 schema），并做好连接池与健康检查。
- **应用自有表**（如审计、API Key、配置缓存）可采用模式 B，在表中加 `tenant_id`，与现有鉴权体系结合。

---

## 四、核心组件

| 组件 | 职责 |
|------|------|
| **Tenant** | 租户实体：id、code、name、status、rpc_url、db_url（可选）等 |
| **TenantContext** | 当前请求的租户 ID/code（ThreadLocal），请求结束清理 |
| **TenantResolver** | 从 Header/子域/JWT 解析出当前租户并写入 TenantContext |
| **TenantService** | 租户 CRUD、按 code/id 查询、校验租户是否有效 |
| **TenantFilter / Interceptor** | 在请求入口调用 TenantResolver，在响应出口清理 TenantContext |
| **数据源/RPC 路由** | 根据 TenantContext 选择 DataSource 与 KamailioRpcService（动态或池化） |

---

## 五、配置项建议

```yaml
multi-tenant:
  enabled: true
  default-tenant-code: default   # 未识别时使用的租户 code
  resolve-order: header,subdomain,jwt,session  # 解析顺序
  header-tenant-id: X-Tenant-Id
  header-tenant-code: X-Tenant-Code
  subdomain-pattern: "^([a-z0-9-]+)\\.dashboard\\.example\\.com$"  # 可选
```

---

## 六、与鉴权的关系

- 鉴权先于或与租户解析同层：先识别用户/API Key，再解析租户（可从 JWT/Session 取 tenant_id，或从 Header 取）。
- 权限可细化为“某用户仅能访问某租户”：在鉴权结果中带 tenant_id，与 TenantContext 校验一致方可访问。

---

## 七、实施顺序

1. **租户表与上下文**：建 `tenant` 表、TenantContext、TenantResolver、TenantFilter，配置 default-tenant。
2. **请求链路**：所有需隔离的 API 经 TenantFilter 设置 TenantContext；开放 API 支持通过 Header 传租户。
3. **数据源/RPC 按租户路由**：根据 TenantContext 选择对应 DataSource 与 KamailioRpcService（可先支持“每租户独立 rpc_url”，DB 仍共享或后续再接独立 DB/schema）。
4. **控制台登录与 JWT**：登录时绑定租户，JWT 中写入 tenant_id，后续请求从 JWT 解析租户。

---

## 八、本仓库已做改动（可直接在此基础上继续）

- **设计文档**：本文档。
- **租户实体**：`Tenant` 表与 JPA 实体（`tenant` 表）、TenantRepository、TenantService；建表脚本见 **sql/03-tenant-table.sql**（MySQL）、**sql/03-tenant-table-postgres.sql**（PostgreSQL），含默认租户 `default`。
- **租户上下文**：TenantContext（ThreadLocal）、TenantResolver（Header：X-Tenant-Code / X-Tenant-Id；可选子域解析）、TenantFilter 注册（Order 0，请求入口解析、出口清理）。
- **子域解析**：配置 `multi-tenant.subdomain-enabled`、`multi-tenant.subdomain-suffix`（如 `.dashboard.example.com`），从 Host 提取租户 code。
- **租户管理 API**：`/api/tenants` 提供租户 CRUD 与 `GET /api/tenants/current` 查询当前租户。
- **按租户 RPC 路由**：**TenantAwareKamailioRpcService**（@Primary）根据当前租户的 `rpc_url` 选择 Kamailio RPC；未配置则走全局默认。所有注入 `KamailioRpcService` 的模块自动按租户路由。
- **默认租户初始化**：**TenantInitializer**（ApplicationRunner）在启动时若租户表为空则插入默认租户（可关闭：`multi-tenant.init-default-tenant=false`）。
- **配置**：`multi-tenant.enabled`、`default-tenant-code`、`resolve-order`、`subdomain-enabled`、`subdomain-suffix`、`init-default-tenant`。
- **数据源按租户**：Tenant 实体支持 `db_url`；按租户动态 DataSource 可在后续迭代实现。
