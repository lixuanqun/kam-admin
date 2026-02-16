# 导出 Swagger/OpenAPI 文档

## 在线文档与调试

- **Swagger UI**：启动服务后访问 **http://localhost:3000/api/docs**，可在页面上查看并调试所有接口。
- **OpenAPI JSON**：**http://localhost:3000/v3/api-docs** 返回 OpenAPI 3.0 规范（JSON）。

## 导出为文件

服务**已启动**的前提下，可将文档导出为本地文件，便于归档或给前端/第三方使用。

### Windows (PowerShell)

```powershell
# 导出 JSON（默认端口 3000）
Invoke-WebRequest -Uri "http://localhost:3000/v3/api-docs" -OutFile "docs/openapi.json" -UseBasicParsing

# 导出 YAML（若已启用）
Invoke-WebRequest -Uri "http://localhost:3000/v3/api-docs.yaml" -OutFile "docs/openapi.yaml" -UseBasicParsing
```

### 使用项目脚本（推荐）

在项目根目录执行（需先启动 kam-admin-server）：

```powershell
.\scripts\export-openapi.ps1
```

导出文件位于 `docs/openapi.json`。

### curl（任意系统）

```bash
curl -s http://localhost:3000/v3/api-docs -o docs/openapi.json
```

## 运行接口测试

```bash
cd kam-admin-server
mvn test
```

测试包含各模块 Controller 的 MockMvc 单测，以及 `OpenApiDocTest`（校验 `/v3/api-docs` 与 `/api/docs` 可访问）。
