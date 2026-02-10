# Kamailio Dashboard

<p align="center">
  <b>Un tableau de bord de gestion Full-Stack pour le serveur SIP Kamailio</b>
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

## Fonctionnalités

- **Gestion des utilisateurs**: Utilisateurs SIP, domaines, alias, groupes d'utilisateurs
- **Gestion du routage**: Dispatcher, routage dynamique, LCR, routes opérateur, plan de numérotation
- **Surveillance en temps réel**: État d'enregistrement, dialogues actifs, traçage SIP
- **Gestion de la sécurité**: Liste blanche IP, filtres de sécurité, protection Pike
- **Système de facturation**: Enregistrements CDR, statistiques des appels manqués
- **Gestion du système**: TLS, configuration, statistiques, RTPEngine

## Démarrage rapide

### Prérequis

- Node.js >= 18.0.0
- npm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x (avec JSONRPC activé)

### Configuration du backend

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### Configuration du frontend

```bash
cd frontend
npm install
npm run dev
```

### URLs d'accès

- Frontend: http://localhost:5666
- API Backend: http://localhost:3000
- Documentation Swagger: http://localhost:3000/api/docs

## Structure du projet

```
.
├── backend/          # Service backend NestJS
├── frontend/         # Frontend Arco Design Pro Vue
├── docs/             # Documentation
└── README.md
```

## Stack technologique

| Couche | Technologie |
|--------|-------------|
| Backend | NestJS, TypeORM, MySQL |
| Frontend | Vue 3, Vite, Arco Design Vue |
| Modèle | Arco Design Pro Vue |

## Documentation

- [Premiers pas](./docs/README.md)
- [Guide utilisateur](./docs/user-guide.md)
- [Guide de routage](./docs/routing-guide.md)
- [Guide de surveillance](./docs/monitoring-guide.md)
- [Guide système](./docs/system-guide.md)
- [Référence API](./docs/api-reference.md)
- [Guide de déploiement](./docs/deployment-guide.md)

## Configuration Kamailio

Activer JSONRPC dans `kamailio.cfg`:

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

## Licence

[Licence MIT](./LICENSE)

## Contribuer

Les Issues et Pull Requests sont les bienvenues.
