#!/usr/bin/env bash
# 从 Kamailio 官方 GitHub 拉取 PostgreSQL schema 到 sql/kamailio-postgres/
# 需要 curl 或 wget；可选：指定分支/标签，默认 master

set -e
BASE_URL="${KAMAILIO_RAW_URL:-https://raw.githubusercontent.com/kamailio/kamailio/master/utils/kamctl/postgres}"
DEST="${1:-$(dirname "$0")/../sql/kamailio-postgres}"
mkdir -p "$DEST"

FILES=(
  standard-create.sql
  acc-create.sql
  dialog-create.sql
  dispatcher-create.sql
  domain-create.sql
  drouting-create.sql
  htable-create.sql
  lcr-create.sql
  msilo-create.sql
  permissions-create.sql
  presence-create.sql
  siptrace-create.sql
  topos-create.sql
  usrloc-create.sql
  carrierroute-create.sql
  dialplan-create.sql
  mtree-create.sql
  pdt-create.sql
  rtpengine-create.sql
  secfilter-create.sql
  uac-create.sql
)

for f in "${FILES[@]}"; do
  echo "Fetching $f ..."
  if command -v curl >/dev/null 2>&1; then
    curl -sSL -o "$DEST/$f" "$BASE_URL/$f"
  else
    wget -q -O "$DEST/$f" "$BASE_URL/$f"
  fi
done

echo "Done. Schema files in $DEST"
