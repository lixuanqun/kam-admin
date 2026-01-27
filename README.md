# KAM Dashboard

基于 NestJS + Vue Vben Admin 的全栈管理后台项目

## 项目结构

```
.
├── backend/          # NestJS 后端服务
├── frontend/         # Vue Vben Admin 前端应用
└── README.md
```

## 技术栈

### 后端 (Backend)
- **NestJS** - 渐进式 Node.js 框架
- **TypeScript** - 类型安全

### 前端 (Frontend)
- **Vue 3** - 渐进式 JavaScript 框架
- **Vben Admin** - 企业级中后台管理模板
- **TypeScript** - 类型安全
- **Vite** - 下一代前端构建工具
- **Ant Design Vue** - 企业级 UI 组件库

## 快速开始

### 环境要求

- Node.js >= 18.0.0
- pnpm >= 8.0.0 (前端)
- npm >= 8.0.0 (后端)

### 后端开发

```bash
cd backend
npm install
npm run start:dev
```

后端服务默认运行在 http://localhost:3000

### 前端开发

```bash
cd frontend
pnpm install
pnpm run dev
```

前端应用默认运行在 http://localhost:5173

## 构建

### 后端构建

```bash
cd backend
npm run build
```

### 前端构建

```bash
cd frontend
pnpm run build
```

## 许可证

MIT License
