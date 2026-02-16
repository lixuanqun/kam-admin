# kam-admin-server 拆分细化思路

当前 server 是单体应用：一个 Spring Boot 进程内包含 **config**、**rpc**（KamailioRpcService 实现）、**auth**、**open.api**、以及 26+ 个 **modules**（每个含 entity / repository / service / controller）。**common**（DTO、异常、KamailioRpcService 接口）已下沉至 **kam-admin-common**；**kam-admin-module-user** 已新增为占位，可迁入 subscriber、domain 等。下面给出几种可选的进一步拆分方式，便于按需选型或分阶段实施。

---

## 一、现状简要

- **kam-admin-common**（独立 JAR）：ApiResponse、PaginationDto、PaginatedResult、BusinessException、KamailioRpcService **接口**
- **server 内**：config（DataSource、Web、Kamailio、Redis、AuthFilter）、rpc（KamailioRpcServiceImpl）、auth（多种鉴权）、open.api、common.exception（GlobalExceptionHandler）
- **modules**：subscriber, domain, dispatcher, location, permissions, acc, monitoring, version, system, drouting, dialog, htable, lcr, carrierroute, dialplan, mtree, pdt, userdata, uac, usrpreferences, presence, msilo, siptrace, topos, secfilter, rtpengine 等

---

## 二、思路一：按“层”拆（common 下沉 + server 只做组装）

**做法**  
- 把 **common** 里可复用部分迁到 **kam-admin-common**（DTO、异常、工具）。  
- **kam-admin-server** 只保留：启动类、config、对 KamailioRpcService 的实现、以及各 module 的 controller/service/repository/entity（仍在一个应用内）。  
- 不在 Maven 上再拆更多模块，只在 server 内用包结构区分层。

**结构示意**

```
kam-admin-common（已有）
  └── dto, exception, 常量
kam-admin-server
  └── config, KamailioRpcService, modules/*（保持现状）
```

**优点**：改动小，只做“common 下沉”，依赖清晰。  
**缺点**：server 仍是“大单体”，模块边界只在包级别。

**适用**：先统一 DTO/异常，为后续拆分打基础。

---

## 三、思路二：按“领域/功能域”拆成多个 Maven 子模块（Nacos 风格）

**做法**  
- 在 **kam-admin-server** 下不再放所有代码，而是拆成多个 **Maven 子模块**（或与 server 平级的模块），按业务域划分，例如：

| 子模块 | 职责 | 包含的 modules（示例） |
|--------|------|------------------------|
| **kam-admin-module-user** | 用户与鉴权相关 | subscriber, domain, permissions, userdata, usrpreferences |
| **kam-admin-module-routing** | 路由与调度 | dispatcher, drouting, lcr, carrierroute, dialplan, mtree, pdt |
| **kam-admin-module-session** | 会话与缓存 | dialog, htable, location |
| **kam-admin-module-monitor** | 监控与统计 | monitoring, acc, version, system |
| **kam-admin-module-presence** | 状态与消息 | presence, msilo, uac |
| **kam-admin-module-trace** | 跟踪与安全 | siptrace, topos, secfilter, rtpengine |

- **kam-admin-server** 只做 **bootstrap**：启动类、全局配置、Web/DataSource、以及依赖上述各子模块并做组件扫描（或按需排除某些模块）。
- 各子模块依赖 **kam-admin-common**；若需要跨模块调用，可再抽 **kam-admin-api**（接口 + DTO）或通过事件/消息解耦。

**结构示意**

```
kam-admin/
  kam-admin-common
  kam-admin-api（可选：对外接口与 DTO）
  kam-admin-module-user
  kam-admin-module-routing
  kam-admin-module-session
  kam-admin-module-monitor
  kam-admin-module-presence
  kam-admin-module-trace
  kam-admin-server   # 只做启动 + 配置，依赖上述模块
```

**优点**：边界清晰、可按域独立演进、构建时可只编/测某一块。  
**缺点**：子模块多、依赖与打包顺序要设计好（避免循环依赖）。

**适用**：团队分工按域、或打算后续部分域独立成服务时。

---

## 四、思路三：按“技术层”拆（api / service / dal）

**做法**  
- **kam-admin-api**：Controller + 入参/出参 DTO（无业务逻辑、不依赖 JPA）。  
- **kam-admin-service**：Service 层 + KamailioRpcService，依赖 api。  
- **kam-admin-dal**：Entity、Repository、数据源相关配置，依赖 common。  
- **kam-admin-server**：启动类 + 配置，依赖 api、service、dal，组装成可运行应用。

**结构示意**

```
kam-admin-common
kam-admin-dal      # entity, repository
kam-admin-service  # service + RPC，依赖 dal, common
kam-admin-api      # controller + dto，依赖 service（接口）
kam-admin-server   # 启动 + 配置，依赖 api
```

**优点**：分层明确，api 可被其他服务或 SDK 复用。  
**缺点**：层多、包和模块边界要严格（例如禁止 api 依赖 dal），改动面大。

**适用**：计划对外提供多端/多语言 SDK 或开放 API 时。

---

## 五、思路四：混合——common 下沉 + server 内“领域包” + 可选 1～2 个独立 JAR

**做法**  
- **kam-admin-common**：继续收拢 DTO、异常、工具；可选把 **KamailioRpcService** 的接口放到 common，实现仍放在 server 或单独 rpc-client 模块。  
- **kam-admin-server** 内部不再按“一个业务一个平铺 module”，而是按领域分子包，例如：

```
io.kamailio.admin
  ├── config
  ├── rpc          # KamailioRpcService 实现
  └── modules
      ├── user     # subscriber, domain, permissions, userdata, usrpreferences
      ├── routing  # dispatcher, drouting, lcr, carrierroute, dialplan, mtree, pdt
      ├── session  # dialog, htable, location
      ├── monitor  # monitoring, acc, version, system
      ├── presence # presence, msilo, uac
      └── trace    # siptrace, topos, secfilter, rtpengine
```

- 若某一块后续要单独复用（例如“只发 RPC、不提供 HTTP”），再拆成独立 Maven 模块（如 **kam-admin-rpc-client**），server 依赖该 JAR。

**优点**：先通过包结构理清领域，再按需把 1～2 块拆成独立模块，演进平滑。  
**缺点**：包名和类移动会有一轮重构。

**适用**：当前就想理清结构，又不想一次性上很多 Maven 模块时。

---

## 六、推荐实施顺序（可裁剪）

1. **第一步（低成本）**  
   - 把 **common** 中与业务无关的 DTO、异常、常量迁到 **kam-admin-common**。  
   - server 只保留：config、KamailioRpcService、各 module 的 controller/service/repository/entity。  
   - 对应“思路一”。

2. **第二步（结构清晰）**  
   - 在 **kam-admin-server** 内按“思路四”做**包级**领域拆分（user / routing / session / monitor / presence / trace），不新增 Maven 模块。  
   - 为后续“按域拆 Maven 模块”打基础。

3. **第三步（按需）**  
   - 若某领域要独立交付或多人并行开发，再按“思路二”把该领域拆成 **kam-admin-module-xxx**，server 依赖这些模块并做扫描。  
   - 或需要对外 SDK/开放 API 时，再引入“思路三”的 api/service/dal 分层。

---

## 七、小结

| 思路 | 核心做法 | 改动量 | 适合阶段 |
|------|----------|--------|----------|
| 一、层拆（common 下沉） | common 迁到 kam-admin-common，server 只做组装 | 小 | 立即 |
| 二、按领域拆 Maven 模块 | 多个 kam-admin-module-xxx，server 只做 bootstrap | 大 | 团队/域边界清晰后 |
| 三、按技术层拆 api/service/dal | 分层 JAR，api 可独立复用 | 大 | 要开放 API/SDK 时 |
| 四、混合（包级领域 + 可选 1～2 个 JAR） | server 内按领域子包，再按需拆出 1～2 个模块 | 中 | 想先理清结构再渐进拆 |

建议：先做 **思路一 + 思路四的包级拆分**，再根据协作和交付需求决定是否上 **思路二** 或 **思路三**。
