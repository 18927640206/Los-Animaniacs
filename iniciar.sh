#!/bin/bash

echo "🚀 Iniciando la artillería de Los Animaniacs..."

# 1. Configurar Java 17 por si acaso
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# 2. Levantar toda la infraestructura (Backend + Base de Datos + RabbitMQ + Frontend)
docker-compose up -d --build

echo "✅ Sistema levantado."
echo "⚠️  RECUERDA: Ve a la pestaña PORTS y pon los puertos 8080 y 3000 en PUBLIC."