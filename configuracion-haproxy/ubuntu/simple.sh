#!/usr/bin/env bash
echo "Configuracion basica de HAProxy"

#Elimiando todas las aplicaciones Java.
killall java

# Creando una copia del archivo actual.
if [ ! -e "/etc/haproxy/haproxy.cfg.original" ]; then
  echo "Creando archivo de backup"
  sudo cp /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.original
fi

# Copiando el archivo simple.
sudo cp ~/balanceadores-java-haproxy/configuracion-haproxy/ubuntu/haproxy.cfg.simple /etc/haproxy/haproxy.cfg

# Reinicando el servicio de HAProxy
sudo service haproxy stop && sudo service haproxy start

# Compilando la aplicación simple.
cd /home/ubuntu/balanceadores-java-haproxy/app-web-simple

# Ejecutando shadowjar
./gradlew clean --no-daemon
./gradlew shadowjar --no-daemon

# Cambiando el nombre al archivo generado
mv build/libs/*.jar build/libs/app-simple.jar

# subiendo las instancias de las aplicaciones
java -jar build/libs/app-simple.jar 7000 &
java -jar build/libs/app-simple.jar 7001 &
java -jar build/libs/app-simple.jar 7002 &

echo "Completando ambiente simple"