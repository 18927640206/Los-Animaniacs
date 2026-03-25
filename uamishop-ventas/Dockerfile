# Archivo: /workspaces/Los-Animaniacs/uamishop-ventas/Dockerfile

# Etapa 1: Construcción (Builder)
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Seguridad: No ejecutar como root
RUN groupadd -g 1001 appgroup && useradd -u 1001 -g appgroup -m appuser
USER appuser
# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/target/*.jar app.jar
# Exponer el puerto 8081 (según la práctica para el catálogo)
EXPOSE 8082 
#Correccion linea 20, tenia que ser 8082 sino causa un conflicto de puertos
ENTRYPOINT ["java", "-jar", "app.jar"]