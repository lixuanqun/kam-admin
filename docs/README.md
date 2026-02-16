# Kamailio Dashboard 使用文档

## 目录

1. [项目概述](#项目概述)
2. [快速开始](#快速开始)
3. [系统架构](#系统架构)
4. [功能模块](#功能模块)
5. [API 文档](#api-文档)
6. [配置说明](#配置说明)
7. [常见问题](#常见问题)

---

## 项目概述

Kamailio Dashboard 是一个基于 **Spring Boot + Arco Design Vue** 的全栈管理后台项目，用于可视化管理 Kamailio SIP 服务器的配置、数据库和运行状态。

### 主要功能

- **用户管理**: SIP 用户账号的增删改查
- **路由管理**: 动态路由、LCR、调度器等配置
- **实时监控**: 用户注册状态、活动对话监控
- **安全管理**: IP 白名单、黑名单、安全过滤
- **计费跟踪**: CDR 记录查询、SIP 消息跟踪
- **系统管理**: Kamailio 运行状态、配置管理

### 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.x (JDK 21) |
| 数据库 | MySQL 5.7+ / MariaDB / PostgreSQL |
| ORM | Spring Data JPA |
| 前端框架 | Vue 3 + Vite |
| UI 组件 | Arco Design Vue |

---

## 快速开始

### 环境要求

- JDK 21（后端）
- Node.js >= 18.0.0（前端）
- MySQL 5.7+ / MariaDB / PostgreSQL
- Kamailio 5.x (已配置 JSONRPC 模块)

### 1. 克隆项目

```bash
git clone https://github.com/lixuanqun/kam-admin.git
cd kam-admin
```

### 2. 配置并启动后端 (kam-admin-server)

```bash
# 构建（根目录执行）
npm run backend:build
# 或：mvn clean package -pl kam-admin-server -am -DskipTests

# 启动（根目录执行，配置见 application.yml 或环境变量）
npm run backend:run
# 或：mvn spring-boot:run -pl kam-admin-server
```

或使用 `application.yml` / `application-postgres.yml` / 环境变量配置数据库与 Kamailio RPC，例如：

```env
# 数据库（示例）
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/kamailio
SPRING_DATASOURCE_USERNAME=kamailio
SPRING_DATASOURCE_PASSWORD=kamailiorw

# Kamailio RPC 配置
KAMAILIO_RPC_HOST=localhost
KAMAILIO_RPC_PORT=5060
KAMAILIO_RPC_PATH=/RPC

# 服务端口
PORT=3000
```

### 3. 配置前端 (kam-admin-console)

```bash
# 仅前端子工程使用 pnpm
cd kam-admin-console
pnpm install
pnpm dev
```

### 4. 访问系统

- **Docker 环境**：http://localhost:80（Nginx 统一入口，前端 + API）
- **本地开发**：前端 http://localhost:5666，后端 http://localhost:3000
- Swagger 文档: http://localhost:3000/api/docs 或 http://localhost/api/docs

### 5. 使用 Docker Compose 一键启动全栈（推荐）

```bash
docker compose up -d
# 或：npm run docker:up
```

包含：**Nginx**（前端+代理）、**kam-admin-server**、**MySQL**、**Redis**、**Kamailio**、**RTPengine**。访问 http://localhost:80 即可使用。首次启动会自动初始化 MySQL 与 Kamailio schema。

- 详细说明：[docker/README.md](../docker/README.md)
- 架构与配置：[docker-architecture.md](./docker-architecture.md)

---

## 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│              前端 (kam-admin-console / Arco Design Vue)      │
│                    http://localhost:5666                    │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTP/REST API
                          ▼
┌─────────────────────────────────────────────────────────────┐
│              后端 (kam-admin-server / Spring Boot 3)         │
│                    http://localhost:3000                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │  业务模块   │  │  配置/鉴权   │  │   Kamailio RPC      │  │
│  │  Controllers│  │  Nacos/Redis │  │   (KamailioRpcSvc)  │  │
│  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘  │
└─────────┼────────────────┼────────────────────┼─────────────┘
          │                │                    │
          ▼                ▼                    ▼
┌─────────────────┐  ┌─────────────┐  ┌──────────────────────┐
│ MySQL/PostgreSQL│  │ Redis/Nacos │  │   Kamailio SIP Server  │
│  (kamailio db)  │  │ (可选)      │  │   (JSONRPC Interface)  │
└─────────────────┘  └─────────────┘  └──────────────────────┘
```

---

## 功能模块

### 开发技能

- [Arco Design Vue 开发技能](./arco-design-vue-skill.md) - 基于官方文档的前端组件库使用总结
- [Kamailio 配置与知识 SKILL](./kamailio-skill.md) - 基于官方文档的 Kamailio SIP 服务器配置总结

### 详细操作手册

- [用户管理操作手册](./user-guide.md)
- [路由管理操作手册](./routing-guide.md)
- [监控管理操作手册](./monitoring-guide.md)
- [系统管理操作手册](./system-guide.md)

---

## API 文档

后端启动后访问 Swagger 文档：

```
http://localhost:3000/api/docs
```

### API 响应格式

所有 API 返回统一格式（`timestamp` 为 ISO-8601 字符串）：

```json
{
  "code": 0,
  "message": "success",
  "data": { ... },
  "timestamp": "2025-02-16T12:00:00.000Z"
}
```

### 分页参数

支持分页的接口使用以下参数：

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | number | 1 | 页码 |
| limit | number | 20 | 每页数量 |
| keyword | string | - | 搜索关键字 |

分页响应格式：

```json
{
  "items": [...],
  "total": 100,
  "page": 1,
  "limit": 20,
  "totalPages": 5
}
```

---

## 配置说明

### Docker 环境变量

使用 Docker Compose 时，可通过根目录 `.env` 或 `docker-compose.override.yml` 覆盖配置。示例见根目录 [.env.example](../.env.example)。

| 变量 | 说明 | 默认 |
|------|------|------|
| `SPRING_DATASOURCE_*` | 数据库连接 | mysql:3306/kamailio |
| `KAMAILIO_RPC_HOST` | Kamailio 地址 | kamailio |
| `REDIS_HOST` | Redis 地址 | redis |
| `PORT` | 应用端口 | 3000 |

### Kamailio JSONRPC 配置

在 `kamailio.cfg` 中添加以下配置启用 JSONRPC：

```
# 加载模块
loadmodule "jsonrpcs.so"
loadmodule "xhttp.so"

# 配置 JSONRPC
modparam("jsonrpcs", "pretty_format", 1)
modparam("jsonrpcs", "transport", 1)

# HTTP 监听
listen=tcp:0.0.0.0:5060

# 路由处理
event_route[xhttp:request] {
    if ($hu =~ "^/RPC") {
        jsonrpc_dispatch();
        exit;
    }
}
```

### 数据库配置

确保 Kamailio 数据库已创建并初始化。本仓库 **sql/** 目录汇总了相关 SQL 与说明（[sql/README.md](../sql/README.md)）：

```bash
# 使用 kamdbctl 创建数据库
kamdbctl create

# 或手动导入 SQL（Kamailio 官方 schema 可从安装路径或 sql/kamailio-mysql/ 获取）
mysql -u root -p kamailio < /usr/share/kamailio/mysql/standard-create.sql
# 本模块：sql/01-database-user.sql、sql/02-kam-admin-mysql.sql
```

---

## 常见问题

### Q: 无法连接到 Kamailio RPC？

A: 检查以下配置：
1. Kamailio 是否加载了 `jsonrpcs` 和 `xhttp` 模块
2. 防火墙是否开放了 RPC 端口
3. `.env` 中的 RPC 配置是否正确

### Q: 数据库连接失败？

A: 检查：
1. MySQL 服务是否运行
2. 用户名密码是否正确
3. 用户是否有访问 kamailio 数据库的权限

### Q: 前端无法访问后端 API？

A: 检查：
1. 后端服务是否正常运行
2. CORS 配置是否正确
3. 前端 `.env.development` 中的 API 地址是否正确

---

## 支持

如有问题，请提交 Issue 或联系开发团队。
