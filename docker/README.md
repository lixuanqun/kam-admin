# Docker 开发环境

本目录包含 Kamailio、MySQL 等外部依赖的 Docker 配置，与根目录 `docker-compose.yml` 配合使用。架构参考 [OpenSIPS 社区集群方案](https://github.com/OpenSIPS/docker-opensips)。

## 目录结构

```
docker/
├── README.md           # 本说明
├── mysql/
│   └── init/           # MySQL 初始化脚本（首次启动执行）
│       ├── 02-kamailio-schema.sh   # 拉取并导入 Kamailio schema
│       └── 03-kam-admin-ext.sql     # 索引优化 + 租户表（合并）
├── nginx/
│   ├── Dockerfile      # 构建前端 + Nginx 托管
│   └── nginx.conf      # 静态资源 + /api 代理
└── kamailio/
    ├── Dockerfile      # 基于官方镜像 + JSONRPC + RTPengine 配置
    └── kamailio-docker.cfg
```

## 使用方式

在项目根目录执行：

```bash
docker compose up -d
```

首次启动时 MySQL 会执行 `init/` 下的脚本，从 Kamailio 官方仓库拉取 schema 并导入。

## 服务与端口

| 服务 | 端口 | 说明 |
|------|------|------|
| **nginx** | **80** | **统一入口**：前端静态 + /api 代理 |
| kam-admin-server | 3000 | 管理后台 API（也可直连） |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| Kamailio | 5060 | SIP/JSONRPC |
| RTPengine | 22222/udp, 10000-10100/udp | 媒体代理（RTP/WebRTC） |

**默认访问**：http://localhost:80 或 http://localhost（前端与 API 统一经 Nginx）

## 默认凭据

- **MySQL**：root/root，kamailio/kamailiorw
- **Redis**：无密码

## 配置覆盖

根目录创建 `.env`（可参考 `.env.example`）可覆盖默认环境变量。详见 [docs/docker-architecture.md](../docs/docker-architecture.md)。

## License

[Apache License 2.0](../LICENSE)
