# 从 Kamailio 官方 GitHub 拉取 PostgreSQL schema 到 sql/kamailio-postgres/
# PowerShell；可选环境变量 $env:KAMAILIO_RAW_URL 指定 base，默认 master

$baseUrl = if ($env:KAMAILIO_RAW_URL) { $env:KAMAILIO_RAW_URL } else { "https://raw.githubusercontent.com/kamailio/kamailio/master/utils/kamctl/postgres" }
$dest = if ($args[0]) { $args[0] } else { Join-Path $PSScriptRoot ".." "sql" "kamailio-postgres" }

$files = @(
  "standard-create.sql",
  "acc-create.sql", "dialog-create.sql", "dispatcher-create.sql", "domain-create.sql",
  "drouting-create.sql", "htable-create.sql", "lcr-create.sql", "msilo-create.sql",
  "permissions-create.sql", "presence-create.sql", "siptrace-create.sql", "topos-create.sql",
  "usrloc-create.sql", "carrierroute-create.sql", "dialplan-create.sql", "mtree-create.sql",
  "pdt-create.sql", "rtpengine-create.sql", "secfilter-create.sql", "uac-create.sql"
)

if (-not (Test-Path $dest)) { New-Item -ItemType Directory -Path $dest -Force | Out-Null }

foreach ($f in $files) {
  Write-Host "Fetching $f ..."
  Invoke-WebRequest -Uri "$baseUrl/$f" -OutFile (Join-Path $dest $f) -UseBasicParsing
}

Write-Host "Done. Schema files in $dest"
