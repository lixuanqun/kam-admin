# 架构设计：对外 API、集群、Nacos、Redis

本文档针对以下四个目标给出方案与落地要点：

1. **对外提供 API**：开放接口、认证、限流、版本化  
2. **集群模式**：无状态、多实例、负载均衡  
3. **集成 Nacos**：配置中心、服务发现  
4. **集成 Redis**：缓存、会话、分布式锁  

---

## 一、对外提供 API

### 1.1 目标

- 对**内**：现有控制台前端调用的管理 API（/api/*）  
- 对**外**：给第三方/脚本/其他系统调用的**开放 API**，需认证、限流、可版本化  

### 1.2 方案要点

| 项 | 建议 |
|----|------|
| **路径区分** | 对内：`/api/*`；对外：`/open/v1/*` 或 ` /open-api/v1/*` |
| **认证** | 对外 API：API Key（Header `X-API-Key`）或 OAuth2 客户端凭证；对内可继续 Session/前端 Token |
| **限流** | 基于 Redis 的滑动窗口/令牌桶，按 API Key 或 IP 限流 |
| **版本** | URL 版本：`/open/v1/...`，便于后续 v2 并存 |
| **文档** | 对外 OpenAPI 单独分组或 tag，与对内 API 区分 |

### 1.3 实现思路

- **开放 API 控制器**：新建包 `io.kamailio.admin.open.api`，所有接口前缀 `@RequestMapping("/open/v1")`，统一走开放 API 的拦截器/过滤器。  
- **认证**：  
  - 拦截 `/open/**` 请求，校验 Header `X-API-Key`（或 Bearer Token），与 Nacos/DB/Redis 中配置的 Key 比对。  
  - 可选：接入 Spring Security，对 `/open/**` 使用 ApiKey 或 OAuth2 Resource Server。  
- **限流**：  
  - 使用 Redis 存储计数与时间窗口（或集成 Redisson、Spring Data Redis + 自定义注解）。  
  - 按 `apiKey` 或 `ip` 维度限流（如 100 次/分钟）。  
- **与现有 API 的关系**：  
  - 对内 `/api/*` 保持不变；  
  - 对外 `/open/v1/*` 可复用现有 Service，仅 Controller 层和认证/限流不同，或单独写一层薄 DTO 与接口。

---

## 二、集群模式

### 2.1 目标

- 支持 **kam-admin-server 多实例** 部署，前面挂负载均衡（Nginx/云 LB）。  
- 无单点，配置与状态不依赖单机内存（配置进 Nacos，会话/缓存进 Redis）。  

### 2.2 无状态设计

| 项 | 说明 |
|----|------|
| **不存会话在内存** | 若有“登录态”，存 Redis 或 JWT；不依赖 Sticky Session。 |
| **配置外置** | 数据库、Kamailio RPC 地址等从 Nacos 配置中心读取，各实例一致。 |
| **缓存外置** | 公共缓存（如 Kamailio 状态、接口限流计数）放 Redis，不放在单机内存。 |
| **任务与锁** | 若有定时任务（如清理、同步），用 Redis 分布式锁或 Nacos/DB 选主，避免多实例重复执行。 |

### 2.3 部署示意

```
                    [Nginx / 云 LB]
                           |
        +------------------+------------------+
        |                  |                  |
   [kam-admin-server] [kam-admin-server] [kam-admin-server]
        |                  |                  |
        +------------------+------------------+
        |         Nacos（配置 + 服务发现）        |
        |         Redis（缓存 / 会话 / 限流）      |
        |         MySQL（业务数据）               |
```

- 各实例**不**做 Sticky Session，LB 轮询或按最少连接即可。  
- Kamailio 侧若有多节点，可在 Nacos 中配置多个 RPC 地址，由应用侧做简单负载（或后续接入服务发现）。

---

## 三、集成 Nacos

### 3.1 角色

- **配置中心**：数据库 URL、Redis 连接、Kamailio RPC 地址、开放 API 的 Key 列表、限流阈值等放 Nacos，支持动态刷新（如 `@RefreshScope`）。  
- **服务发现（可选）**：kam-admin-server 注册到 Nacos，便于 LB 或网关做服务发现；若暂时用 Nginx 配死 IP/域名，可先只上配置中心。  

### 3.2 依赖与配置

- **依赖**（在 kam-admin-server 或父 POM 中）：  
  - `spring-cloud-starter-alibaba-nacos-config`  
  - `spring-cloud-starter-alibaba-nacos-discovery`（需要服务发现时）  
- **BOM**：使用 `spring-cloud-alibaba` 的 BOM 统一版本（与 Spring Boot 3.x 兼容的版本）。  

- **bootstrap 配置**（Nacos 建议用 bootstrap 拉取配置）：  
  - `bootstrap.yml` 或 `bootstrap.properties`：  
    - `spring.application.name=kam-admin-server`  
    - `spring.cloud.nacos.config.server-addr=...`  
    - `spring.cloud.nacos.config.namespace=...`（可选）  
    - `spring.cloud.nacos.config.file-extension=yaml`  
  - 拉取到的配置可覆盖本地 `application.yml` 中的 `spring.datasource`、`spring.data.redis`、自定义 `kamailio` 等。  

- **服务发现**（可选）：  
  - `spring.cloud.nacos.discovery.server-addr` 与 namespace。  
  - 启用 `@EnableDiscoveryClient`（或自动装配）。  

### 3.3 配置项建议放 Nacos 的内容

- `spring.datasource.*`（或对应 DataSource 配置）  
- `spring.data.redis.*`  
- `kamailio.rpc.*`（或每个 Kamailio 节点一个列表，供集群）  
- 开放 API：`open-api.keys`（API Key 列表或加密存储的密钥）、`open-api.rate-limit-per-minute`  
- 业务开关：如功能开关、清理任务 cron 等  

---

## 四、集成 Redis

### 4.1 用途

| 用途 | 说明 |
|------|------|
| **缓存** | 对 Kamailio RPC 结果、统计结果做短时缓存，减少对 Kamailio 和 DB 压力；集群下多实例共享。 |
| **限流** | 开放 API 限流计数存 Redis（key 如 `ratelimit:open:${apiKey}:${minute}`），滑动窗口或固定窗口。 |
| **会话** | 若登录态存服务端，用 Redis Session 存储，实现无状态网关 + 有状态 Session。 |
| **分布式锁** | 定时任务、批量操作时用 Redis 锁，避免多实例重复执行。 |
| **可选** | 集群下 Kamailio 节点列表/状态缓存、发布订阅（如配置变更通知） |

### 4.2 依赖与配置

- **依赖**：`spring-boot-starter-data-redis`，可选 `redisson` 或 `jedis`（Lettuce 为 Spring 默认）。  
- **配置**：  
  - `spring.data.redis.host`、`port`、`password`、`database`  
  - 建议从 Nacos 读取，与集群多实例一致。  
- **序列化**：建议 Key 用 String，Value 用 JSON（如 Jackson2JsonRedisSerializer），便于排查与兼容多语言。  

### 4.3 使用方式（建议）

- **缓存**：`@Cacheable` + `CacheManager` 配置 Redis 为后端；或手写 `RedisTemplate` 封装常用缓存方法。  
- **限流**：自定义注解 + AOP，在 Redis 中 incr/expire 实现；或 Redisson 的 `RRateLimiter`。  
- **分布式锁**：`RedisTemplate.setIfAbsent` + 过期时间，或 Redisson 的 `RLock`。  

---

## 五、与现有工程的结合方式

### 5.1 模块与配置

- **kam-admin-server**：  
  - 增加依赖：`spring-cloud-starter-alibaba-nacos-config`、`spring-cloud-starter-alibaba-nacos-discovery`（可选）、`spring-boot-starter-data-redis`。  
  - 增加 `bootstrap.yml` 拉取 Nacos 配置。  
  - 保留本地 `application.yml` 作为默认值，Nacos 中配置可覆盖。  
- **kam-admin-common**：  
  - 若有“开放 API”的公共 DTO、限流注解、异常码，可放在 common，供 server 与后续网关/SDK 复用。  

### 5.2 配置优先级建议

1. Nacos 远程配置（高优先级，可动态刷新）  
2. 本地 `application.yml` / `application-{profile}.yml`  
3. 环境变量  

### 5.3 开放 API 与集群的衔接

- 对外 API：`/open/v1/*`，认证 + Redis 限流，无状态。  
- 集群：多实例无 Sticky，配置与缓存依赖 Nacos + Redis。  
- 对内 API：`/api/*` 可继续现有实现；若将来要对内也做限流或审计，可复用同一套 Redis 与 Nacos 配置能力。  

---

## 六、实施顺序建议

1. **集成 Redis**：在 kam-admin-server 中引入 Redis，先做简单缓存或限流试点，确认连接与序列化无误。  
2. **集成 Nacos 配置中心**：引入 Nacos Config，将 DB、Redis、Kamailio 等配置迁到 Nacos，本地保留默认值，实现配置外置与动态刷新。  
3. **集群验证**：启动 2 个及以上实例，前面挂 LB，验证无状态与配置一致性；会话/缓存走 Redis。  
4. **对外 API**：新增 `/open/v1` 控制器与认证、Redis 限流，文档与对内 API 区分。  
5. **Nacos 服务发现**（可选）：注册 kam-admin-server，由网关或 LB 通过 Nacos 发现实例。  

---

## 七、小结

| 需求 | 要点 |
|------|------|
| 对外 API | `/open/v1/*`、API Key 或 OAuth2、Redis 限流、OpenAPI 文档分组 |
| 集群模式 | 无状态、配置与缓存外置（Nacos + Redis）、多实例 + LB |
| 集成 Nacos | 配置中心必选；服务发现可选；bootstrap + Nacos 拉取配置 |
| 集成 Redis | 缓存、限流、会话、分布式锁；配置从 Nacos 读取 |

---

## 八、本仓库已做改动（可直接在此基础上继续）

- **依赖**：父 POM 已加入 Spring Cloud、Spring Cloud Alibaba BOM；kam-admin-server 已加入 Nacos Config、Nacos Discovery、Bootstrap、Redis 依赖。  
- **配置**：已新增 `bootstrap.yml`（Nacos 地址/命名空间占位）；`application.yml` 已加入 Redis 与开放 API 占位（`open-api.enabled`、`open-api.rate-limit-per-minute`）。  
- **Redis**：已新增 `RedisConfig`，Key 为 String、Value 为 JSON。  
- **开放 API**：已新增包 `io.kamailio.admin.open.api` 与示例控制器 `OpenApiV1Controller`（前缀 `/open/v1`，示例接口 `GET /open/v1/health`）。**鉴权**：已通过 `AuthFilter` 与 `ApiKeyAuthResolver` 对 `/open/*` 做 API Key 校验（配置 `open-api.keys`）；限流可在此基础上用 Redis 实现，见上文第一节。  

**本地开发**：若暂不启用 Nacos/Redis，可设置环境变量 `NACOS_SERVER_ADDR` 为空或禁用 Nacos 自动装配，并确保 Redis 可连接（或使用 `spring.autoconfigure.exclude` 排除 Redis 自动配置后自行提供 NoOp 实现）。
