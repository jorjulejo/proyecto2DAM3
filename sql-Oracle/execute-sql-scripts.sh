#!/bin/bash

# Esperar a que la base de datos est� completamente lista (ajustar seg�n sea necesario)
sleep 60

# Conectar a la base de datos y ejecutar cada script SQL, excepto crear-usuario.sql
for script in /opt/oracle/scripts/other-scripts/*.sql; do
  if [ "$(basename $script)" != "01-create-usuario.sql" ]; then
    echo "Ejecutando $script..."
    sqlplus jorju/20022002@XEPDB1 @$script
  fi
done
