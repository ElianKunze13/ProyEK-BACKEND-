# Etapa 1: Construcción (Build stage)
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y descarga las dependencias (esto se cachea)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el resto del código fuente
COPY src ./src

# Construye el JAR (salta los tests para acelerar)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Runtime stage)
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto que usará la aplicación (Render/Railway asignarán el puerto real)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]