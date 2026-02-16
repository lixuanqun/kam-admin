# Kam-Admin 工程结构说明（Nacos 风格多模块）

参考 [Nacos](https://github.com/alibaba/nacos) 多模块工程，将项目规划为「父 POM + 多子模块」的 Maven 结构，便于依赖统一管理、分层清晰与后续接入 Nacos 配置/服务发现。

## 一、当前结构（已落地）

```
kam-admin/
├── pom.xml                          # 父 POM（聚合 + dependencyManagement）
├── package.json                     # 根目录仅后端/Docker 脚本（无 pnpm）
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
├── docker-compose.yml               # 全栈编排（Nginx + server + MySQL + Redis + Kamailio + RTPengine）
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
| `kam-admin-console/` | 控制台前端（Vue3 + Arco），仅此子工程使用 pnpm |

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

- **backend**、**frontend** 已删除，由 **kam-admin-server**、**kam-admin-console** 替代。
- 根目录无 pnpm，仅 **kam-admin-console** 使用 pnpm；根目录 `package.json` 仅含后端构建与 Docker 脚本。

## 八、文档索引

完整导航见 [README.md#文档导航](./README.md#文档导航)。本目录当前文档：

- **入门与结构**：README.md、project-structure.md  
- **部署**：deployment-guide.md、docker-architecture.md、k8s-deployment-guide.md  
- **架构**：domain-modeling-and-subdomains.md、kam-admin-server-split-design.md、architecture-*.md  
- **API 与导出**：api-reference.md、export-openapi.md  
- **操作手册**：user-guide.md、routing-guide.md、monitoring-guide.md、system-guide.md  
- **参考**：kamailio-skill.md、arco-design-vue-skill.md  
- **归档**：archive/（历史核对报告，非主流程）
