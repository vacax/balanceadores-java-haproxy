#!/usr/bin/env bash
echo "Instalando estructura basica para HAProxy en Ubuntu"

# Habilitando la memoria de intecambio.
sudo dd if=/dev/zero of=/swapfile count=2048 bs=1MiB
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo cp /etc/fstab /etc/fstab.bak
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Instando los software necesarios para probar el concepto.
sudo apt update && sudo apt install -y nmap zip unzip haproxy certbot

# Instalando la versión sdkman y java
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java

# Clonando el repositorio.
git clone https://github.com/vacax/balanceadores-java-haproxy

# Aplicando el permiso de ejecucion a los scripts
chmod +x ~/balanceadores-java-haproxy/configuracion-haproxy/ubuntu/simple.sh
chmod +x ~/balanceadores-java-haproxy/configuracion-haproxy/ubuntu/sesiones.sh
chmod +x ~/balanceadores-java-haproxy/configuracion-haproxy/ubuntu/sesiones-centralizadas.sh