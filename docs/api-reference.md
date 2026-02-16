# API 参考文档

## 概述

本文档提供 Kamailio Dashboard 后端 API 的完整参考。后端为 **kam-admin-server**（Spring Boot 3），对内管理 API 前缀为 `/api`，对外开放 API 前缀为 `/open/v1`（需 API Key 鉴权，见 [架构文档](./architecture-api-cluster-nacos-redis.md)）。

**基础 URL（对内）**: `http://localhost:3000/api`（直连后端）或 `http://localhost/api`（经 Nginx 代理）  
**开放 API**: `http://localhost:3000/open/v1` 或 `http://localhost/open/v1`  
**Swagger 文档**: `http://localhost:3000/api/docs` 或 `http://localhost/api/docs`

---

## 通用说明

### 响应格式

所有 API 返回统一的 JSON 格式：

```json
{
  "code": 0,
  "message": "success",
  "data": { ... },
  "timestamp": "2025-02-16T12:00:00.000Z"
}
```

（`timestamp` 为 ISO-8601 字符串。）

### 状态码

| code | 说明 |
|------|------|
| 0 | 成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

### 分页参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | number | 1 | 页码 |
| limit | number | 20 | 每页数量 |
| keyword | string | - | 搜索关键字 |

### 分页响应

```json
{
  "items": [...],
  "total": 100,
  "page": 1,
  "limit": 20,
  "totalPages": 5
}
```

---

## 用户管理 API

### 获取用户列表

```
GET /subscribers
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| page | number | 页码 |
| limit | number | 每页数量 |
| keyword | string | 搜索用户名 |

**响应：**

```json
{
  "code": 0,
  "data": {
    "items": [
      {
        "id": 1,
        "username": "1001",
        "domain": "example.com",
        "emailAddress": "user@example.com"
      }
    ],
    "total": 100,
    "page": 1,
    "limit": 20
  }
}
```

### 创建用户

```
POST /subscribers
```

**请求体：**

```json
{
  "username": "1001",
  "domain": "example.com",
  "password": "secret123",
  "emailAddress": "user@example.com"
}
```

### 更新用户

```
PATCH /subscribers/:id
```

**请求体：**

```json
{
  "password": "newpassword",
  "emailAddress": "new@example.com"
}
```

### 删除用户

```
DELETE /subscribers/:id
```

### 获取用户统计

```
GET /subscribers/stats
```

---

## 域管理 API

### 获取域列表

```
GET /domains
```

### 创建域

```
POST /domains
```

**请求体：**

```json
{
  "domain": "example.com",
  "description": "主域"
}
```

### 重载域配置

```
POST /domains/reload
```

---

## 调度器 API

### 获取调度器列表

```
GET /dispatchers
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| setId | number | 调度组 ID |

### 创建调度目标

```
POST /dispatchers
```

**请求体：**

```json
{
  "setid": 1,
  "destination": "sip:192.168.1.100:5060",
  "flags": 0,
  "priority": 0,
  "weight": 100,
  "description": "Media Server 1"
}
```

### 重载调度器

```
POST /dispatchers/reload
```

### 获取调度器状态

```
GET /dispatchers/status
```

---

## 动态路由 API

### 网关管理

```
GET    /drouting/gateways      # 获取网关列表
POST   /drouting/gateways      # 创建网关
PATCH  /drouting/gateways/:id  # 更新网关
DELETE /drouting/gateways/:id  # 删除网关
```

**网关字段：**

```json
{
  "gwid": "gw1",
  "type": 0,
  "address": "sip:10.0.0.1:5060",
  "strip": 0,
  "priPrefix": "",
  "attrs": "",
  "probeMode": 0,
  "state": 0,
  "description": "Gateway 1"
}
```

### 规则管理

```
GET    /drouting/rules      # 获取规则列表
POST   /drouting/rules      # 创建规则
PATCH  /drouting/rules/:id  # 更新规则
DELETE /drouting/rules/:id  # 删除规则
```

**规则字段：**

```json
{
  "groupid": "1",
  "prefix": "86",
  "timerec": "",
  "priority": 0,
  "routeid": "",
  "gwlist": "gw1,gw2",
  "attrs": "",
  "description": "China route"
}
```

### 重载动态路由

```
POST /drouting/reload
```

### 获取统计

```
GET /drouting/stats
```

---

## LCR 路由 API

### 网关管理

```
GET /lcr/gateways
```

### 规则管理

```
GET /lcr/rules
```

### 目标管理

```
GET /lcr/targets
```

### 重载 LCR

```
POST /lcr/reload
```

---

## 用户位置 API

### 获取注册用户

```
GET /locations
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| keyword | string | 搜索用户名 |

### 从内存加载

```
GET /locations/memory
```

### 删除联系地址

```
DELETE /locations/contact
```

**请求体：**

```json
{
  "table": "location",
  "aor": "sip:1001@example.com",
  "contact": "sip:1001@192.168.1.100:5060"
}
```

---

## 对话管理 API

### 获取数据库对话

```
GET /dialogs
```

### 获取活动对话

```
GET /dialogs/active
```

### 获取对话统计

```
GET /dialogs/stats
```

### 结束对话

```
POST /dialogs/end
```

**请求体：**

```json
{
  "hashEntry": 123,
  "hashId": 456
}
```

---

## CDR 记录 API

### 获取 CDR 记录

```
GET /acc/cdr
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| srcUser | string | 主叫用户 |
| dstUser | string | 被叫用户 |
| callid | string | Call-ID |
| startTime | string | 开始时间 |
| endTime | string | 结束时间 |
| sipCode | string | SIP 状态码 |

### 获取未接来电

```
GET /acc/missed-calls
```

### 获取统计

```
GET /acc/stats
```

### 获取今日统计

```
GET /acc/today
```

---

## SIP 跟踪 API

### 获取跟踪记录

```
GET /siptrace
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| callid | string | Call-ID |
| tracedUser | string | 跟踪用户 |
| method | string | SIP 方法 |
| fromIp | string | 来源 IP |
| toIp | string | 目标 IP |
| startTime | string | 开始时间 |
| endTime | string | 结束时间 |

### 获取呼叫跟踪

```
GET /siptrace/call?callid=xxx
```

### 获取统计

```
GET /siptrace/stats
```

### 清理旧记录

```
POST /siptrace/cleanup
```

**请求体：**

```json
{
  "days": 30
}
```

---

## 哈希表 API

### 获取数据库记录

```
GET /htable
```

### RPC 操作

```
GET  /htable/rpc/get?table=xxx&key=xxx     # 获取值
POST /htable/rpc/sets                       # 设置字符串值
POST /htable/rpc/seti                       # 设置整数值
POST /htable/rpc/delete                     # 删除键
GET  /htable/rpc/dump?table=xxx             # 导出表
POST /htable/rpc/reload                     # 重载表
GET  /htable/rpc/tables                     # 列出表
```

---

## 系统管理 API

### 系统信息

```
GET /system/info        # 核心信息
GET /system/version     # 版本
GET /system/uptime      # 运行时间
GET /system/memory      # 内存信息
GET /system/processes   # 进程列表
GET /system/modules     # 模块列表
GET /system/status      # 综合状态
```

### 统计信息

```
GET /system/statistics?group=xxx  # 获取统计
POST /system/statistics/reset     # 重置统计
```

### TLS 管理

```
GET  /system/tls/list    # TLS 连接列表
GET  /system/tls/info    # TLS 信息
POST /system/tls/reload  # 重载 TLS
```

### Pike 防护

```
GET /system/pike/list         # 封禁列表
GET /system/pike/top?limit=10 # Top N
```

### 配置管理

```
GET  /system/config/list              # 列出配置
GET  /system/config?group=x&name=y    # 获取配置
POST /system/config                   # 设置配置
```

---

## RTPEngine API

### 获取状态

```
GET /rtpengine/status
```

### 显示 RTPEngine

```
GET /rtpengine/show
```

### 重载配置

```
POST /rtpengine/reload
```

### 启用/禁用

```
POST /rtpengine/enable
```

**请求体：**

```json
{
  "url": "udp:127.0.0.1:2223",
  "flag": 1
}
```

---

## 监控 API

### 健康检查

```
GET /monitoring/health
```

### 仪表板数据

```
GET /monitoring/dashboard
```

### 系统概览

```
GET /monitoring/overview
```

---

## 拨号计划 API

### 获取规则

```
GET /dialplan
```

**参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| dpid | number | 拨号计划 ID |

### 创建规则

```
POST /dialplan
```

**请求体：**

```json
{
  "dpid": 1,
  "priority": 0,
  "matchOp": 1,
  "matchExp": "^00(.*)$",
  "matchLen": 0,
  "substExp": "\\1",
  "replExp": "+\\1",
  "attrs": ""
}
```

### 重载

```
POST /dialplan/reload
```

### 测试翻译

```
GET /dialplan/translate?dpid=1&input=008613800000000
```

---

## 安全过滤 API

### 获取规则

```
GET /secfilter
```

### 创建规则

```
POST /secfilter
```

**请求体：**

```json
{
  "action": 0,
  "type": 3,
  "data": "192.168.1.100"
}
```

### 重载

```
POST /secfilter/reload
```

### 获取统计

```
GET /secfilter/stats
```

### 添加黑名单

```
POST /secfilter/add-bl
```

**请求体：**

```json
{
  "type": 3,
  "data": "192.168.1.100"
}
```

### 添加白名单

```
POST /secfilter/add-wl
```

---

## 数据库版本 API

### 获取所有版本

```
GET /version
```

### 获取指定表版本

```
GET /version/table?name=subscriber
```

### 获取统计

```
GET /version/stats
```
