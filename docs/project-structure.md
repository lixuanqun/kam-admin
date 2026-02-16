# Kam-Admin 工程结构说明（Nacos 风格多模块）

参考 [Nacos](https://github.com/alibaba/nacos) 多模块工程，将项目规划为「父 POM + 多子模块」的 Maven 结构，便于依赖统一管理、分层清晰与后续接入 Nacos 配置/服务发现。

## 一、当前结构（已落地）

```
kam-admin/
├── pom.xml                          # 父 POM（聚合 + dependencyManagement）
├── package.json / pnpm-workspace.yaml
├── docs/
├── sql/                             # SQL 汇总：Kamailio 与本模块所需脚本（见 sql/README.md）
├── scripts/                         # 脚本（导出 OpenAPI、拉取 Kamailio schema 等）
├── kam-admin-common/                # 公共模块：DTO、异常、RPC 接口
├── kam-admin-module-user/           # 用户领域：subscriber, domain, permissions, userdata, usrpreferences
├── kam-admin-module-routing/        # 路由领域：dispatcher, drouting, lcr, carrierroute, dialplan, mtree, pdt
├── kam-admin-module-session/        # 会话领域：dialog, htable, location
├── kam-admin-module-monitor/        # 监控领域：monitoring, acc, version, system
├── kam-admin-module-presence/       # 状态领域：presence, msilo, uac
├── kam-admin-module-trace/          # 跟踪领域：siptrace, topos, secfilter, rtpengine
├── kam-admin-server/                # 服务端（Spring Boot 主应用，依赖上述 6 个领域模块）
├── kam-admin-console/               # 控制台前端（Vue/Arco）
├── k8s/                             # Kubernetes 清单（Deployment/Service/ConfigMap 等）
├── docker/                           # Docker 外部依赖（MySQL init、Kamailio 配置）
├── docker-compose.yml               # 全栈编排（server + MySQL + Redis + Kamailio）
└── ...
```

## 二、模块职责（对标 Nacos）

| 模块 | 职责 | 类比 Nacos |
|------|------|------------|
| **根 pom.xml** | 聚合子模块、统一版本与 dependencyManagement、通用插件 | nacos-all |
| **kam-admin-common** | 公共 DTO（ApiResponse、Pagination）、异常、常量、工具类 | nacos-common |
| **kam-admin-module-*** | 6 个领域 JAR：user / routing / session / monitor / presence / trace，仅依赖 common | 按领域拆分的业务模块 |
| **kam-admin-server** | Spring Boot 主应用：配置、RPC、鉴权、租户、开放 API，依赖上述 6 个领域模块 | nacos-console + nacos-server |
| **kam-admin-console** | 前端控制台（Vue3 + Arco Design） | console-ui |

## 三、与目录的对应关系

| 目录 | 说明 |
|------|------|
| `pom.xml`（根） | 父 POM，聚合 common、6 个领域模块、server |
| `kam-admin-common/` | 公共 JAR，供各领域模块与 server 依赖 |
| `kam-admin-module-user/` | 用户领域 JAR：subscriber, domain, permissions, userdata, usrpreferences |
| `kam-admin-module-routing/` | 路由领域 JAR：dispatcher, drouting, lcr, carrierroute, dialplan, mtree, pdt |
| `kam-admin-module-session/` | 会话领域 JAR：dialog, htable, location |
| `kam-admin-module-monitor/` | 监控领域 JAR：monitoring, acc, version, system |
| `kam-admin-module-presence/` | 状态领域 JAR：presence, msilo, uac |
| `kam-admin-module-trace/` | 跟踪领域 JAR：siptrace, topos, secfilter, rtpengine |
| `kam-admin-server/` | 服务端模块，依赖上述 6 个领域模块，保留启动类、配置、RPC、鉴权等；打包产出 `kam-admin-server/target/kam-admin-server-*.jar` |
| `kam-admin-console/` | 控制台前端，pnpm workspace 包，package 名 kamailio-dashboard |

## 四、父 POM 要点（参考 Nacos）

- `packaging` 为 `pom`，仅做聚合与依赖管理。
- `<modules>` 列出：`kam-admin-common`、6 个 `kam-admin-module-*`、`kam-admin-server`。
- `<dependencyManagement>` 中统一 Spring Boot、Spring Cloud Alibaba、MySQL、PostgreSQL、springdoc 及 6 个领域模块版本。
- 子模块通过 `<parent>` 引用根，同 `groupId`/`version`，仅 `artifactId` 不同。
- **kam-admin-server** 依赖 **kam-admin-common** 及 6 个领域模块（user、routing、session、monitor、presence、trace）；各领域模块仅依赖 common，不依赖 server。

## 五、构建与运行

- 仅构建服务端：`mvn clean install -pl kam-admin-server -am`
- 构建全部：`mvn clean install`
- 打包可运行 JAR：`mvn clean package -pl kam-admin-server -am`，产出在 `kam-admin-server/target/kam-admin-server-*.jar`
- 前端：仅子工程 `kam-admin-console` 使用 pnpm，`cd kam-admin-console && pnpm install && pnpm dev`
- 若需一键脚本，可在根目录提供 `dev.sh` / `start.sh`。

## 六、已集成与扩展（Nacos / Redis）

- **Nacos**：`kam-admin-server` 已集成 Nacos 配置中心与服务发现（`bootstrap.yml`），配置可从 Nacos 读取并动态刷新。
- **Redis**：已集成 Spring Data Redis，用于缓存、限流、会话（集群模式）。
- **对外 API**：`/open/v1/*` 开放接口，配合 API Key 鉴权与限流，详见 [architecture-api-cluster-nacos-redis.md](./architecture-api-cluster-nacos-redis.md)。
- **多种鉴权**：支持 API Key、Session、JWT、OAuth2 等，详见 [architecture-common-modules-auth.md](./architecture-common-modules-auth.md)。

## 七、已移除的旧目录

- **backend-spring**、**frontend** 已删除，分别由 **kam-admin-server**、**kam-admin-console** 替代。
- pnpm workspace 仅保留 `kam-admin-console`；根目录 `dev`/`build` 仅含前端脚本。

## 八、文档索引

| 文档 | 说明 |
|------|------|
| [README.md](./README.md) | 使用文档：快速开始、系统架构、配置说明 |
| [project-structure.md](./project-structure.md) | 本文件，工程结构与构建说明 |
| [deployment-guide.md](./deployment-guide.md) | 生产部署：后端 JAR、前端构建、Nginx、Kamailio |
| [api-reference.md](./api-reference.md) | API 参考（对内 /api、开放 /open/v1） |
| [kam-admin-server-split-design.md](./kam-admin-server-split-design.md) | server 拆分思路：common 下沉、按领域/技术层拆模块 |
| [domain-modeling-and-subdomains.md](./domain-modeling-and-subdomains.md) | 领域建模与子域划分：核心/支撑/通用子域、划分理由与未来扩展 |
| [architecture-common-modules-auth.md](./architecture-common-modules-auth.md) | 架构：common 下沉、领域模块、对外 API、多种鉴权 |
| [architecture-api-cluster-nacos-redis.md](./architecture-api-cluster-nacos-redis.md) | 架构：对外 API、集群、Nacos、Redis |
| [architecture-multi-tenancy.md](./architecture-multi-tenancy.md) | 架构：多租户体系（租户识别、隔离、RPC/DB 路由） |
| [export-openapi.md](./export-openapi.md) | 导出 Swagger/OpenAPI 文档与接口测试说明 |
| **sql/**（目录） | [sql/README.md](../sql/README.md) 汇总 Kamailio 与本模块所需 SQL |
| [user-guide.md](./user-guide.md) | 用户管理操作手册 |
| [routing-guide.md](./routing-guide.md) | 路由管理操作手册 |
| [monitoring-guide.md](./monitoring-guide.md) | 监控管理操作手册 |
| [system-guide.md](./system-guide.md) | 系统管理操作手册 |
| **archive/**（目录） | 归档文档：历史核对报告等 |
| [k8s-deployment-guide.md](./k8s-deployment-guide.md) | Kubernetes 部署指南 |
| **docker/**（目录） | [docker/README.md](../docker/README.md) Docker 开发环境（MySQL/Redis/Kamailio/RTPengine） |
| [docker-architecture.md](./docker-architecture.md) | Docker 架构与配置（参考 OpenSIPS 社区方案） |
