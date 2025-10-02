# Multi-stage build for Population Information System
# Stage 1: Build stage using Maven with Eclipse Temurin JDK 17
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application and create executable JAR
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage using Eclipse Temurin JRE 17
FROM eclipse-temurin:17-jre AS runtime

# Create a non-root user for security
RUN groupadd -r appgroup && useradd -r -g appgroup -d /app -s /bin/bash appuser

# Set working directory
WORKDIR /app

# Copy the executable JAR from builder stage
COPY --from=builder /app/target/population-info-system-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Change ownership to the non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port (if needed for future web interface)
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD java -cp app.jar com.napier.sem.Main --health-check || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]