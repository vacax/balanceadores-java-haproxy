#!/usr/bin/env bash
echo "Configuracion sesiones centralizadas de HAProxy"

#Elimiando todas las aplicaciones Java.
killall java

# Creando una copia del archivo actual.
if [ ! -e "/etc/haproxy/haproxy.cfg.original" ]; then
  echo "Creando archivo de backup"
  sudo cp /etc/haproxy/haproxy.cfg /etc/haproxy/haproxy.cfg.original
fi

# Copiando el archivo simple.
sudo cp ~/balanceadores-java-haproxy/configuracion-haproxy/ubuntu/haproxy.cfg.sesiones-centralizadas /etc/haproxy/haproxy.cfg

# Reinicando el servicio de HAProxy
sudo service haproxy stop && sudo service haproxy start

# Compilando la aplicación simple.
cd /home/ubuntu/balanceadores-java-haproxy/balanceo-spring-boot

# Ejecutando shadowjar
./gradlew clean --no-daemon
./gradlew bootJar --no-daemon

# Cambiando el nombre al archivo generado
mv build/libs/*.jar build/libs/app-spring.jar

# subiendo las instancias de las aplicaciones
java -Dserver.port=7000 -jar build/libs/app-spring.jar &
java -Dserver.port=7001 -jar build/libs/app-spring.jar &
java -Dserver.port=7002 -jar build/libs/app-spring.jar &

echo "Completando ambiente Sesiones Centralizadas"