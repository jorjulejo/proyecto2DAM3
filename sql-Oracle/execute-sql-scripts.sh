#!/bin/bash


sleep 60

# Conectar a la base de datos y ejecutar cada script SQL, excepto crear-usuario.sql
for script in /opt/oracle/scripts/other-scripts/*.sql; do
  
    echo "Ejecutando $script..."
    sqlplus jorju/20022002@XEPDB1 @$script
  
done
