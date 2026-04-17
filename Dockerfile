# Etapa 1: Build
FROM maven:3.9.14-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar pom.xml
COPY pom.xml .

# Descargar dependencias
RUN mvn -q -DskipTests dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar
RUN mvn -q -DskipTests clean package

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copiar JAR desde la etapa de build
COPY --from=builder /build/target/franchise-api-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=mysql
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/franchise_db
ENV SPRING_DATASOURCE_USERNAME=franchise_user
ENV SPRING_DATASOURCE_PASSWORD=franchise_password
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
