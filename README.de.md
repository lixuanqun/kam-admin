# Kamailio Dashboard

<p align="center">
  <b>Ein Full-Stack-Management-Dashboard für Kamailio SIP-Server</b>
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

## Funktionen

- **Benutzerverwaltung**: SIP-Benutzer, Domains, Aliase, Benutzergruppen
- **Routing-Verwaltung**: Dispatcher, dynamisches Routing, LCR, Carrier-Routen, Wählplan
- **Echtzeit-Überwachung**: Registrierungsstatus, aktive Dialoge, SIP-Tracing
- **Sicherheitsverwaltung**: IP-Whitelist, Sicherheitsfilter, Pike-Schutz
- **Abrechnungssystem**: CDR-Datensätze, Statistiken verpasster Anrufe
- **Systemverwaltung**: TLS, Konfiguration, Statistiken, RTPEngine

## Schnellstart

### Voraussetzungen

- Node.js >= 18.0.0
- npm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x (mit aktiviertem JSONRPC)

### Backend-Setup

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### Frontend-Setup

```bash
cd frontend
npm install
npm run dev
```

### Zugriffs-URLs

- Frontend: http://localhost:5666
- Backend-API: http://localhost:3000
- Swagger-Dokumentation: http://localhost:3000/api/docs

## Projektstruktur

```
.
├── backend/          # NestJS-Backend-Service
├── frontend/         # Arco Design Pro Vue Frontend
├── docs/             # Dokumentation
└── README.md
```

## Technologie-Stack

| Schicht | Technologie |
|---------|-------------|
| Backend | NestJS, TypeORM, MySQL |
| Frontend | Vue 3, Vite, Arco Design Vue |
| Vorlage | Arco Design Pro Vue |

## Dokumentation

- [Erste Schritte](./docs/README.md)
- [Benutzerhandbuch](./docs/user-guide.md)
- [Routing-Handbuch](./docs/routing-guide.md)
- [Überwachungshandbuch](./docs/monitoring-guide.md)
- [Systemhandbuch](./docs/system-guide.md)
- [API-Referenz](./docs/api-reference.md)
- [Bereitstellungshandbuch](./docs/deployment-guide.md)

## Kamailio-Konfiguration

JSONRPC in `kamailio.cfg` aktivieren:

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

## Lizenz

[MIT-Lizenz](./LICENSE)

## Beitragen

Issues und Pull Requests sind willkommen.
