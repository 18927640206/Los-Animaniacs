# Archivo: /workspaces/Los-Animaniacs/uamishop-ordenes/Dockerfile

# Etapa 1: Construcción (Builder)
# Usar la imagen de Maven con JDK 17 como base para la etapa de construcción, nombrada 'builder' para multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS builder
# Establecer el directorio de trabajo dentro del contenedor para las operaciones de construcción
WORKDIR /app
# Copiar el pom para descargar dependencias y usar caché
# Copiar el archivo pom.xml al directorio de trabajo para aprovechar la caché de Docker en descargas de dependencias
COPY pom.xml .
# Ejecutar Maven para descargar dependencias offline, usando modo batch para no interactivo
RUN mvn dependency:go-offline -B
# Copiar el código fuente y compilar el JAR
# Copiar el directorio src al contenedor
COPY src ./src
# Ejecutar Maven para empaquetar la aplicación en un JAR, saltando pruebas y en modo batch
RUN mvn package -DskipTests -B

# Etapa 2: Ejecución (Runtime)
# Usar la imagen ligera de JRE 17 para la etapa de ejecución, reduciendo el tamaño de la imagen final
FROM eclipse-temurin:17-jre
# Establecer el directorio de trabajo para la aplicación en ejecución
WORKDIR /app
# Seguridad: No ejecutar como root
# Crear un grupo y usuario no root para ejecutar la aplicación, mejorando la seguridad
RUN groupadd -g 1001 appgroup && useradd -u 1001 -g appgroup -m appuser
# Cambiar al usuario no root para ejecutar la aplicación
USER appuser
# Traer solo el JAR generado
# Copiar el JAR compilado desde la etapa 'builder' a la imagen final, renombrándolo a app.jar
COPY --from=builder /app/target/*.jar app.jar
# Exponer el puerto 8083 para que la aplicación sea accesible desde fuera del contenedor
EXPOSE 8083
# Definir el punto de entrada para ejecutar la aplicación Java
ENTRYPOINT ["java", "-jar", "app.jar"]