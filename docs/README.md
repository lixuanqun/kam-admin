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

Kamailio Dashboard 是一个基于 **NestJS + Arco Design Pro Vue** 的全栈管理后台项目，用于可视化管理 Kamailio SIP 服务器的配置、数据库和运行状态。

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
| 后端框架 | NestJS 10.x |
| 数据库 | MySQL 5.7+ / MariaDB |
| ORM | TypeORM |
| 前端框架 | Vue 3 + Vite |
| UI 组件 | Arco Design Vue |
| 前端模板 | Arco Design Pro Vue |

---

## 快速开始

### 环境要求

- Node.js >= 18.0.0
- npm >= 8.0.0
- MySQL 5.7+ 或 MariaDB 10.x
- Kamailio 5.x (已配置 JSONRPC 模块)

### 1. 克隆项目

```bash
git clone https://github.com/lixuanqun/kam-admin.git
cd kam-admin
```

### 2. 配置后端

```bash
cd backend

# 复制环境变量配置
cp .env.example .env

# 编辑配置文件
vim .env
```

配置 `.env` 文件：

```env
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=kamailio
DB_PASSWORD=kamailiorw
DB_DATABASE=kamailio
DB_LOGGING=false

# Kamailio RPC 配置
KAMAILIO_RPC_HOST=localhost
KAMAILIO_RPC_PORT=5060
KAMAILIO_RPC_PATH=/RPC

# 服务端口
PORT=3000
```

安装依赖并启动：

```bash
npm install
npm run start:dev
```

### 3. 配置前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 4. 访问系统

- 前端地址: http://localhost:5666
- 后端 API: http://localhost:3000
- Swagger 文档: http://localhost:3000/api/docs

---

## 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                  前端 (Arco Design Pro Vue)                    │
│                    http://localhost:5666                      │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTP/REST API
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                      后端 (NestJS)                            │
│                    http://localhost:3000                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │  业务模块   │  │  公共服务   │  │   Kamailio RPC      │  │
│  │  Controllers│  │  Services   │  │   Service           │  │
│  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘  │
└─────────┼────────────────┼────────────────────┼─────────────┘
          │                │                    │
          ▼                ▼                    ▼
┌─────────────────┐  ┌──────────────────────────────────────┐
│   MySQL/MariaDB │  │           Kamailio SIP Server        │
│   (kamailio db) │  │         (JSONRPC Interface)          │
└─────────────────┘  └──────────────────────────────────────┘
```

---

## 功能模块

详细操作手册请参阅：

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

所有 API 返回统一格式：

```json
{
  "code": 0,
  "message": "success",
  "data": { ... },
  "timestamp": 1706345678901
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

确保 Kamailio 数据库已创建并初始化：

```bash
# 使用 kamdbctl 创建数据库
kamdbctl create

# 或手动导入 SQL
mysql -u root -p kamailio < /usr/share/kamailio/mysql/standard-create.sql
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
