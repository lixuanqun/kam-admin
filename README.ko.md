# Kamailio Dashboard

<p align="center">
  <b>NestJS + Vue Vben Admin 기반 Kamailio 풀스택 관리 대시보드</b>
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

## 기능

- **사용자 관리**: SIP 사용자, 도메인, 별칭, 사용자 그룹
- **라우팅 관리**: 디스패처, 동적 라우팅, LCR, 캐리어 라우트, 다이얼플랜
- **실시간 모니터링**: 등록 상태, 활성 다이얼로그, SIP 추적
- **보안 관리**: IP 화이트리스트, 보안 필터, Pike 보호
- **과금 시스템**: CDR 레코드, 부재중 통화 통계
- **시스템 관리**: TLS, 설정, 통계, RTPEngine

## 빠른 시작

### 요구 사항

- Node.js >= 18.0.0
- pnpm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x (JSONRPC 활성화)

### 백엔드 설정

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### 프론트엔드 설정

```bash
cd frontend
pnpm install
pnpm run dev:antd
```

### 접속 URL

- 프론트엔드: http://localhost:5666
- 백엔드 API: http://localhost:3000
- Swagger 문서: http://localhost:3000/api/docs

## 프로젝트 구조

```
.
├── backend/          # NestJS 백엔드 서비스
├── frontend/         # Vue Vben Admin 프론트엔드
├── docs/             # 문서
└── README.md
```

## 기술 스택

| 계층 | 기술 |
|------|------|
| 백엔드 | NestJS, TypeORM, MySQL |
| 프론트엔드 | Vue 3, Vite, Ant Design Vue |
| 템플릿 | Vue Vben Admin |

## 문서

- [시작하기](./docs/README.md)
- [사용자 가이드](./docs/user-guide.md)
- [라우팅 가이드](./docs/routing-guide.md)
- [모니터링 가이드](./docs/monitoring-guide.md)
- [시스템 가이드](./docs/system-guide.md)
- [API 레퍼런스](./docs/api-reference.md)
- [배포 가이드](./docs/deployment-guide.md)

## Kamailio 설정

`kamailio.cfg`에서 JSONRPC 활성화:

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

## 라이선스

[MIT 라이선스](./LICENSE)

## 기여

Issue와 Pull Request를 환영합니다.
