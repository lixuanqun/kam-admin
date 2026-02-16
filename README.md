# Kamailio Dashboard

<p align="center">
  <b>A Full-Stack Management Dashboard for Kamailio SIP Server</b>
</p>

<p align="center">
  <a href="./README.md">English</a> |
  <a href="./README.zh-CN.md">简体中文</a> |
  <a href="./README.zh-TW.md">繁體中文</a> |
  <a href="./README.ja.md">日本語</a> |
  <a href="./README.de.md">Deutsch</a> |
  <a href="./README.fr.md">Français</a> |
  <a href="./README.es.md">Español</a> |
  <a href="./README.ko.md">한국어</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=spring-boot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.x-green?style=flat-square&logo=vue.js" alt="Vue">
  <img src="https://img.shields.io/badge/TypeScript-5.x-blue?style=flat-square&logo=typescript" alt="TypeScript">
  <img src="https://img.shields.io/badge/Kamailio-5.x%20%7C%206.x-orange?style=flat-square" alt="Kamailio">
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License">
</p>

---

## Features

- **User Management**: SIP users, domains, aliases, user groups
- **Routing Management**: Dispatcher, dynamic routing, LCR, carrier routes, dialplan
- **Real-time Monitoring**: Registration status, active dialogs, SIP tracing
- **Security Management**: IP whitelist, security filters, Pike protection
- **Billing System**: CDR records, missed call statistics
- **System Management**: TLS, configuration, statistics, RTPEngine

## Quick Start

### Requirements

- JDK 21（服务端）
- Node.js >= 18.0.0（前端）
- MySQL 5.7+ / MariaDB 10.x 或 PostgreSQL
- Kamailio 5.x / 6.0.x（需启用 JSONRPC，加载 jsonrpcs、xhttp 等模块）

> 详细 Kamailio 配置见 [部署指南](./docs/deployment-guide.md#45-dashboard-所需-kamailio-模块清单)

### Backend (kam-admin-server)

```bash
mvn clean install -pl kam-admin-server -am
cd kam-admin-server && mvn spring-boot:run
# 或运行 distribution 产出：mvn package -pl distribution -am，再 java -jar distribution/target/app/kam-admin-server.jar
```

### Frontend (kam-admin-console)

```bash
pnpm install
pnpm frontend:dev
# 或：cd kam-admin-console && pnpm dev
```

### Access URLs

- Frontend: http://localhost:5666
- Backend API: http://localhost:3000
- **Swagger UI（接口测试与文档）**: http://localhost:3000/api/docs
- **OpenAPI JSON（导出用）**: http://localhost:3000/v3/api-docs

## Project Structure（多模块）

- **根目录**：`pom.xml` 为 Maven 父 POM，聚合子模块。
- **kam-admin-common**：公共 JAR（DTO、异常、RPC 接口）。
- **kam-admin-module-user**：用户领域模块（可迁入 subscriber、domain 等）。
- **kam-admin-server**：服务端主应用（Spring Boot 3 + JDK 21）。
- **kam-admin-console**：Vue3 + Arco Design 控制台（pnpm 包）。
- **distribution**：打包模块，产出可运行 JAR。

详见 [工程结构说明](./docs/project-structure.md)。  
构建：`mvn clean install`（需 JDK 21）；前端：`pnpm install` 后 `pnpm frontend:dev`。

## Documentation

- [Getting Started](./docs/README.md) · [Project Structure](./docs/project-structure.md)
- [Deployment Guide](./docs/deployment-guide.md) · [API Reference](./docs/api-reference.md)
- [User Guide](./docs/user-guide.md) · [Routing Guide](./docs/routing-guide.md) · [Monitoring Guide](./docs/monitoring-guide.md) · [System Guide](./docs/system-guide.md)
- 架构与拆分：[Server 拆分思路](./docs/kam-admin-server-split-design.md) · [Common/领域/鉴权](./docs/architecture-common-modules-auth.md) · [API/集群/Nacos/Redis](./docs/architecture-api-cluster-nacos-redis.md)

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3, JPA, MySQL/PostgreSQL, Nacos, Redis |
| Frontend | Vue 3, Vite, Arco Design Vue |

## License

[MIT License](./LICENSE)
