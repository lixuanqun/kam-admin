# Kamailio Dashboard

<p align="center">
  <b>NestJS + Vue Vben Admin ベースの Kamailio フルスタック管理ダッシュボード</b>
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

## 機能

- **ユーザー管理**: SIP ユーザー、ドメイン、エイリアス、ユーザーグループ
- **ルーティング管理**: ディスパッチャー、動的ルーティング、LCR、キャリアルート、ダイヤルプラン
- **リアルタイム監視**: 登録状態、アクティブダイアログ、SIP トレース
- **セキュリティ管理**: IP ホワイトリスト、セキュリティフィルター、Pike 保護
- **課金システム**: CDR レコード、不在着信統計
- **システム管理**: TLS、設定、統計、RTPEngine

## クイックスタート

### 必要条件

- Node.js >= 18.0.0
- pnpm >= 8.0.0
- MySQL 5.7+ / MariaDB 10.x
- Kamailio 5.x（JSONRPC 有効）

### バックエンドのセットアップ

```bash
cd backend
cp .env.example .env
npm install
npm run start:dev
```

### フロントエンドのセットアップ

```bash
cd frontend
pnpm install
pnpm run dev:antd
```

### アクセス URL

- フロントエンド: http://localhost:5666
- バックエンド API: http://localhost:3000
- Swagger ドキュメント: http://localhost:3000/api/docs

## プロジェクト構造

```
.
├── backend/          # NestJS バックエンドサービス
├── frontend/         # Vue Vben Admin フロントエンド
├── docs/             # ドキュメント
└── README.md
```

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| バックエンド | NestJS, TypeORM, MySQL |
| フロントエンド | Vue 3, Vite, Ant Design Vue |
| テンプレート | Vue Vben Admin |

## ドキュメント

- [はじめに](./docs/README.md)
- [ユーザーガイド](./docs/user-guide.md)
- [ルーティングガイド](./docs/routing-guide.md)
- [監視ガイド](./docs/monitoring-guide.md)
- [システムガイド](./docs/system-guide.md)
- [API リファレンス](./docs/api-reference.md)
- [デプロイガイド](./docs/deployment-guide.md)

## Kamailio 設定

`kamailio.cfg` で JSONRPC を有効にする：

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

## ライセンス

[MIT License](./LICENSE)

## コントリビューション

Issue や Pull Request を歓迎します。
