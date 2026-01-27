# Kamailio Dashboard

<p align="center">
  <b>基于 NestJS + Vue Vben Admin 的 Kamailio 全栈管理后台</b>
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
  <img src="https://img.shields.io/badge/NestJS-10.x-red?style=flat-square&logo=nestjs" alt="NestJS">
  <img src="https://img.shields.io/badge/Vue-3.x-green?style=flat-square&logo=vue.js" alt="Vue">
  <img src="https://img.shields.io/badge/TypeScript-5.x-blue?style=flat-square&logo=typescript" alt="TypeScript">
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License">
</p>

---

## 功能特性

- **用户管理**: SIP 用户、域、别名、用户组管理
- **路由管理**: 调度器、动态路由、LCR、运营商路由、拨号计划
- **实时监控**: 注册状态、活动对话、SIP 跟踪
- **安全管理**: IP 白名单、安全过滤、Pike 防护
- **计费系统**: CDR 记录、未接来电统计
- **系统管理**: TLS、配置、统计、RTPEngine

## 快速开始

### 环境要求

- Node.js >= 18.0.0
- pnpm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x（已启用 JSONRPC）

### 后端启动

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### 前端启动

```bash
cd frontend
pnpm install
pnpm run dev:antd
```

### 访问地址

- 前端: http://localhost:5666
- 后端 API: http://localhost:3000
- Swagger 文档: http://localhost:3000/api/docs

## 项目结构

```
.
├── backend/          # NestJS 后端服务
├── frontend/         # Vue Vben Admin 前端应用
├── docs/             # 文档
└── README.md
```

## 功能模块

### 后端模块（24 个）

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

## 文档

详细文档请查看 [docs](./docs/) 目录：

- [使用文档](./docs/README.md)
- [用户管理手册](./docs/user-guide.md)
- [路由管理手册](./docs/routing-guide.md)
- [监控管理手册](./docs/monitoring-guide.md)
- [系统管理手册](./docs/system-guide.md)
- [API 参考](./docs/api-reference.md)
- [部署指南](./docs/deployment-guide.md)

## 技术栈

### 后端

- **框架**: NestJS 10.x
- **语言**: TypeScript
- **数据库**: MySQL/MariaDB + TypeORM
- **文档**: Swagger/OpenAPI

### 前端

- **框架**: Vue 3 + Vite
- **UI 组件**: Ant Design Vue
- **模板**: Vue Vben Admin

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

## 许可证

[MIT License](./LICENSE)

## 贡献

欢迎提交 Issue 和 Pull Request。
