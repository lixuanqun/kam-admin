# 架构设计：Common 下沉、按领域拆模块、对外 API、多种鉴权

本文档统一说明以下四点的方案与实施顺序：

1. **Common 下沉**：将 DTO、异常、RPC 接口等迁到 **kam-admin-common**
2. **按领域拆 Maven 模块**：按业务域拆成 **kam-admin-module-xxx**，server 只做组装
3. **对外提供 API**：开放接口 `/open/v1/*`，与对内 `/api/*` 区分
4. **多种鉴权方式**：支持 API Key、Session、JWT、OAuth2 等可插拔鉴权

---

## 一、Common 下沉

### 1.1 目标

- **kam-admin-common** 收拢：通用 DTO、分页模型、异常/错误码、RPC 接口定义（无实现）。
- **kam-admin-server** 仅保留：启动类、配置、RPC 实现、Web 层（含全局异常处理）、以及尚未拆到领域模块的代码。

### 1.2 迁入 common 的内容

| 内容 | 说明 |
|------|------|
| **dto** | `ApiResponse`、`PaginationDto`、`PaginatedResult`（与业务无关的统一响应/分页） |
| **exception** | 业务异常基类或错误码枚举（如 `BusinessException`、`ErrorCodes`），供 server 的 `GlobalExceptionHandler` 使用 |
| **service 接口** | `KamailioRpcService` 的**接口**（方法签名，返回 `Mono<?>`），实现留在 server |

### 1.3 保留在 server 的内容

- **GlobalExceptionHandler**：依赖 Spring MVC，保留在 server，仅引用 common 的 `ApiResponse`、异常类。
- **KamailioRpcService 实现类**：依赖 `KamailioProperties`、`WebClient`，留在 server，实现 common 中的接口。
- **config**、各 **modules**：仍在 server，直至按领域拆出到独立模块。

### 1.4 common 依赖原则

- common 不依赖 Spring Boot Web、不依赖 Nacos/Redis；可依赖 `jakarta.validation`、`swagger-annotations`、`reactor-core`（为 RPC 接口的 `Mono` 返回值）。

---

## 二、按领域拆 Maven 模块

### 2.1 领域与模块映射（与 kam-admin-server-split-design 一致）

| 子模块 | 职责 | 包含的 modules（示例） |
|--------|------|------------------------|
| **kam-admin-module-user** | 用户与鉴权相关 | subscriber, domain, permissions, userdata, usrpreferences |
| **kam-admin-module-routing** | 路由与调度 | dispatcher, drouting, lcr, carrierroute, dialplan, mtree, pdt |
| **kam-admin-module-session** | 会话与缓存 | dialog, htable, location |
| **kam-admin-module-monitor** | 监控与统计 | monitoring, acc, version, system |
| **kam-admin-module-presence** | 状态与消息 | presence, msilo, uac |
| **kam-admin-module-trace** | 跟踪与安全 | siptrace, topos, secfilter, rtpengine |

### 2.2 依赖关系

- 各 **kam-admin-module-xxx** 只依赖 **kam-admin-common**（以及 JPA/Redis 等 Spring Boot 技术依赖，由 server 的 BOM 管理）。
- **kam-admin-server** 依赖所有 module，负责启动、配置、Web 注册、组件扫描各模块。

```
kam-admin-common
       ↑
       │ 依赖
       │
kam-admin-module-user / routing / session / monitor / presence / trace
       ↑
       │ 依赖
       │
kam-admin-server（启动 + config + 开放 API + 鉴权）
```

### 2.3 实施方式

- **第一步**：新增 Maven 模块（如先做 **kam-admin-module-user**），将对应包从 server 迁入该模块（entity、repository、service、controller 等）。
- **第二步**：在 server 的 `pom.xml` 中依赖该模块，并确保组件扫描能扫到（如 `@ComponentScan` 或各模块的自动配置）。
- **第三步**：按表逐步将其余领域迁出，保持单次改动可编译、可运行。

---

## 三、对外提供 API

### 3.1 路径与职责

- **对内**：`/api/*`，供控制台前端使用，鉴权方式可为 Session、JWT 等。
- **对外**：`/open/v1/*`，供第三方/脚本/其他系统调用，鉴权方式通常为 **API Key** 或 **OAuth2 客户端凭证**，并配合限流（如 Redis）。

详见 **docs/architecture-api-cluster-nacos-redis.md**。

### 3.2 与 common / 领域模块的关系

- 对外 API 的 Controller 可放在 **kam-admin-server** 的 `open.api` 包下，复用各领域模块的 Service（或后续的 **kam-admin-api** 门面）。
- 对外、对内 API 均通过统一的**鉴权抽象**选择具体鉴权方式（见第四节）。

---

## 四、多种鉴权方式

### 4.1 目标

- 支持多种鉴权方式并存，按**请求路径**或**配置**选择：  
  **API Key**（开放 API）、**Session**（控制台登录）、**JWT**（Bearer Token）、**OAuth2**（如客户端凭证、Resource Owner Password 等）。
- 鉴权逻辑可插拔，便于扩展新方式（如 LDAP、SAML）。

### 4.2 设计要点

| 鉴权方式 | 适用场景 | 识别方式 | 存储/校验 |
|----------|----------|----------|-----------|
| **API Key** | 开放 API `/open/**` | Header `X-API-Key` 或 `Authorization: Bearer <key>` | 配置/DB/Redis 中维护 Key 与权限 |
| **Session** | 控制台 `/api/**` | Cookie 中的 SessionId | Redis 或服务端 Session 存储 |
| **JWT** | 控制台或移动端 `/api/**` | Header `Authorization: Bearer <jwt>` | 校验签名与过期时间，可选 Redis 黑名单 |
| **OAuth2** | 第三方集成 | `Authorization: Bearer <access_token>` | 通过 OAuth2 授权服务校验 token |

### 4.3 抽象与路由

- **AuthMethod**：枚举或策略标识（API_KEY / SESSION / JWT / OAUTH2）。
- **AuthRequest**：请求上下文（路径、Header、Cookie、Token 等）。
- **AuthResult**：鉴权结果（成功则带 Principal/用户标识；失败则带 401/403 与原因）。
- **AuthResolver**：每种鉴权方式一个实现（如 `ApiKeyAuthResolver`、`SessionAuthResolver`、`JwtAuthResolver`、`OAuth2AuthResolver`）。
- **AuthFilter / AuthHandler**：  
  - 根据请求路径或配置决定使用的 **AuthMethod**（如 `/open/**` → API_KEY，`/api/**` → SESSION 或 JWT）。  
  - 调用对应 **AuthResolver**，将 **AuthResult** 写入请求属性或 SecurityContext，供后续 Controller 使用。

### 4.4 与 Spring Security 的关系

- **可选一**：不用 Spring Security，自行实现 Filter + AuthResolver 链，轻量、易控。
- **可选二**：接入 Spring Security，对 `/open/**` 配置 ApiKey 或 OAuth2 Resource Server，对 `/api/**` 配置 Session/JWT，通过多个 `SecurityFilterChain` 按路径区分。

本方案先采用 **Filter + AuthResolver** 抽象，便于与现有无 Security 的工程平滑集成；后续可再迁到 Spring Security。

### 4.5 配置建议

- 在 **application.yml** 或 Nacos 中配置：  
  - 各路径前缀对应的默认鉴权方式（如 `open-api.auth-method=api_key`，`console.auth-method=session` 或 `jwt`）。  
  - 各鉴权方式所需参数（API Key 列表、JWT 签发/校验密钥、OAuth2 端点等）。

---

## 五、实施顺序建议

1. **Common 下沉**（先做）  
   - 将 DTO、异常、`KamailioRpcService` 接口迁入 kam-admin-common；server 实现接口并引用 common。  

2. **多种鉴权方式**（与对外 API 配套）  
   - 在 server 中新增 auth 包：AuthMethod、AuthResult、AuthResolver 接口及各实现（ApiKey、Session、JWT、OAuth2 占位）；  
   - 统一 Filter 根据路径选择鉴权方式，对 `/open/v1/*` 使用 API Key。  

3. **按领域拆 Maven 模块**（渐进）  
   - 先新增 **kam-admin-module-user**（或选一个最小领域），迁出对应包；server 依赖并扫描；  
   - 再按表将其余领域拆出。  

4. **对外 API**（已有骨架）  
   - 保持 `/open/v1/*`，与鉴权、限流（Redis）结合；按需增加接口与 OpenAPI 文档。  

---

## 六、小结

| 需求 | 做法 |
|------|------|
| Common 下沉 | DTO、异常、RPC 接口定义迁入 kam-admin-common；实现与 Web 层留在 server |
| 按领域拆 Maven 模块 | 新增 kam-admin-module-xxx，按领域表迁移代码；server 依赖并扫描 |
| 对外 API | `/open/v1/*`，复用领域 Service，配合鉴权与限流 |
| 多种鉴权 | AuthResolver 抽象 + 按路径选择 API Key / Session / JWT / OAuth2，Filter 统一入口 |

---

## 七、本仓库已做改动（可直接在此基础上继续）

- **Common 下沉**：已将 `ApiResponse`、`PaginationDto`、`PaginatedResult`、`BusinessException`、`KamailioRpcService` 接口迁入 **kam-admin-common**；server 中保留 `GlobalExceptionHandler`，RPC 实现为 **KamailioRpcServiceImpl**（`io.kamailio.admin.rpc`）。
- **多种鉴权**：在 server 中新增 **auth** 包：`AuthMethod`、`AuthResult`、`AuthResolver` 接口，以及 `ApiKeyAuthResolver`、`SessionAuthResolver`、`JwtAuthResolver`、`OAuth2AuthResolver`（后三者为占位）；**AuthFilter** 按路径选择鉴权方式（`/open/*` → API_KEY，`/api/*` → SESSION），配置项 `auth.open-api.method`、`auth.console.method`、`open-api.keys`。
- **按领域拆模块**：已新增 **kam-admin-module-user**，当前为占位包 `io.kamailio.admin.modules.user`；server 已依赖该模块，后续可将 subscriber、domain、permissions、userdata、usrpreferences 迁入此模块。
- **对外 API**：保持 `/open/v1/*` 与 `OpenApiV1Controller`，开放接口走 API Key 鉴权（需配置 `open-api.keys`）。

后续可在本仓库中按上述顺序落地：先完成 common 下沉与鉴权骨架，再逐步拆领域模块并完善对外 API。
