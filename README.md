# Kamailio Dashboard

基于 **NestJS + Vue Vben Admin** 的 Kamailio 全栈管理后台项目。

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 功能特性

- **用户管理**: SIP 用户、域、别名、用户组管理
- **路由管理**: 调度器、动态路由、LCR、运营商路由、拨号计划
- **实时监控**: 注册状态、活动对话、SIP 跟踪
- **安全管理**: IP 白名单、安全过滤、Pike 防护
- **计费系统**: CDR 记录、未接来电统计
- **系统管理**: TLS、配置、统计、RTPEngine

## 项目结构

```
.
├── backend/          # NestJS 后端服务
│   ├── src/
│   │   ├── common/       # 公共模块
│   │   ├── config/       # 配置文件
│   │   └── modules/      # 业务模块
│   └── package.json
├── frontend/         # Vue Vben Admin 前端应用
│   ├── apps/
│   │   └── web-antd/     # Ant Design 版本
│   └── package.json
├── docs/             # 文档
│   ├── README.md         # 主文档
│   ├── user-guide.md     # 用户管理手册
│   ├── routing-guide.md  # 路由管理手册
│   ├── monitoring-guide.md # 监控管理手册
│   ├── system-guide.md   # 系统管理手册
│   ├── api-reference.md  # API 参考
│   └── deployment-guide.md # 部署指南
└── README.md
```

## 快速开始

### 环境要求

- Node.js >= 18.0.0
- pnpm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x (启用 JSONRPC)

### 后端启动

```bash
cd backend

# 配置环境变量
cp .env.example .env
vim .env

# 安装依赖
npm install

# 开发模式启动
npm run start:dev
```

### 前端启动

```bash
cd frontend

# 安装依赖
pnpm install

# 开发模式启动
pnpm run dev:antd
```

### 访问地址

- 前端: http://localhost:5666
- 后端 API: http://localhost:3000
- Swagger 文档: http://localhost:3000/api/docs

## 功能模块

### 后端模块 (24 个)

| 分类 | 模块 | 功能 |
|------|------|------|
| 用户管理 | subscriber | SIP 用户管理 |
| | domain | 域管理 |
| | userdata | 别名/组/快捷拨号 |
| | usrpreferences | 用户偏好设置 |
| 路由管理 | dispatcher | 调度器 |
| | drouting | 动态路由 |
| | lcr | LCR 路由 |
| | carrierroute | 运营商路由 |
| | dialplan | 拨号计划 |
| | mtree | 内存树 |
| | pdt | 前缀域转换 |
| 监控管理 | location | 注册监控 |
| | dialog | 对话管理 |
| | acc | CDR 记录 |
| | siptrace | SIP 跟踪 |
| | topos | 拓扑隐藏 |
| | monitoring | 监控面板 |
| 安全管理 | permissions | 权限管理 |
| | secfilter | 安全过滤 |
| 高级功能 | uac | UAC 注册 |
| | htable | 哈希表 |
| | presence | 存在服务 |
| | msilo | 离线消息 |
| 系统管理 | system | 系统管理 |
| | rtpengine | RTPEngine |
| | version | 数据库版本 |

### 前端页面 (24 个)

- 用户管理、域管理、用户数据、用户偏好
- 调度器、动态路由、LCR 路由、运营商路由、拨号计划、内存树、前缀域转换
- 注册监控、对话管理
- 权限管理、安全过滤
- CDR 记录、SIP 跟踪、拓扑隐藏
- UAC 注册、哈希表、存在服务、离线消息、RTPEngine
- 系统管理、监控面板、数据库版本

## 技术栈

### 后端

- **框架**: NestJS 10.x
- **语言**: TypeScript
- **数据库**: MySQL/MariaDB + TypeORM
- **文档**: Swagger/OpenAPI
- **验证**: class-validator

### 前端

- **框架**: Vue 3 + Vite
- **UI**: Ant Design Vue
- **模板**: Vue Vben Admin
- **状态管理**: Pinia
- **路由**: Vue Router

## 文档

详细文档请查看 [docs](./docs/) 目录：

- [使用文档](./docs/README.md)
- [用户管理手册](./docs/user-guide.md)
- [路由管理手册](./docs/routing-guide.md)
- [监控管理手册](./docs/monitoring-guide.md)
- [系统管理手册](./docs/system-guide.md)
- [API 参考](./docs/api-reference.md)
- [部署指南](./docs/deployment-guide.md)

## Kamailio 配置

在 `kamailio.cfg` 中启用 JSONRPC：

```
loadmodule "xhttp.so"
loadmodule "jsonrpcs.so"

modparam("jsonrpcs", "pretty_format", 1)
modparam("jsonrpcs", "transport", 1)

event_route[xhttp:request] {
    if ($hu =~ "^/RPC") {
        jsonrpc_dispatch();
        exit;
    }
}
```

## 开发

### 添加新模块

1. 在 `backend/src/modules/` 创建模块目录
2. 创建 entity、service、controller、module 文件
3. 在 `app.module.ts` 中注册模块
4. 在前端添加对应的 API 和页面

### 代码规范

- 使用 ESLint + Prettier
- 遵循 NestJS 最佳实践
- 使用 TypeScript 严格模式

## 许可证

[MIT License](./LICENSE)

## 贡献

欢迎提交 Issue 和 Pull Request。
