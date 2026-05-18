# ============================================================
# ETAPA 1 — BUILD
# Imagen completa del JDK para compilar el proyecto con Maven
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero solo los archivos de dependencias.
# Docker cachea esta capa: si pom.xml no cambia, no vuelve a descargar libs.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descargamos dependencias en modo offline (sin compilar el código todavía)
RUN ./mvnw dependency:go-offline -B

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos y empaquetamos saltando los tests (son lentos en CI/CD)
RUN ./mvnw package -DskipTests -B

# ============================================================
# ETAPA 2 — RUNTIME
# Imagen ligera solo con JRE (no necesitamos el compilador)
# La imagen final será ~200MB en vez de ~600MB con JDK completo
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Usuario no-root por seguridad (buena práctica en producción)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Directorio de trabajo en el contenedor final
WORKDIR /app

# Copiamos SOLO el .jar generado en la etapa de build
# Nada de código fuente, ni Maven, ni JDK llega a la imagen final
COPY --from=build /app/target/cursosvulcano-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone la aplicación Spring Boot
EXPOSE 8080

# Comando de arranque con opciones de memoria optimizadas para contenedores
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-jar", "app.jar"]
