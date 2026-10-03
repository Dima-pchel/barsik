#!/usr/bin/env bash
# Ручной аналог deploy/cloud-init.yaml для Ubuntu 24.04. Запускать под root:
#   bash setup-server.sh
# Скрипт можно запускать повторно.
set -euo pipefail

SSH_KEY='ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIBfDX2inhp7aXWg/6/qJUpaTMAzwQEHXVngWXE+ob5fU pchelHome'
USER_NAME=deploy

[ "$(id -u)" -eq 0 ] || { echo "Запусти под root"; exit 1; }

export DEBIAN_FRONTEND=noninteractive

hostnamectl set-hostname barsik
grep -q '127.0.1.1 barsik' /etc/hosts || echo '127.0.1.1 barsik' >> /etc/hosts
timedatectl set-timezone UTC

apt-get update
apt-get -y upgrade
apt-get install -y ca-certificates curl gnupg git ufw fail2ban unattended-upgrades htop unzip

# --- пользователь deploy ---
if ! id "$USER_NAME" >/dev/null 2>&1; then
  adduser --disabled-password --gecos "Barsik deploy user" "$USER_NAME"
fi
usermod -aG sudo "$USER_NAME"
echo "$USER_NAME ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/90-$USER_NAME
chmod 0440 /etc/sudoers.d/90-$USER_NAME
install -d -m 700 -o "$USER_NAME" -g "$USER_NAME" /home/$USER_NAME/.ssh
grep -qF "$SSH_KEY" /home/$USER_NAME/.ssh/authorized_keys 2>/dev/null \
  || echo "$SSH_KEY" >> /home/$USER_NAME/.ssh/authorized_keys
chown "$USER_NAME:$USER_NAME" /home/$USER_NAME/.ssh/authorized_keys
chmod 600 /home/$USER_NAME/.ssh/authorized_keys

# --- swap 2 ГБ ---
if ! swapon --show | grep -q /swapfile; then
  [ -f /swapfile ] || { fallocate -l 2G /swapfile; chmod 600 /swapfile; mkswap /swapfile; }
  swapon /swapfile
  grep -q '^/swapfile' /etc/fstab || echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi
echo 'vm.swappiness=10' > /etc/sysctl.d/99-swappiness.conf
sysctl --system >/dev/null

# --- Docker ---
install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
chmod a+r /etc/apt/keyrings/docker.asc
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
  > /etc/apt/sources.list.d/docker.list
apt-get update
apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
cat > /etc/docker/daemon.json <<'EOF'
{
  "log-driver": "json-file",
  "log-opts": { "max-size": "10m", "max-file": "3" }
}
EOF
usermod -aG docker "$USER_NAME"
systemctl enable docker
systemctl restart docker

# --- автообновления безопасности ---
cat > /etc/apt/apt.conf.d/20auto-upgrades <<'EOF'
APT::Periodic::Update-Package-Lists "1";
APT::Periodic::Unattended-Upgrade "1";
EOF

# --- fail2ban ---
cat > /etc/fail2ban/jail.d/sshd.local <<'EOF'
[sshd]
enabled = true
backend = systemd
maxretry = 5
findtime = 10m
bantime = 1h
EOF
systemctl enable fail2ban
systemctl restart fail2ban

# --- каталог проекта ---
mkdir -p /opt/barsik
chown "$USER_NAME:$USER_NAME" /opt/barsik

# --- файрвол ---
ufw default deny incoming
ufw default allow outgoing
ufw allow 22/tcp
ufw --force enable

# --- SSH-харденинг: самым последним, после проверки, что ключ на месте ---
if [ -s /home/$USER_NAME/.ssh/authorized_keys ]; then
  cat > /etc/ssh/sshd_config.d/00-hardening.conf <<'EOF'
PermitRootLogin no
PasswordAuthentication no
KbdInteractiveAuthentication no
PubkeyAuthentication yes
MaxAuthTries 3
AllowUsers deploy
EOF
  sshd -t && systemctl restart ssh
fi

echo
echo "Готово. НЕ закрывай эту сессию, пока в новом окне не проверишь: ssh deploy@<IP>"
