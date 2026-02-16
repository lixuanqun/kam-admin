# SQL 脚本汇总

本目录集中存放 **Kamailio** 与 **kam-admin（本模块）** 所需的 SQL 脚本，便于初始化数据库与运维。

---

## 目录结构

```
sql/
├── README.md                    # 本说明
├── 01-database-user.sql         # 本模块：数据库用户与权限（MySQL 示例）
├── 02-kam-admin-mysql.sql       # 本模块：索引优化 + 租户表（MySQL）
├── 02-kam-admin-postgres.sql   # 本模块：索引优化 + 租户表（PostgreSQL）
├── kamailio-mysql/              # Kamailio 官方 MySQL schema（见下）
└── kamailio-postgres/           # Kamailio 官方 PostgreSQL schema（见下）
```

---

## 一、本模块（kam-admin）所需 SQL

| 文件 | 说明 | 执行顺序 |
|------|------|----------|
| `01-database-user.sql` | 创建数据库用户、授权（需根据环境改用户名/密码） | 在 Kamailio 库建好后执行 |
| `02-kam-admin-mysql.sql` | 索引优化 + 租户表（MySQL，合并后） | 库与表已存在后执行 |
| `02-kam-admin-postgres.sql` | 索引优化 + 租户表（PostgreSQL） | 同上 |

**注意**：本 Dashboard 依赖 Kamailio 已有数据库与表结构，请先完成 Kamailio 库初始化（见下一节），再执行本目录下的 `01-*`、`02-*`。

---

## 二、Kamailio 官方 Schema 来源

Kamailio 的表结构由 Kamailio 官方提供，有两种获取方式。

### 方式一：系统安装路径（推荐）

安装 Kamailio 后，Schema 通常在：

- **Linux (MySQL)**：`/usr/share/kamailio/mysql/`
- **Linux (PostgreSQL)**：`/usr/share/kamailio/postgres/`

使用 kamdbctl 一键建库：

```bash
kamdbctl create
```

或手动导入 MySQL 标准库：

```bash
mysql -u root -p kamailio < /usr/share/kamailio/mysql/standard-create.sql
```

### 方式二：从 Kamailio 官方仓库获取（脚本拉取）

在无 Kamailio 安装的环境下，可运行项目根目录下的脚本拉取 schema 到本地：

```bash
# MySQL（Linux/macOS）
./scripts/fetch-kamailio-schema-mysql.sh

# MySQL（Windows PowerShell）
./scripts/fetch-kamailio-schema-mysql.ps1

# PostgreSQL（Linux/macOS）
./scripts/fetch-kamailio-schema-postgres.sh

# PostgreSQL（Windows PowerShell）
./scripts/fetch-kamailio-schema-postgres.ps1
```

拉取后文件位于 **`sql/kamailio-mysql/`**、**`sql/kamailio-postgres/`**。也可手动从以下地址下载：

- **MySQL**：<https://github.com/kamailio/kamailio/tree/master/utils/kamctl/mysql>  
- **PostgreSQL**：<https://github.com/kamailio/kamailio/tree/master/utils/kamctl/postgres>

---

## 三、与本 Dashboard 功能对应的 Kamailio 表/模块

| Dashboard 功能 | Kamailio 模块 | 对应 schema 文件（mysql） |
|----------------|---------------|---------------------------|
| 用户/域/权限/位置 | usrloc, domain, permissions | standard-create.sql, domain-create.sql, permissions-create.sql, usrloc-create.sql 等 |
| 路由/调度 | dispatcher, drouting | dispatcher-create.sql, drouting-create.sql |
| 对话/htable | dialog, htable | dialog-create.sql, htable-create.sql |
| 监控/计费 | acc | acc-create.sql |
| 跟踪/拓扑 | siptrace, topos | siptrace-create.sql, topos-create.sql |
| 其他 | presence, msilo, uac, secfilter, rtpengine, carrierroute, dialplan, lcr, pdt, mtree | 同名 *-create.sql |

**标准安装** 一般只需先导入 **standard-create.sql**，再按需导入各模块的 `*-create.sql`。

---

## 四、执行顺序建议

1. 创建数据库：`CREATE DATABASE kamailio;`
2. 导入 Kamailio schema：`standard-create.sql` 及所需模块的 `*-create.sql`
3. 执行本模块：`01-database-user.sql`、`02-kam-admin-mysql.sql`（或 `02-kam-admin-postgres.sql`）
