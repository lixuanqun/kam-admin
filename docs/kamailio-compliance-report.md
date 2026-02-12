# Kamailio 配置与指令符合度核对报告

> 对比当前项目与 Kamailio 官方文档中的全部配置、模块、RPC 指令的覆盖情况

---

## 一、整体结论

| 维度 | 符合度 | 说明 |
|------|--------|------|
| **RPC 模块覆盖** | ⚠️ 部分满足 | 已覆盖约 20+ 个常用模块的 RPC，未覆盖 200+ 模块中的多数 |
| **配置化管理** | ❌ 不满足 | 无可视化 kamailio.cfg 编辑、全局参数、路由脚本配置 |
| **数据库表** | ✅ 基本满足 | 覆盖 subscriber、domain、dispatcher、drouting、acc、siptrace 等核心表 |
| **JSONRPC 前置** | ✅ 满足 | 需 Kamailio 启用 jsonrpcs + xhttp，项目正确调用 |

**结论**：当前项目满足 **常用业务场景下的 Kamailio 管理**，但 **不满足**“全部配置和指令”的完整覆盖。缺失的主要是：配置文件编辑、预处理器、路由脚本、以及大量未接入 RPC 的模块。

---

## 二、已覆盖的 Kamailio 能力

### 2.1 后端模块与 RPC 对应

| 项目模块 | Kamailio 模块 | 使用 RPC | 数据来源 |
|----------|---------------|----------|----------|
| system | core, cfg_rpc, tls, pike, statistics | ✅ | core.*, cfg.*, tls.*, pike.*, stats.* |
| subscriber | usrloc (DB) | - | MySQL subscriber 表 |
| domain | domain | ✅ | DB + domain.reload, domain.dump |
| dispatcher | dispatcher | ✅ | DB + dispatcher.reload, dispatcher.list, set_state |
| drouting | drouting | ✅ | DB + drouting.reload, gw_status, carrier_status |
| carrierroute | carrierroute | ✅ | cr.reload_routes, cr.dump_routes |
| lcr | lcr | ✅ | DB + lcr.reload, dump_gws, dump_rules |
| dialplan | dialplan | ✅ | DB + dialplan.reload, translate, dump |
| pdt | pdt | ✅ | DB + pdt.reload, pdt.list |
| mtree | mtree | ✅ | DB + mtree.reload, match, summary |
| location | usrloc | ✅ | DB + ul.dump, ul.lookup, ul.rm, ul.rm_contact |
| dialog | dialog | ✅ | dlg.list, dlg.stats_active, end_dlg, dlg_list, bridge_dlg |
| permissions | permissions | ✅ | DB + permissions.addressReload, trustedReload, addressDump |
| secfilter | secfilter | ✅ | secfilter.reload, print, stats, add_bl, add_wl |
| presence | presence | ✅ | presence.cleanup, refreshWatchers |
| msilo | msilo | ✅ | DB + msilo.dump |
| uac | uac | ✅ | DB + uac.reg_reload, reg_info, reg_refresh, reg_dump |
| htable | htable | ✅ | htable.get, sets, seti, delete, dump, reload, listTables, stats |
| rtpengine | rtpengine, rtpproxy, nathelper | ✅ | rtpengine.show, reload, enable, ping 等 |
| acc | acc | - | DB acc 表 |
| siptrace | siptrace | - | DB sip_trace 表 |
| topos | topos | - | DB topos_d 表 |
| userdata | 多表 | - | DB usr_preferences, grp, speed_dial 等 |
| usrpreferences | usr_preferences | - | DB |
| version | version 表 | - | DB |
| monitoring | 聚合 | ✅ | 聚合 core、stats、dlg 等 |

### 2.2 RPC 方法完整性核对

| RPC 方法 | 项目使用 | 官方支持 | 备注 |
|----------|----------|----------|------|
| ul.dump | ✅ | ✅ | |
| ul.lookup | ✅ | ✅ | |
| ul.rm | ✅ | ✅ | KamailioRpcService.deleteUserLocation |
| ul.rm_contact | ✅ | ✅ | |
| dispatcher.reload | ✅ | ✅ | |
| dispatcher.list | ✅ | ✅ | |
| dispatcher.set_state | ✅ | ✅ | |
| drouting.reload | ✅ | ✅ | |
| drouting.gw_status | ✅ | ✅ | |
| drouting.carrier_status | ✅ | ✅ | |
| domain.reload | ✅ | ✅ | |
| domain.dump | ✅ | ✅ | |
| dlg.list | ✅ | ✅ | |
| dlg.stats_active | ✅ | ✅ | |
| dlg.end_dlg | ✅ | ✅ | |
| dlg.dlg_list | ✅ | ✅ | |
| dlg.bridge_dlg | ✅ | ✅ | |
| permissions.* | ✅ | ✅ | |
| htable.* | ✅ | ✅ | |
| pdt.reload, pdt.list | ✅ | ✅ | |
| mtree.reload, match, summary | ✅ | ✅ | |
| secfilter.* | ✅ | ✅ | |
| presence.cleanup, refreshWatchers | ✅ | ✅ | |
| msilo.dump | ✅ | ✅ | |
| uac.reg_* | ✅ | ✅ | |
| rtpengine.* | ✅ | ✅ | |
| cr.reload_routes, cr.dump_routes | ✅ | ✅ | |
| dialplan.reload, translate, dump | ✅ | ✅ | |
| lcr.reload, dump_gws, dump_rules | ✅ | ✅ | |
| core.* | ✅ | ✅ | |
| cfg.* | ✅ | ✅ | 来自 cfg_rpc 模块 |
| tls.* | ✅ | ✅ | |
| pike.* | ✅ | ✅ | |
| stats.* | ✅ | ✅ | 来自 statistics 模块 |

---

## 三、未覆盖的 Kamailio 能力

### 3.1 配置文件与路由（无 UI）

| 能力 | 说明 | 项目状态 |
|------|------|----------|
| kamailio.cfg 编辑 | 全局参数、listen、debug 等 | ❌ 无 |
| loadmodule / modparam | 模块加载与参数 | ❌ 无 |
| 路由块 | request_route、onreply_route、failure_route 等 | ❌ 无 |
| 预处理器 | #!define、#!include、#!ifdef 等 | ❌ 无 |
| 子路由 | 自定义路由块 | ❌ 无 |

### 3.2 未接入的常用 RPC 模块

| Kamailio 模块 | 常用 RPC | 项目状态 |
|---------------|----------|----------|
| usrloc | ul.flush, ul.add, ul.db_users, ul.db_contacts | 部分（缺 flush, add） |
| sipcapture | sipcapture.* | ❌ 未用 |
| registrar | 无 RPC | - |
| tm | tm.* | ❌ 未用 |
| rr | 无 RPC | - |
| acc | 无 RPC（仅 DB） | ✅ 已用 DB |
| auth_db | 无 RPC | - |
| alias_db | 无 RPC | - |
| tsilo | tsilo.* | ❌ 未用 |
| imc | imc.* | ❌ 未用 |
| pua | pua.* | ❌ 未用 |
| presence_* | 多子模块 | 仅 presence 主模块 |
| snmpstats | snmpstats.* | ❌ 未用 |
| ctl | 通过 FIFO 控制 | ❌ 未用 |
| evapi | evapi.* | ❌ 未用 |
| kafka | kafka.* | ❌ 未用 |
| mqueue | mqueue.* | ❌ 未用 |

### 3.3 未覆盖的数据库表/实体

| 表/实体 | 说明 | 项目状态 |
|---------|------|----------|
| aliases | 别名 | ❌ 无 |
| grp | 用户组 | ✅ userdata 含 grp |
| dbaliases | DB 别名 | ✅ userdata |
| acc（扩展字段） | 计费扩展 | 基础支持 |
| missed_calls | 未接来电 | ✅ 有 |
| sip_trace | SIP 跟踪 | ✅ 有 |
| dialog 表 | 对话持久化 | ❌ 仅用内存 dlg |
| sl (stateless) | 无 RPC | - |

### 3.4 配置项未覆盖

| 配置类 | 示例 | 项目状态 |
|--------|------|----------|
| 全局参数 | debug, log_stderror, listen | ❌ |
| 模块参数 | modparam 各模块 | ❌ |
| 路由逻辑 | request_route 内逻辑 | ❌ |
| 变量与转换 | 伪变量、transformations | ❌ |

---

## 四、潜在 RPC 兼容性说明

### 4.1 cfg 模块

- 项目使用：`cfg.get`, `cfg.list`, `cfg.set_now_int`, `cfg.set_now_string`, `cfg.diff`
- 官方模块：`cfg_rpc` 提供 `cfg.list` 等，RPC 前缀为 `cfg`
- 需确保 Kamailio 已加载 `cfg_rpc` 模块

### 4.2 statistics 模块

- 项目使用：`stats.get_statistics`, `stats.reset_statistics`, `stats.clear_statistics`
- 官方模块：`statistics` 提供这些 RPC
- 需确保已加载 `statistics` 模块

### 4.3 版本差异

- 项目 RPC 按 5.x/6.0.x 设计，部分方法在不同版本可能略有差异
- 建议在部署文档中注明支持的 Kamailio 版本（如 5.x、6.0.x）

---

## 五、缺失能力汇总（按优先级）

### 高优先级（建议补充）

| 项 | 说明 |
|----|------|
| kamailio.cfg 查看 | 至少支持只读查看当前配置 |
| 部署文档完善 | 明确需加载的模块清单（jsonrpcs, xhttp, cfg_rpc, statistics 等） |
| ul.flush | 需要时从内存刷到 DB |

### 中优先级（可选）

| 项 | 说明 |
|----|------|
| sipcapture RPC | 若使用 sipcapture 替代 siptrace |
| tsilo | 事务状态存储 |
| 更多 usrloc RPC | ul.add, ul.db_users, ul.db_contacts |

### 低优先级（按需）

| 项 | 说明 |
|----|------|
| 配置编辑器 | 可视化编辑 kamailio.cfg |
| 路由脚本编辑 | 编辑 request_route 等 |
| 更多模块 | kafka、evapi、imc 等 |

---

## 六、满足“全部配置和指令”的差距

要满足 Kamailio 的 **全部** 配置和指令，需要补充：

1. **配置文件管理**：kamailio.cfg 的查看、编辑、备份、重载
2. **预处理器**：#!define、#!include、#!ifdef 等
3. **路由块**：request_route、onreply_route、failure_route 等脚本编辑
4. **全部模块**：200+ 模块的加载与 modparam 配置
5. **全部 RPC**：各模块对外暴露的 RPC 命令
6. **伪变量与转换**：$ru、$fu 等及 transformations 的说明/辅助

当前项目定位为 **业务管理 Dashboard**，侧重用户、路由、权限、监控、计费等，**不追求** 对 kamailio.cfg 和全部模块的完整覆盖。若要向“全配置、全指令”方向发展，需按上述维度逐步扩展。

---

## 七、建议

1. **文档**：在 `docs/deployment-guide.md` 中列出 Kamailio 必须加载的模块及示例配置
2. **配置检查**：增加“连接测试”或“模块检查”接口，验证 jsonrpcs、cfg_rpc、statistics 等是否可用
3. **版本约束**：在 README 或部署文档中写明支持的 Kamailio 版本（如 5.5+、6.0.x）
4. **RPC 完善**：视需求补充 ul.flush、ul.add 等 usrloc RPC 的接口
