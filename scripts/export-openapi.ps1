# 导出 OpenAPI 文档到 docs/openapi.json（需先启动 kam-admin-server，默认端口 3000）
$baseUrl = $env:API_BASE_URL ?? "http://localhost:3000"
$outDir  = Join-Path $PSScriptRoot ".." "docs"
$outFile = Join-Path $outDir "openapi.json"

if (-not (Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir -Force | Out-Null }

try {
    Invoke-WebRequest -Uri "$baseUrl/v3/api-docs" -OutFile $outFile -UseBasicParsing
    Write-Host "OpenAPI 文档已导出: $outFile"
} catch {
    Write-Error "导出失败，请确认 kam-admin-server 已启动（默认 $baseUrl）。错误: $_"
    exit 1
}
