# ---- Stage 1: Build (Maven + JDK) ----
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Download dependencies first to take advantage of the cache
COPY pom.xml .
RUN mvn -q dependency:go-offline -B

# Copy source and package (fat JAR -> target/app.jar)
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- Stage 2: Runtime (JRE) ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# (Optional) create a non-root user
RUN addgroup --system --gid 1001 appgroup \
 && adduser --system --uid 1001 --gid 1001 --home /app --shell /bin/sh appuser

# Copy the final artifact
COPY --from=builder /app/target/app.jar /app/app.jar
RUN chown -R appuser:appgroup /app
USER appuser

# (Optional) Only expose if your app opens a port (e.g. 8080)
# EXPOSE 8080

# JVM in container; avoid using 'sh -c'
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m -XX:+UseContainerSupport"

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
