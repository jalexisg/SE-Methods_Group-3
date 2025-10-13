# ---- Stage 1: Build (Maven + JDK) ----
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Descarga dependencias primero para aprovechar cache
COPY pom.xml .
RUN mvn -q dependency:go-offline -B

# Copia el código y empaqueta (fat JAR -> target/app.jar)
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- Stage 2: Runtime (JRE) ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# (Opcional) crea usuario no root
RUN addgroup --system --gid 1001 appgroup \
 && adduser --system --uid 1001 --gid 1001 --home /app --shell /bin/sh appuser

# Copia el artefacto final
COPY --from=builder /app/target/app.jar /app/app.jar
RUN chown -R appuser:appgroup /app
USER appuser

# (Opcional) Solo expón si tu app abre un puerto (por ejemplo 8080)
# EXPOSE 8080

# JVM en contenedor sin usar 'sh -c'
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m -XX:+UseContainerSupport"

# Ejecuta la app
ENTRYPOINT ["java","-jar","/app/app.jar"]
