# Docker 架构说明

参考 [OpenSIPS 社区集群方案](https://github.com/OpenSIPS/docker-opensips) 与 [OpenSIPS IMS CE](https://github.com/OpenSIPS/opensips-ims-ce)，kam-admin 采用 Docker Compose 编排全栈，便于开发、演示与生产部署。

---

## 一、架构图

```
                    ┌─────────────────────────────────────────┐
                    │     Nginx (:80) - 统一入口               │
                    │  前端静态 /api /open /v3/api-docs        │
                    └─────────────────────┬───────────────────┘
                                          │
                                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         kam-admin-server (Spring Boot)                       │
│                              :3000 /api, /open/v1                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ 领域模块     │  │ 鉴权/租户    │  │ Kamailio RPC │  │ Redis/Nacos      │  │
│  │ user/routing │  │ AuthFilter   │  │ KamailioRpc  │  │ (可选)           │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  └────────┬─────────┘  │
└─────────┼─────────────────┼─────────────────┼───────────────────┼────────────┘
          │                 │                 │                   │
          ▼                 ▼                 ▼                   ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   MySQL     │    │   Kamailio  │    │  RTPengine  │    │   Redis     │
│   :3306     │    │   :5060     │    │ :22222/udp  │    │   :6379     │
│  kamailio   │◄───│  JSONRPC    │◄───│  RTP 媒体   │    │  缓存/限流  │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

---

## 二、服务组件

| 组件 | 镜像/构建 | 端口 | 说明 |
|------|-----------|------|------|
| **nginx** | 自构建 | **80** | **统一入口**：前端静态 + /api、/open 代理 |
| **kam-admin-server** | 自构建 | 3000 | 管理后台 API |
| **MySQL** | mysql:8.0 | 3306 | Kamailio + kam-admin 共享库 |
| **Redis** | redis:7-alpine | 6379 | 缓存、限流、会话 |
| **Kamailio** | 自构建（ghcr.io/kamailio/kamailio） | 5060 | SIP 服务器，启用 JSONRPC |
| **RTPengine** | fonoster/rtpengine | 22222/udp, 10000-10100/udp | 媒体代理 |

---

## 三、目录结构（参考 OpenSIPS）

```
kam-admin/
├── docker-compose.yml          # 全栈编排
├── docker/
│   ├── README.md
│   ├── mysql/init/             # MySQL 初始化
│   ├── nginx/                  # 前端 + 反向代理
│   │   ├── Dockerfile
│   │   └── nginx.conf
│   └── kamailio/
│       ├── Dockerfile
│       └── kamailio-docker.cfg
├── kam-admin-server/
│   └── Dockerfile              # 多阶段构建
└── k8s/                        # Kubernetes 清单（生产可选）
```

---

## 四、环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `SPRING_DATASOURCE_URL` | jdbc:mysql://mysql:3306/kamailio | 数据库连接 |
| `SPRING_DATASOURCE_USERNAME` | kamailio | 数据库用户 |
| `SPRING_DATASOURCE_PASSWORD` | kamailiorw | 数据库密码 |
| `KAMAILIO_RPC_HOST` | kamailio | Kamailio 服务名 |
| `KAMAILIO_RPC_PORT` | 5060 | RPC 端口 |
| `KAMAILIO_RPC_PATH` | /RPC | RPC 路径 |
| `REDIS_HOST` | redis | Redis 服务名 |
| `REDIS_PORT` | 6379 | Redis 端口 |
| `PORT` | 3000 | 应用端口 |

可通过 `.env` 或 `docker-compose.override.yml` 覆盖。

---

## 五、启动与扩展

```bash
# 启动全栈
docker compose up -d

# 查看日志
docker compose logs -f kam-admin-server

# 扩缩容（示例）
docker compose up -d --scale kam-admin-server=2
```

生产环境可配合 [k8s/](../k8s/) 清单部署到 Kubernetes，或使用 Nginx/Traefik 做反向代理与负载均衡。
