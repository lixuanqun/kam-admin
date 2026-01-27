# 系统管理操作手册

## 概述

系统管理模块用于管理 Kamailio 的系统配置、安全设置、RTPEngine 等功能。

---

## 1. 系统管理 (/kamailio/system)

### 1.1 系统概览

**查看信息：**

1. 进入 **系统管理** 页面
2. **系统概览** 标签显示：
   - 进程数量
   - 运行时间
   - 版本信息
   - 内存使用情况

### 1.2 进程列表

查看 Kamailio 所有工作进程：

| 字段 | 说明 |
|------|------|
| ID | 进程 ID |
| PID | 系统 PID |
| 描述 | 进程功能描述 |

### 1.3 加载的模块

查看当前加载的所有 Kamailio 模块列表。

### 1.4 相关 RPC 指令

```
core.info - 获取核心信息
core.version - 获取版本
core.uptime - 获取运行时间
core.shmmem - 获取共享内存信息
core.psx - 获取进程列表
core.modules - 获取模块列表
```

---

## 2. TLS 管理

### 2.1 查看 TLS 连接

1. 在 **系统管理** 页面选择 **TLS 管理** 标签
2. 查看当前 TLS 连接列表

### 2.2 TLS 信息

查看 TLS 模块配置和证书信息。

### 2.3 重载 TLS

点击 **重载 TLS** 按钮重新加载 TLS 配置和证书。

### 2.4 相关 RPC 指令

```
tls.list - 列出 TLS 连接
tls.info - 获取 TLS 信息
tls.reload - 重载 TLS 配置
```

---

## 3. Pike 防护

### 3.1 功能说明

Pike 模块用于防止 SIP 洪水攻击，自动封禁异常 IP。

### 3.2 查看封禁列表

1. 选择 **Pike 防护** 标签
2. 查看当前被封禁的 IP 列表

### 3.3 Pike Top

查看请求最频繁的 IP 列表（Top 20）。

### 3.4 相关 RPC 指令

```
pike.list - 列出封禁 IP
pike.top <n> - 获取 Top N
```

### 3.5 Pike 配置

在 Kamailio 配置中：

```
loadmodule "pike.so"

modparam("pike", "sampling_time_unit", 2)
modparam("pike", "reqs_density_per_unit", 30)
modparam("pike", "remove_latency", 120)

route {
    if (!pike_check_req()) {
        xlog("L_ALERT", "PIKE: $si blocked\n");
        exit;
    }
    ...
}
```

---

## 4. 统计信息

### 4.1 查看统计

1. 选择 **统计信息** 标签
2. 查看各模块的统计数据

### 4.2 统计分组

| 分组 | 说明 |
|------|------|
| core | 核心统计 |
| shmem | 共享内存 |
| dns | DNS 统计 |
| sl | 无状态回复 |
| tm | 事务管理 |
| usrloc | 用户位置 |
| registrar | 注册服务 |

### 4.3 相关 RPC 指令

```
stats.get_statistics <group> - 获取统计
stats.reset_statistics <name> - 重置统计
stats.clear_statistics <name> - 清除统计
```

---

## 5. 配置管理

### 5.1 查看配置

1. 选择 **配置管理** 标签
2. 查看所有可配置参数

### 5.2 修改配置

> 注意：运行时修改配置需谨慎操作

1. 选择配置组和参数名
2. 设置新值
3. 应用更改

### 5.3 相关 RPC 指令

```
cfg.list - 列出所有配置
cfg.get <group> <name> - 获取配置值
cfg.set_now_int <group> <name> <value> - 设置整数值
cfg.set_now_string <group> <name> <value> - 设置字符串值
cfg.diff - 查看配置差异
```

---

## 6. RTPEngine 管理 (/kamailio/rtpengine)

### 6.1 功能说明

RTPEngine 用于媒体代理，处理 NAT 穿透和媒体转码。

### 6.2 查看状态

1. 进入 **RTPEngine** 页面
2. 查看 RTPEngine 连接状态

### 6.3 管理操作

| 操作 | 说明 |
|------|------|
| 刷新 | 刷新状态信息 |
| 重载配置 | 重新加载 RTPEngine 配置 |
| 启用/禁用 | 启用或禁用指定 RTPEngine |

### 6.4 相关 RPC 指令

```
rtpengine.show all - 显示所有 RTPEngine
rtpengine.reload - 重载配置
rtpengine.enable <url> <flag> - 启用/禁用
rtpengine.ping <url> - Ping 测试
```

### 6.5 RTPEngine 配置

在 Kamailio 配置中：

```
loadmodule "rtpengine.so"

modparam("rtpengine", "rtpengine_sock", "udp:127.0.0.1:2223")

route {
    if (is_method("INVITE")) {
        rtpengine_manage("replace-session-connection replace-origin");
    }
    ...
}
```

---

## 7. 权限管理 (/kamailio/permissions)

### 7.1 功能说明

权限管理用于控制 IP 地址的访问权限，包括 IP 白名单和可信源。

### 7.2 地址表（IP 白名单）

**新增地址：**

1. 进入 **权限管理** 页面
2. 在 **地址表** 标签点击 **新增**
3. 填写：
   - **分组 ID**: 地址分组
   - **IP 地址**: 允许的 IP
   - **掩码**: 子网掩码
   - **端口**: 端口（0 表示任意）
   - **标签**: 描述标签
4. 保存

**IP 格式：**

```
# 单个 IP
192.168.1.100

# 子网
192.168.1.0 (掩码 24)

# 任意 IP
0.0.0.0 (掩码 0)
```

### 7.3 可信源

可信源用于标识可信的 SIP 源。

### 7.4 重载权限

点击 **重载配置** 按钮使更改生效。

### 7.5 相关 RPC 指令

```
permissions.addressReload - 重载地址表
permissions.trustedReload - 重载可信源
permissions.addressDump - 导出地址表
permissions.subnetDump - 导出子网表
```

---

## 8. 安全过滤 (/kamailio/secfilter)

### 8.1 功能说明

安全过滤用于过滤恶意的 SIP 请求。

### 8.2 过滤类型

| 类型 | 说明 |
|------|------|
| 0 | User-Agent |
| 1 | Country |
| 2 | Domain |
| 3 | IP |
| 4 | User |

### 8.3 过滤动作

| 动作 | 说明 |
|------|------|
| 0 | 拒绝（黑名单） |
| 1 | 接受（白名单） |

### 8.4 新增规则

1. 进入 **安全过滤** 页面
2. 点击 **新增**
3. 选择动作、类型，填写数据
4. 保存

### 8.5 快速添加到内存

1. 点击 **快速添加** 按钮
2. 选择黑名单或白名单
3. 填写类型和数据
4. 直接添加到 Kamailio 内存

### 8.6 相关 RPC 指令

```
secfilter.reload - 重载配置
secfilter.print - 打印规则
secfilter.stats - 获取统计
secfilter.stats_reset - 重置统计
secfilter.add_bl <type> <data> - 添加黑名单
secfilter.add_wl <type> <data> - 添加白名单
```

---

## 9. 哈希表 (/kamailio/htable)

### 9.1 功能说明

哈希表用于存储键值对数据，可用于：
- 黑名单管理
- 计数器
- 临时数据存储

### 9.2 数据库记录

查看数据库中存储的 htable 记录。

### 9.3 RPC 操作

| 操作 | 说明 |
|------|------|
| 查询值 | 获取指定键的值 |
| 设置值 | 设置键值对 |
| 删除键 | 删除指定键 |
| 重载表 | 从数据库重载表数据 |
| 导出表 | 导出整个表的内容 |

### 9.4 相关 RPC 指令

```
htable.get <table> <key> - 获取值
htable.sets <table> <key> <value> - 设置字符串值
htable.seti <table> <key> <value> - 设置整数值
htable.delete <table> <key> - 删除键
htable.dump <table> - 导出表
htable.reload <table> - 重载表
htable.listTables - 列出所有表
```

---

## 10. 数据库版本 (/kamailio/version)

### 10.1 功能说明

查看 Kamailio 数据库各表的版本信息。

### 10.2 查看版本

进入 **数据库版本** 页面查看所有表的版本号。

### 10.3 版本用途

版本表用于：
- 数据库升级检查
- 模块兼容性验证
- 故障排查

---

## 11. 系统维护

### 11.1 日常维护任务

| 任务 | 频率 | 说明 |
|------|------|------|
| 检查系统状态 | 每日 | 确保服务正常运行 |
| 查看内存使用 | 每日 | 监控内存泄漏 |
| 清理日志 | 每周 | 防止磁盘满 |
| 清理跟踪数据 | 每周 | 清理过期数据 |
| 备份数据库 | 每日 | 防止数据丢失 |

### 11.2 性能优化

1. 合理配置共享内存大小
2. 调整 worker 进程数量
3. 优化数据库连接池

### 11.3 安全加固

1. 启用 Pike 防护
2. 配置 IP 白名单
3. 启用安全过滤
4. 使用 TLS 加密

---

## 12. 故障排查

### Kamailio 无响应

1. 检查进程是否运行
2. 查看系统日志
3. 检查内存使用情况

### RPC 调用失败

1. 检查 JSONRPC 模块配置
2. 确认端口可访问
3. 查看错误信息

### 数据库连接问题

1. 检查数据库服务
2. 验证连接参数
3. 检查用户权限
