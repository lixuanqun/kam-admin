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
  <img src="https://img.shields.io/badge/NestJS-10.x-red?style=flat-square&logo=nestjs" alt="NestJS">
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

- Node.js >= 18.0.0
- npm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x / 6.0.x（需启用 JSONRPC，加载 jsonrpcs、xhttp 等模块）

> 详细 Kamailio 配置见 [部署指南](./docs/deployment-guide.md#45-dashboard-所需-kamailio-模块清单)

### Backend Setup

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

### Access URLs

- Frontend: http://localhost:5666
- Backend API: http://localhost:3000
- Swagger Docs: http://localhost:3000/api/docs

## Project Structure

```
.
├── backend/          # NestJS backend service
├── frontend/         # Arco Design Pro Vue frontend
├── docs/             # Documentation
└── README.md
```

## Documentation

- [Getting Started](./docs/README.md)
- [User Guide](./docs/user-guide.md)
- [Routing Guide](./docs/routing-guide.md)
- [Monitoring Guide](./docs/monitoring-guide.md)
- [System Guide](./docs/system-guide.md)
- [API Reference](./docs/api-reference.md)
- [Deployment Guide](./docs/deployment-guide.md)

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | NestJS, TypeORM, MySQL |
| Frontend | Vue 3, Vite, Arco Design Vue |
| Template | Arco Design Pro Vue |

## License

[MIT License](./LICENSE)
