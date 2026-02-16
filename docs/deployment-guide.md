# 部署指南

## 概述

本文档介绍如何在生产环境部署 Kamailio Dashboard。

**开发/演示**：推荐直接使用 [Docker Compose](../docker-compose.yml) 或 [Docker 架构说明](./docker-architecture.md) 一键启动全栈（Nginx + 后端 + MySQL + Redis + Kamailio + RTPengine）。  
**生产**：可按下文进行 JAR 部署、Nginx 配置与 K8S 部署（见 [k8s-deployment-guide.md](./k8s-deployment-guide.md)）。

---

## 1. 环境要求

### 1.1 硬件要求

| 配置 | 最低要求 | 推荐配置 |
|------|---------|---------|
| CPU | 2 核 | 4 核+ |
| 内存 | 2 GB | 4 GB+ |
| 磁盘 | 20 GB | 50 GB+ |

### 1.2 软件要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 21 | 后端 kam-admin-server |
| Maven | 3.8+ | 构建后端 |
| Node.js | >= 18.0.0 | 仅前端子工程 kam-admin-console 构建时需要 |
| pnpm | 9.x | 仅 kam-admin-console 内使用 |
| MySQL/MariaDB/PostgreSQL | 5.7+ / 10.x / 12+ | 业务数据库 |
| Kamailio | 5.x / 6.x | 需启用 JSONRPC |
| Nginx | 1.18+ | 生产环境反向代理与静态资源（或使用 Docker 内 Nginx） |

---

## 2. 后端部署（kam-admin-server）

### 2.1 下载代码与构建

```bash
git clone https://github.com/lixuanqun/kam-admin.git
cd kam-admin

# 构建（需 JDK 21）
mvn clean package -pl kam-admin-server -am
```

可运行 JAR 位于：`kam-admin-server/target/kam-admin-server-0.0.1.jar`。

### 2.2 配置

通过环境变量或 `application.yml` / Nacos 配置，例如：

```env
# 服务端口
PORT=3000

# 数据库（示例 MySQL）
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/kamailio
SPRING_DATASOURCE_USERNAME=kamailio
SPRING_DATASOURCE_PASSWORD=your_secure_password

# Kamailio RPC
KAMAILIO_RPC_HOST=localhost
KAMAILIO_RPC_PORT=5060
KAMAILIO_RPC_PATH=/RPC

# Redis（可选，集群/缓存/限流）
REDIS_HOST=localhost
REDIS_PORT=6379

# Nacos（可选，配置中心/服务发现）
NACOS_SERVER_ADDR=127.0.0.1:8848
```

### 2.3 启动服务

**直接运行 JAR：**

```bash
java -jar kam-admin-server/target/kam-admin-server-0.0.1.jar
```

**使用 systemd（推荐生产）：**

创建 `/etc/systemd/system/kam-dashboard-api.service`：

```ini
[Unit]
Description=Kamailio Dashboard API (Spring Boot)
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/kam-admin
ExecStart=/usr/bin/java -jar /opt/kam-admin/kam-admin-server/target/kam-admin-server-0.0.1.jar
Restart=on-failure
Environment=PORT=3000

[Install]
WantedBy=multi-user.target
```

启动：

```bash
systemctl daemon-reload
systemctl enable kam-dashboard-api
systemctl start kam-dashboard-api
```

---

## 3. 前端部署（kam-admin-console）

### 3.1 安装依赖与构建

```bash
cd kam-admin-console
pnpm install
pnpm run build
```

构建产物位于 `kam-admin-console/dist` 目录。

### 3.2 配置环境

编辑 `kam-admin-console/.env.production`（如需要）：

```env
VITE_API_BASE_URL=/api
```

### 3.4 部署到 Nginx

**复制文件：**

```bash
cp -r kam-admin-console/dist/* /var/www/kam-dashboard/
```

**Nginx 配置：**

```nginx
server {
    listen 80;
    server_name dashboard.example.com;
    
    # 重定向到 HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name dashboard.example.com;
    
    # SSL 证书
    ssl_certificate /etc/letsencrypt/live/dashboard.example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/dashboard.example.com/privkey.pem;
    
    # 前端静态文件
    root /var/www/kam-dashboard;
    index index.html;
    
    # SPA 路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
    
    # API 代理（可选）
    location /api {
        proxy_pass http://127.0.0.1:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }
}
```

重载 Nginx：

```bash
nginx -t
systemctl reload nginx
```

---

## 4. Kamailio 配置

### 4.1 启用 JSONRPC

在 `kamailio.cfg` 中添加：

```
#!define WITH_JSONRPC

loadmodule "xhttp.so"
loadmodule "jsonrpcs.so"

# JSONRPC 参数
modparam("jsonrpcs", "pretty_format", 1)
modparam("jsonrpcs", "transport", 1)
modparam("jsonrpcs", "fifo_name", "/var/run/kamailio/kamailio_rpc.fifo")

# HTTP 事件路由
event_route[xhttp:request] {
    set_reply_close();
    set_reply_no_connect();
    
    if ($hu =~ "^/RPC") {
        jsonrpc_dispatch();
        exit;
    }
    
    xhttp_reply("200", "OK", "text/html",
        "<html><body>Kamailio JSONRPC</body></html>");
}
```

### 4.2 配置监听地址

```
# HTTP/RPC 监听
listen=tcp:127.0.0.1:5060

# 或允许远程访问（需配合防火墙）
listen=tcp:0.0.0.0:5060
```

### 4.3 重启 Kamailio

```bash
systemctl restart kamailio
```

### 4.4 测试 RPC

```bash
curl -X POST http://localhost:5060/RPC \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"core.version","id":1}'
```

### 4.5 Dashboard 所需 Kamailio 模块清单

Dashboard 正常运行需要 Kamailio 加载以下模块，请确保 `kamailio.cfg` 中已正确配置：

| 模块 | 用途 | 必需性 |
|------|------|--------|
| **jsonrpcs** | JSONRPC 服务，Dashboard 通信必需 | 必需 |
| **xhttp** | HTTP 请求处理，jsonrpcs 依赖 | 必需 |
| **usrloc** | 用户位置/注册，监控与位置管理 | 必需 |
| **dispatcher** | 调度器，负载均衡配置 | 必需 |
| **drouting** | 动态路由，网关与规则管理 | 必需 |
| **domain** | 域管理 | 必需 |
| **permissions** | 权限/IP 白名单 | 必需 |
| **dialog** | 对话管理 | 必需 |
| **htable** | 哈希表 | 可选 |
| **cfg_rpc** | 运行时配置，系统管理页 | 推荐 |
| **statistics** | 统计信息，监控与系统页 | 推荐 |
| **tls** | TLS 配置，系统管理页 | 可选 |
| **pike** | Pike 防护，系统管理页 | 可选 |
| **pdt** | 前缀域转换 | 可选 |
| **mtree** | 内存树 | 可选 |
| **secfilter** | 安全过滤 | 可选 |
| **presence** | 存在服务 | 可选 |
| **msilo** | 离线消息 | 可选 |
| **uac** | UAC 注册 | 可选 |
| **rtpengine** | RTPEngine | 可选 |
| **carrierroute** | 运营商路由 | 可选 |
| **dialplan** | 拨号计划 | 可选 |
| **lcr** | LCR 路由 | 可选 |

**最小配置示例**（仅保证 Dashboard 可连接与基本监控）：

```
loadmodule "xhttp.so"
loadmodule "jsonrpcs.so"
loadmodule "usrloc.so"
loadmodule "sl.so"
# ... 其他业务所需模块
```

**系统管理页完整功能** 需额外加载：`cfg_rpc`、`statistics`、`tls`、`pike`。

---

## 5. 数据库配置

### 5.1 创建数据库用户

```sql
CREATE USER 'kamailio_admin'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON kamailio.* TO 'kamailio_admin'@'localhost';
FLUSH PRIVILEGES;
```

### 5.2 数据库优化

```sql
-- 添加索引优化查询
ALTER TABLE location ADD INDEX idx_username (username);
ALTER TABLE acc ADD INDEX idx_time (time);
ALTER TABLE sip_trace ADD INDEX idx_callid (callid);
ALTER TABLE sip_trace ADD INDEX idx_time_stamp (time_stamp);
```

---

## 6. 安全配置

### 6.1 防火墙配置

```bash
# 允许 HTTP/HTTPS
ufw allow 80/tcp
ufw allow 443/tcp

# 限制后端 API 端口（仅本地访问）
ufw deny 3000/tcp

# 限制 Kamailio RPC（仅本地访问）
ufw deny 5060/tcp
```

### 6.2 HTTPS 证书

使用 Let's Encrypt：

```bash
# 安装 certbot
apt install certbot python3-certbot-nginx

# 获取证书
certbot --nginx -d dashboard.example.com

# 自动续期
certbot renew --dry-run
```

### 6.3 API 安全

1. 配置 CORS 白名单
2. 添加认证中间件
3. 使用 HTTPS
4. 限制 API 访问频率

---

## 7. 监控和日志

### 7.1 后端服务监控（systemd）

```bash
# 查看状态
systemctl status kam-dashboard-api

# 查看日志
journalctl -u kam-dashboard-api -f -n 100

# 资源占用
systemctl status kam-dashboard-api  # 或结合 top/htop 按 PID 查看
```

### 7.2 日志配置

**后端日志：**

Spring Boot 默认输出到 stdout，由 systemd 的 journald 接管；也可在 `application.yml` 中配置 `logging.file.name` 输出到文件。

**Nginx 日志：**

```nginx
access_log /var/log/nginx/kam-dashboard.access.log;
error_log /var/log/nginx/kam-dashboard.error.log;
```

### 7.3 日志轮转

创建 `/etc/logrotate.d/kam-dashboard`：

```
/var/log/kam-dashboard/*.log {
    daily
    missingok
    rotate 14
    compress
    delaycompress
    notifempty
    create 0640 www-data www-data
}
```

---

## 8. 备份策略

### 8.1 数据库备份

```bash
#!/bin/bash
# /opt/scripts/backup-db.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=/backup/kamailio
mkdir -p $BACKUP_DIR

mysqldump -u kamailio -p'password' kamailio | gzip > $BACKUP_DIR/kamailio_$DATE.sql.gz

# 保留 30 天
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete
```

添加到 crontab：

```bash
0 2 * * * /opt/scripts/backup-db.sh
```

### 8.2 配置备份

```bash
#!/bin/bash
# /opt/scripts/backup-config.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR=/backup/config
mkdir -p $BACKUP_DIR

tar -czf $BACKUP_DIR/kam-admin-config_$DATE.tar.gz \
    /opt/kam-admin/kam-admin-server/src/main/resources/application*.yml \
    /etc/kamailio/kamailio.cfg \
    /etc/nginx/sites-available/kam-dashboard

find $BACKUP_DIR -name "*.tar.gz" -mtime +30 -delete
```

---

## 9. 故障排查

### 9.1 后端无法启动

```bash
# 检查服务状态与日志（systemd）
systemctl status kam-dashboard-api
journalctl -u kam-dashboard-api -n 100 --no-pager

# 检查端口占用
lsof -i :3000

# 检查数据库连接
mysql -u kamailio -p -h localhost kamailio
```

### 9.2 前端无法访问

```bash
# 检查 Nginx 配置
nginx -t

# 检查 Nginx 日志
tail -f /var/log/nginx/error.log

# 检查文件权限
ls -la /var/www/kam-dashboard/
```

### 9.3 RPC 调用失败

```bash
# 测试 RPC 连接
curl -v http://localhost:5060/RPC

# 检查 Kamailio 日志
tail -f /var/log/kamailio.log

# 检查 Kamailio 进程
kamctl ps
```

---

## 10. 性能优化

### 10.1 JVM 优化

```bash
# 启动时设置堆内存等（示例）
java -Xms512m -Xmx1024m -jar kam-admin-server/target/kam-admin-server-0.0.1.jar
```

systemd 的 `ExecStart` 中可写为：
`ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/kam-admin/kam-admin-server/target/kam-admin-server-0.0.1.jar`

### 10.2 数据库连接池

在 `application.yml` 或环境变量中配置（示例）：

```yaml
spring.datasource.hikari.maximum-pool-size: 10
```

### 10.3 Nginx 优化

```nginx
# 启用 Gzip
gzip on;
gzip_types text/plain text/css application/json application/javascript;

# 启用缓存
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=api_cache:10m;
```

---

## 11. 升级指南

### 11.1 后端升级

```bash
cd /opt/kam-admin

# 备份当前 JAR
cp kam-admin-server/target/kam-admin-server-0.0.1.jar kam-admin-server/target/kam-admin-server-0.0.1.jar.bak

# 拉取新代码
git pull origin main

# 构建（需 JDK 21）
mvn clean package -pl kam-admin-server -am

# 重启服务
systemctl restart kam-dashboard-api
```

### 11.2 前端升级

```bash
cd /opt/kam-admin

# 拉取新代码
git pull origin main

# 前端构建（仅前端子工程使用 pnpm）
cd kam-admin-console && pnpm install && pnpm run build

# 部署
cp -r kam-admin-console/dist/* /var/www/kam-dashboard/
```

### 11.3 回滚

```bash
# 后端回滚
cp kam-admin-server/target/kam-admin-server-0.0.1.jar.bak kam-admin-server/target/kam-admin-server-0.0.1.jar
systemctl restart kam-dashboard-api

# 前端回滚
cp -r /var/www/kam-dashboard.bak/* /var/www/kam-dashboard/
```
