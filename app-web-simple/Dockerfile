# Probando el concepto de Multi-stage.
# Instalando Gradle para compilar al aplicación y luego lo necesario a una imagen completa.
FROM gradle:8.5.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowjar --no-daemon

# Utilizando una imagen con lo necesario para arrancar la aplicación.
# en Java las imagenes slim son las mas pequeñas en ese tipo.
FROM eclipse-temurin:17-jre-alpine
# indicando el puerto para exponer, debo pasar el flag -p para habilitarlo o -P para publicarlos todos.
EXPOSE 8080
# creando la carpeta para el proyecto
RUN mkdir /app
# desde la otra instancia estaremos copiando lo necesario
COPY --from=build /home/gradle/src/build/libs/*.jar /app/mi-web-simple.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/mi-web-simple.jar"]