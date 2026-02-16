# Kamailio 官方 PostgreSQL Schema

本目录用于存放 **Kamailio 官方** PostgreSQL 建表脚本，与 kam-admin 无直接归属关系；仅作汇总与备份参考。

## 来源

- **官方仓库**：<https://github.com/kamailio/kamailio/tree/master/utils/kamctl/postgres>
- **安装路径**：安装 Kamailio 后通常在 `/usr/share/kamailio/postgres/`

## 获取方式

### 1. 从本机 Kamailio 安装复制（推荐）

```bash
# Linux 示例
cp -r /usr/share/kamailio/postgres/* sql/kamailio-postgres/
```

### 2. 从 GitHub 下载（脚本拉取）

运行项目根目录下的脚本：

```bash
# Linux/macOS
./scripts/fetch-kamailio-schema-postgres.sh

# Windows PowerShell
./scripts/fetch-kamailio-schema-postgres.ps1
```

或参考 `kamailio-mysql/README.md` 中的方式，将路径改为 `utils/kamctl/postgres` 下的对应 `.sql` 文件。

## 说明

若本仓库未附带具体 `.sql` 文件，请以系统安装路径或官方仓库为准；本目录仅作索引与说明。
