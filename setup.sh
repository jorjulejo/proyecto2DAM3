#!/bin/bash

# Parar en caso de error
set -e

# Definir el nombre del directorio del repositorio
REPO_DIR="docker-images"

# Clonar el repositorio de Oracle y construir la imagen
echo "Clonando el repositorio de Oracle y construyendo la imagen..."
git clone https://github.com/oracle/docker-images.git "$REPO_DIR"
cd "$REPO_DIR"/OracleDatabase/SingleInstance/dockerfiles
./buildContainerImage.sh -v 21.3.0 -x

# Volver al directorio principal
cd ../../../../

# Limpieza: eliminar el repositorio clonado
echo "Limpiando: eliminando el repositorio clonado..."
rm -rf "$REPO_DIR"

# Ejecutar Docker Compose
echo "Levantando servicios con Docker Compose..."
docker-compose up --build -d

# Instalar dependencias y ejecutar la aplicación Electron
cd electron-vite
echo "Instalando dependencias en electron-vite..."
npm install
cd reto-electron
echo "Instalando dependencias en reto-electron..."
npm install
echo "Iniciando la aplicación Electron..."
npm run dev

echo "Proceso completado."
