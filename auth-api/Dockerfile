# 1. Utiliza una imagen base de Maven con JDK 17 para la construcción
FROM maven:3.8.4-openjdk-17-slim as build

# 2. Establece el directorio de trabajo en el contenedor
WORKDIR /app

# 3. Copia el archivo pom.xml
COPY pom.xml .

# 4. Descarga todas las dependencias
RUN mvn dependency:go-offline -B

# 5. Copia el resto de tu proyecto
COPY src ./src

# 6. Construye la aplicación
RUN mvn package -DskipTests

# 7. Empieza con una nueva imagen para mantener el tamaño del contenedor pequeño
FROM openjdk:17-slim

# 8. Copia el JAR construido desde la imagen de build
COPY --from=build /app/target/*.jar app.jar

# 9. Define el comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
