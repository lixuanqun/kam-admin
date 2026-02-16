#!/bin/bash
# 从 Kamailio 官方仓库拉取 MySQL schema 并导入（首次启动时执行）
set -e
BASE_URL="${KAMAILIO_RAW_URL:-https://raw.githubusercontent.com/kamailio/kamailio/master/utils/kamctl/mysql}"
cd /tmp

FILES="standard-create.sql acc-create.sql dialog-create.sql dispatcher-create.sql domain-create.sql \
  drouting-create.sql htable-create.sql lcr-create.sql permissions-create.sql usrloc-create.sql \
  carrierroute-create.sql dialplan-create.sql mtree-create.sql pdt-create.sql presence-create.sql \
  siptrace-create.sql topos-create.sql msilo-create.sql uac-create.sql rtpengine-create.sql secfilter-create.sql"

echo "Fetching Kamailio MySQL schema..."
for f in $FILES; do
  if curl -sSL -o "$f" "$BASE_URL/$f" 2>/dev/null && [ -s "$f" ]; then
    mysql -u root -p"$MYSQL_ROOT_PASSWORD" kamailio < "$f" && echo "  loaded $f"
  fi
done
echo "Kamailio schema initialized."
