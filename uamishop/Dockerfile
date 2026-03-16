# Etapa 1: Construcción (Builder)
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
# Copiar el pom para descargar dependencias y usar caché
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copiar el código fuente y compilar el JAR
COPY src ./src
RUN mvn package -DskipTests -B

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Seguridad: No ejecutar como root
RUN groupadd -g 1001 appgroup && useradd -u 1001 -g appgroup -m appuser
USER appuser
# Traer solo el JAR generado
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]