# 部署指南

## 概述

本文档介绍如何在生产环境部署 Kamailio Dashboard。

---

## 1. 环境要求

### 1.1 硬件要求

| 配置 | 最低要求 | 推荐配置 |
|------|---------|---------|
| CPU | 2 核 | 4 核+ |
| 内存 | 2 GB | 4 GB+ |
| 磁盘 | 20 GB | 50 GB+ |

### 1.2 软件要求

| 软件 | 版本 |
|------|------|
| Node.js | >= 18.0.0 |
| pnpm | >= 8.0.0 |
| MySQL/MariaDB | 5.7+ / 10.x |
| Kamailio | 5.x |
| Nginx | 1.18+ (可选) |

---

## 2. 后端部署

### 2.1 下载代码

```bash
git clone https://github.com/lixuanqun/kam-admin.git
cd kam-admin/backend
```

### 2.2 安装依赖

```bash
npm install --production
```

### 2.3 配置环境变量

创建 `.env` 文件：

```env
# 服务配置
NODE_ENV=production
PORT=3000

# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=kamailio
DB_PASSWORD=your_secure_password
DB_DATABASE=kamailio
DB_LOGGING=false

# Kamailio RPC 配置
KAMAILIO_RPC_HOST=localhost
KAMAILIO_RPC_PORT=5060
KAMAILIO_RPC_PATH=/RPC
```

### 2.4 构建项目

```bash
npm run build
```

### 2.5 启动服务

**使用 PM2 管理（推荐）：**

```bash
# 安装 PM2
npm install -g pm2

# 启动服务
pm2 start dist/main.js --name kam-dashboard-api

# 设置开机自启
pm2 startup
pm2 save
```

**使用 systemd：**

创建 `/etc/systemd/system/kam-dashboard-api.service`：

```ini
[Unit]
Description=Kamailio Dashboard API
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/kam-admin/backend
ExecStart=/usr/bin/node dist/main.js
Restart=on-failure
Environment=NODE_ENV=production

[Install]
WantedBy=multi-user.target
```

启动服务：

```bash
systemctl daemon-reload
systemctl enable kam-dashboard-api
systemctl start kam-dashboard-api
```

---

## 3. 前端部署

### 3.1 安装依赖

```bash
cd kam-admin/frontend
pnpm install
```

### 3.2 配置环境

编辑 `apps/web-antd/.env.production`：

```env
VITE_GLOB_API_URL=https://api.example.com
```

### 3.3 构建项目

```bash
pnpm run build:antd
```

构建产物位于 `apps/web-antd/dist` 目录。

### 3.4 部署到 Nginx

**复制文件：**

```bash
cp -r apps/web-antd/dist/* /var/www/kam-dashboard/
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

### 7.1 PM2 监控

```bash
# 查看状态
pm2 status

# 查看日志
pm2 logs kam-dashboard-api

# 监控面板
pm2 monit
```

### 7.2 日志配置

**后端日志：**

使用 Winston 或 Pino 配置日志输出到文件。

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
    sharedscripts
    postrotate
        pm2 reloadLogs
    endscript
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
    /opt/kam-admin/backend/.env \
    /etc/kamailio/kamailio.cfg \
    /etc/nginx/sites-available/kam-dashboard

find $BACKUP_DIR -name "*.tar.gz" -mtime +30 -delete
```

---

## 9. 故障排查

### 9.1 后端无法启动

```bash
# 检查日志
pm2 logs kam-dashboard-api --lines 100

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

### 10.1 Node.js 优化

```bash
# 设置 Node.js 内存限制
NODE_OPTIONS="--max-old-space-size=4096" pm2 start dist/main.js
```

### 10.2 数据库连接池

在 `.env` 中配置：

```env
DB_POOL_SIZE=10
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
cd /opt/kam-admin/backend

# 备份当前版本
cp -r dist dist.bak

# 拉取新代码
git pull origin main

# 安装依赖
npm install

# 构建
npm run build

# 重启服务
pm2 restart kam-dashboard-api
```

### 11.2 前端升级

```bash
cd /opt/kam-admin/frontend

# 拉取新代码
git pull origin main

# 安装依赖
pnpm install

# 构建
pnpm run build:antd

# 部署
cp -r apps/web-antd/dist/* /var/www/kam-dashboard/
```

### 11.3 回滚

```bash
# 后端回滚
cd /opt/kam-admin/backend
rm -rf dist
mv dist.bak dist
pm2 restart kam-dashboard-api

# 前端回滚
cp -r /var/www/kam-dashboard.bak/* /var/www/kam-dashboard/
```
