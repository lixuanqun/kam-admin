# Kamailio SKILL

> 基于 [Kamailio 官方文档](https://www.kamailio.org/w/documentation/) 总结的配置与知识技能

---

## 一、Kamailio 概述

Kamailio 是开源的 **SIP (Session Initiation Protocol) 服务器**，用于构建 VoIP、即时通讯、WebRTC 等实时通信系统。

| 特性 | 说明 |
|------|------|
| 协议 | SIP (RFC 3261) |
| 许可证 | GPL v2 |
| 语言 | C |
| 典型用途 | 注册服务器、代理、负载均衡、SBC、计费 |

---

## 二、官方文档入口

| 资源 | 链接 |
|------|------|
| 文档首页 | https://www.kamailio.org/w/documentation/ |
| 模块文档 | https://kamailio.org/docs/modules/ (按版本 5.x/6.0.x) |
| Wiki 文档 | https://www.kamailio.org/wikidocs/ |
| Core Cookbook | https://www.kamailio.org/wikidocs/cookbooks/6.0.x/core/ |
| Pseudo-Variables | https://www.kamailio.org/wikidocs/cookbooks/6.0.x/pseudovariables/ |
| Transformations | https://www.kamailio.org/wikidocs/cookbooks/6.0.x/transformations/ |
| 安装指南 | https://kamailio.org/docs/tutorials/ |
| FAQ | https://www.kamailio.org/wikidocs/tutorials/faq/main/ |

---

## 三、kamailio.cfg 结构

配置文件分为三部分：

### 3.1 全局参数 (Global Parameters)

```cfg
# 核心参数示例
debug=3
log_stderror=no
listen=udp:0.0.0.0:5060
listen=tcp:0.0.0.0:5060
```

### 3.2 模块配置 (Modules Settings)

```cfg
loadmodule "sl.so"
loadmodule "rr.so"
loadmodule "usrloc.so"
loadmodule "registrar.so"

# 模块参数
modparam("usrloc", "db_mode", 0)
modparam("registrar", "method_filtering", 1)
```

### 3.3 路由块 (Routing Blocks)

```cfg
request_route {
    # 主请求路由
}

onreply_route {
    # 应答路由
}

failure_route {
    # 失败路由
}
```

**唯一必需的路由块**：`request_route`

---

## 四、预处理器指令

| 指令 | 说明 |
|------|------|
| `#!include_file "path"` | 包含文件内容，解析前替换 |
| `#!import_file "path"` | 包含文件，且可避免重复 |
| `#!define ID value` | 定义宏 |
| `#!ifdef ID` / `#!endif` | 条件编译 |
| `#!ifexp EXPR` | 表达式条件 |
| `#!defenv VAR` | 从环境变量定义 |
| `#!subst /regex/replacement/` | 正则替换 |

示例：

```cfg
#!define WITH_JSONRPC
#!ifdef WITH_JSONRPC
loadmodule "jsonrpcs.so"
#!endif
```

---

## 五、JSONRPC 配置（管理接口）

Dashboard 通过 JSONRPC 与 Kamailio 交互，需启用 `jsonrpcs` 和 `xhttp` 模块。

### 5.1 必需模块

```cfg
loadmodule "xhttp.so"
loadmodule "jsonrpcs.so"
```

### 5.2 模块参数

| 参数 | 含义 | 示例 |
|------|------|------|
| pretty_format | 是否美化 JSON 输出 | 1 |
| transport | 1=HTTP, 2=FIFO 等 | 1 |
| fifo_name | FIFO 路径（FIFO 模式） | /var/run/kamailio/kamailio_rpc.fifo |

### 5.3 HTTP 事件路由

```cfg
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

### 5.4 监听配置

```cfg
# 需监听 TCP 以支持 HTTP/xhttp
listen=tcp:0.0.0.0:5060
```

### 5.5 RPC 调用示例

```bash
curl -X POST http://localhost:5060/RPC \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"core.version","params":[],"id":1}'
```

---

## 六、常用模块与 RPC 方法

### 6.1 核心 (core)

| RPC 方法 | 说明 |
|----------|------|
| core.version | 版本号 |
| core.info | 核心信息 |
| core.uptime | 运行时间 |
| core.shmmem | 共享内存 |
| core.modules | 已加载模块 |
| core.psx | 进程信息 |

### 6.2 用户位置 (usrloc / ul)

| RPC 方法 | 说明 |
|----------|------|
| ul.dump | 导出内存用户位置 |
| ul.lookup | 查找指定 AOR 的注册 |
| ul.rm | 删除用户位置 |
| ul.rm_contact | 删除指定联系地址 |

### 6.3 调度器 (dispatcher)

| RPC 方法 | 说明 |
|----------|------|
| dispatcher.reload | 重载调度器配置 |
| dispatcher.list | 列出调度器 |
| dispatcher.set_state | 设置目标状态 |

### 6.4 动态路由 (drouting)

| RPC 方法 | 说明 |
|----------|------|
| drouting.reload | 重载路由 |
| drouting.gw_status | 网关状态 |
| drouting.carrier_status | 运营商状态 |

### 6.5 对话 (dialog / dlg)

| RPC 方法 | 说明 |
|----------|------|
| dlg.list | 活动对话列表 |
| dlg.stats_active | 活动对话统计 |
| dlg.dlg_list | 对话详情 |
| dlg.end_dlg | 结束对话 |
| dlg.bridge_dlg | 桥接/转接对话 |

### 6.6 权限 (permissions)

| RPC 方法 | 说明 |
|----------|------|
| permissions.addressReload | 重载地址表 |
| permissions.trustedReload | 重载信任源 |
| permissions.addressDump | 导出地址 |
| permissions.subnetDump | 导出子网 |

### 6.7 域 (domain)

| RPC 方法 | 说明 |
|----------|------|
| domain.reload | 重载域 |
| domain.dump | 导出内存域列表 |

### 6.8 哈希表 (htable)

| RPC 方法 | 说明 |
|----------|------|
| htable.get | 获取键值 |
| htable.sets | 设置字符串 |
| htable.seti | 设置整数 |
| htable.delete | 删除键 |
| htable.dump | 导出表 |
| htable.reload | 重载表 |
| htable.listTables | 列出表 |
| htable.stats | 统计 |

### 6.9 其他模块

| 模块 | 常用 RPC |
|------|----------|
| pdt | pdt.reload, pdt.list |
| mtree | mtree.reload, mtree.match, mtree.summary |
| secfilter | secfilter.reload, secfilter.print, secfilter.add_bl |
| presence | presence.cleanup, presence.refreshWatchers |
| msilo | msilo.dump |
| uac | uac.reg_reload, uac.reg_info, uac.reg_dump |
| rtpengine | rtpengine.show, rtpengine.reload |
| lcr | lcr.reload, lcr.dump_gws, lcr.dump_rules |
| cfg | cfg.get, cfg.set_now_int, cfg.list |
| tls | tls.list, tls.reload |
| pike | pike.list, pike.top |
| stats | stats.get_statistics, stats.reset_statistics |

---

## 七、伪变量 (Pseudo-Variables)

| 变量 | 说明 |
|------|------|
| $ru | Request URI |
| $fu | From URI |
| $tu | To URI |
| $du | Destination URI |
| $src_ip | 源 IP |
| $src_port | 源端口 |
| $dst_ip | 目标 IP |
| $dst_port | 目标端口 |
| $proto | 协议 (UDP/TCP/TLS) |
| $method | SIP 方法 (INVITE, REGISTER 等) |
| $Hu | 原始 Host 头 |
| $T | 当前时间 |
| $Ts | 当前时间戳 |

完整列表见：https://www.kamailio.org/wikidocs/cookbooks/6.0.x/pseudovariables/

---

## 八、数据库与 Kamailio

Kamailio 支持多种数据库：MySQL、PostgreSQL、SQLite 等。常用表：

| 表 | 说明 |
|------|------|
| subscriber | 用户账号 |
| domain | 域 |
| dispatcher | 调度器目标 |
| dr_gateways | 动态路由网关 |
| dr_rules | 动态路由规则 |
| acc | 计费记录 |
| location | 用户位置（可内存） |

---

## 九、常见配置模式

### 9.1 注册流程

```cfg
request_route {
    if (method == "REGISTER") {
        if (!save("location")) {
            sl_reply_error();
        }
        exit;
    }
    # ...
}
```

### 9.2 鉴权

```cfg
loadmodule "auth_db.so"
# ...
if (!proxy_authorize("", "subscriber")) {
    proxy_challenge("", "0");
    exit;
}
```

### 9.3 负载均衡

```cfg
loadmodule "dispatcher.so"
# 从数据库加载
modparam("dispatcher", "db_url", "mysql://...")
# 或使用 drouting、carrierroute
```

### 9.4 记录计费 (ACC)

```cfg
loadmodule "acc.so"
# ...
acc_log_request("acc");
```

---

## 十、本 Dashboard 相关 RPC 映射

| 业务模块 | 后端服务 | 主要 RPC 调用 |
|----------|----------|---------------|
| 用户管理 | 数据库 + location | ul.dump, ul.lookup |
| 调度器 | dispatcher 表 | dispatcher.reload, dispatcher.list |
| 动态路由 | drouting 表 | drouting.reload, drouting.gw_status |
| 域名 | domain 表 | domain.reload, domain.dump |
| 权限 | permissions 表 | permissions.addressReload |
| 对话 | 内存 | dlg.list, dlg.end_dlg |
| 监控 | 多模块 | core.uptime, stats.get_statistics |
| 系统 | 多模块 | core.info, tls.list, pike.list |

---

## 十一、安装与运维

### 11.1 安装（从源码）

```bash
# 参考官方教程
# https://kamailio.org/docs/tutorials/6.0.x/kamailio-install-guide-git/
```

### 11.2 常用命令

```bash
kamctl start    # 启动
kamctl stop     # 停止
kamctl restart  # 重启
kamctl status   # 状态
kamctl db show  # 查看数据库表
```

### 11.3 常见问题

- **RPC 无法连接**：检查 jsonrpcs、xhttp 是否加载，listen 是否含 TCP
- **401 鉴权失败**：检查 auth_db、subscriber 表
- **注册失败**：检查 usrloc、registrar、权限、防火墙

---

## 十二、扩展资源

| 类型 | 链接 |
|------|------|
| Kamailio + Asterisk | http://kb.asipto.com/asterisk:index |
| Kamailio + FreeSwitch | http://kb.asipto.com/freeswitch:index |
| 各种用例 | http://kb.asipto.com/kamailio:index |
| Kamailio Devel Guide | http://www.asipto.com/pub/kamailio-devel-guide/ |
