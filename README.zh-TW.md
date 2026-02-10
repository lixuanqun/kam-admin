# Kamailio Dashboard

<p align="center">
  <b>基於 NestJS + Arco Design Pro Vue 的 Kamailio 全端管理後台</b>
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

- **使用者管理**: SIP 使用者、網域、別名、使用者群組管理
- **路由管理**: 調度器、動態路由、LCR、營運商路由、撥號計劃
- **即時監控**: 註冊狀態、活動對話、SIP 追蹤
- **安全管理**: IP 白名單、安全過濾、Pike 防護
- **計費系統**: CDR 記錄、未接來電統計
- **系統管理**: TLS、設定、統計、RTPEngine

## 快速開始

### 環境要求

- Node.js >= 18.0.0
- npm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x（已啟用 JSONRPC）

### 後端啟動

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### 前端啟動

```bash
cd frontend
npm install
npm run dev
```

### 存取網址

- 前端: http://localhost:5666
- 後端 API: http://localhost:3000
- Swagger 文件: http://localhost:3000/api/docs

## 專案結構

```
.
├── backend/          # NestJS 後端服務
├── frontend/         # Arco Design Pro Vue 前端應用
├── docs/             # 文件
└── README.md
```

## 技術架構

| 層級 | 技術 |
|------|------|
| 後端 | NestJS, TypeORM, MySQL |
| 前端 | Vue 3, Vite, Arco Design Vue |
| 範本 | Arco Design Pro Vue |

## 文件

- [使用文件](./docs/README.md)
- [使用者管理手冊](./docs/user-guide.md)
- [路由管理手冊](./docs/routing-guide.md)
- [監控管理手冊](./docs/monitoring-guide.md)
- [系統管理手冊](./docs/system-guide.md)
- [API 參考](./docs/api-reference.md)
- [部署指南](./docs/deployment-guide.md)

## 授權條款

[MIT License](./LICENSE)
