# Kamailio 官方 MySQL Schema

本目录用于存放 **Kamailio 官方** MySQL 建表脚本，与 kam-admin 无直接归属关系；仅作汇总与备份参考。

## 来源

- **官方仓库**：<https://github.com/kamailio/kamailio/tree/master/utils/kamctl/mysql>
- **安装路径**：安装 Kamailio 后通常在 `/usr/share/kamailio/mysql/`

## 获取方式

### 1. 从本机 Kamailio 安装复制（推荐）

```bash
# Linux 示例
cp -r /usr/share/kamailio/mysql/* sql/kamailio-mysql/
```

### 2. 从 GitHub 下载

运行项目根目录下的脚本（若已提供）：

```bash
./scripts/fetch-kamailio-schema-mysql.sh
```

或手动从以下地址下载单个文件（将 `master` 改为你需要的分支/标签）：

- 标准库：<https://raw.githubusercontent.com/kamailio/kamailio/master/utils/kamctl/mysql/standard-create.sql>
- 其他：<https://github.com/kamailio/kamailio/tree/master/utils/kamctl/mysql> 中每个 `*-create.sql` 的 raw 链接

## 与本 Dashboard 相关的常用文件

| 文件 | 对应模块 |
|------|----------|
| standard-create.sql | 基础表（推荐首选导入） |
| acc-create.sql | 计费 |
| dialog-create.sql | 对话 |
| dispatcher-create.sql | 调度器 |
| domain-create.sql | 域 |
| drouting-create.sql | 动态路由 |
| htable-create.sql | 哈希表 |
| lcr-create.sql | LCR |
| msilo-create.sql | 离线消息 |
| permissions-create.sql | 权限 |
| presence-create.sql | 存在 |
| siptrace-create.sql | SIP 跟踪 |
| topos-create.sql | 拓扑 |
| usrloc-create.sql | 用户位置 |
| 以及 carrierroute, dialplan, mtree, pdt, rtpengine, secfilter, uac 等 | 见根目录 sql/README.md |
