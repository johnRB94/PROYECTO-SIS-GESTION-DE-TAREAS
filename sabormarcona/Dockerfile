# Etapa 1: Construcción con Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar archivos de proyecto
COPY pom.xml .
COPY src ./src
COPY mvnw ./mvnw
COPY mvnw.cmd ./mvnw.cmd
COPY .mvn ./.mvn

# Compilar y empacar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jdk-alpine

# Establecer variables
ENV APP_HOME=/app \
    PORT=8080 \
    JAVA_OPTS="-Xmx512m -Xms256m"

WORKDIR $APP_HOME

# Copiar JAR desde builder
COPY --from=builder /build/target/*.jar app.jar

# Exponer puerto
EXPOSE $PORT

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:${PORT}/sabormarcona/actuator/health || exit 1

# Entrypoint
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
