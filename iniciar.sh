#!/bin/bash

echo "🚀 Iniciando la artillería de Los Animaniacs..."

echo "☕ Verificando e instalando Java 17..."
sudo apt-get update -y
sudo apt-get install openjdk-17-jdk -y

# 1. Configurar Java 17 como variable de entorno
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

clear

# 2. Levantar toda la infraestructura (Backend + Base de Datos + RabbitMQ + Frontend)
echo "🐳 Construyendo y levantando contenedores..."
docker-compose up -d --build

echo "✅ Sistema levantado."
echo "⚠️  RECUERDA: Ve a la pestaña PORTS y pon los puertos 8080 y 3000 en PUBLIC."