# Usar una imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado por Maven
COPY target/uamishop-catalogo-*.jar app.jar

# Exponer el puerto que usa el microservicio (8081 en application.yml)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]