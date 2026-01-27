# Kamailio Dashboard

<p align="center">
  <b>Un panel de administración Full-Stack para el servidor SIP Kamailio</b>
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

## Características

- **Gestión de usuarios**: Usuarios SIP, dominios, alias, grupos de usuarios
- **Gestión de enrutamiento**: Dispatcher, enrutamiento dinámico, LCR, rutas de operador, plan de marcación
- **Monitoreo en tiempo real**: Estado de registro, diálogos activos, trazado SIP
- **Gestión de seguridad**: Lista blanca de IP, filtros de seguridad, protección Pike
- **Sistema de facturación**: Registros CDR, estadísticas de llamadas perdidas
- **Gestión del sistema**: TLS, configuración, estadísticas, RTPEngine

## Inicio rápido

### Requisitos

- Node.js >= 18.0.0
- pnpm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x (con JSONRPC habilitado)

### Configuración del backend

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### Configuración del frontend

```bash
cd frontend
pnpm install
pnpm run dev:antd
```

### URLs de acceso

- Frontend: http://localhost:5666
- API Backend: http://localhost:3000
- Documentación Swagger: http://localhost:3000/api/docs

## Estructura del proyecto

```
.
├── backend/          # Servicio backend NestJS
├── frontend/         # Frontend Vue Vben Admin
├── docs/             # Documentación
└── README.md
```

## Stack tecnológico

| Capa | Tecnología |
|------|------------|
| Backend | NestJS, TypeORM, MySQL |
| Frontend | Vue 3, Vite, Ant Design Vue |
| Plantilla | Vue Vben Admin |

## Documentación

- [Primeros pasos](./docs/README.md)
- [Guía de usuario](./docs/user-guide.md)
- [Guía de enrutamiento](./docs/routing-guide.md)
- [Guía de monitoreo](./docs/monitoring-guide.md)
- [Guía del sistema](./docs/system-guide.md)
- [Referencia API](./docs/api-reference.md)
- [Guía de despliegue](./docs/deployment-guide.md)

## Configuración de Kamailio

Habilitar JSONRPC en `kamailio.cfg`:

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

## Licencia

[Licencia MIT](./LICENSE)

## Contribuir

Se aceptan Issues y Pull Requests.
