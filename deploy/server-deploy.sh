#!/usr/bin/env bash
# Выкатка нового образа бота на сервере. Лежит в /opt/barsik/deploy.sh.
#
# Вызывается:
#  - из GitHub Actions по SSH: ключ в authorized_keys привязан к этому скрипту (command=...),
#    тег образа приходит в SSH_ORIGINAL_COMMAND и проверяется по маске;
#  - вручную, в том числе для отката:  /opt/barsik/deploy.sh 1.0.1.0
#
# Если после выкатки бот не стартовал, скрипт возвращает предыдущий тег.
set -euo pipefail

TAG="${1:-${SSH_ORIGINAL_COMMAND:-}}"
[[ "$TAG" =~ ^[A-Za-z0-9._-]{1,64}$ ]] || { echo "Некорректный тег образа: '$TAG'" >&2; exit 2; }

cd /opt/barsik

# Два деплоя одновременно не запускаем
exec 9>/tmp/barsik-deploy.lock
flock -n 9 || { echo "Деплой уже выполняется" >&2; exit 3; }

OLD_TAG="$(grep '^BARSIK_TAG=' .env | cut -d= -f2)"

set_tag() { sed -i "s/^BARSIK_TAG=.*/BARSIK_TAG=$1/" .env; }

start() {
  docker compose pull barsik
  docker compose up -d barsik
}

# Ждём до 150 секунд строку об успешном старте приложения
wait_started() {
  for _ in $(seq 1 30); do
    if docker logs barsik 2>&1 | grep -q "Started BarsikApplication"; then
      return 0
    fi
    sleep 5
  done
  return 1
}

echo "Деплой: ${OLD_TAG} -> ${TAG}"
set_tag "$TAG"

if start && wait_started; then
  echo "Бот запущен, образ ${TAG}"
  docker image prune -af --filter "until=24h" > /dev/null
  docker compose ps
else
  echo "Бот не стартовал, откат на ${OLD_TAG}. Последние логи:" >&2
  docker logs --tail 30 barsik >&2 || true
  set_tag "$OLD_TAG"
  start || true
  exit 1
fi
