# USE CASE: 2 Docker Containerization

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *Developer* I want *the application to run in Docker containers* so that *deployment is consistent across environments.*

### Scope

Application build and runtime environments (Docker / Docker Compose).

### Level

Primary task / deployment.

### Preconditions

- Multi-stage Dockerfile exists and can build the application artifact (uber-jar).
- The application build (Maven) succeeds locally and in CI.
- MySQL connector (JDBC driver) is included in the build artifact.
- Network configuration and environment variables support database connectivity between containers.

### Success End Condition

The application runs successfully inside a container built from the repository's Dockerfile and can connect to the database when orchestrated via Docker Compose.

### Failed End Condition

Container fails to build or run (missing dependencies, misconfigured environment variables, network issues) and errors are logged during build or startup.

### Primary Actor

Developer / DevOps engineer.

### Trigger

A developer or CI pipeline initiates a container build (local docker build or CI job), or a developer runs `docker-compose up`.

## MAIN SUCCESS SCENARIO

1. Developer or CI builds the image using the repository's Dockerfile (multi-stage build).
2. Build stage compiles the project with Maven and produces an executable jar (with dependencies).
3. Runtime stage creates a lightweight container image and copies the built jar.
4. Developer runs `docker run` or `docker-compose up` to start the application and the database.
5. Application connects to the database using configured environment variables or Docker Compose network.
6. Health-checks and logs indicate successful startup and database connectivity.

## EXTENSIONS

2a. **Build fails due to missing dependencies**:
   1. CI or developer inspects build logs, fixes pom dependencies or build process, and retries the build.

3a. **Container cannot connect to DB**:
   1. Verify environment variables, Docker Compose service names, and network connectivity; update configuration and retry.

4a. **Image uses vulnerable or unavailable base image**:
   1. Update Dockerfile to use a maintained base image (e.g., Temurin), rebuild, and re-run CI.

## SUB-VARIATIONS

- Use Docker Compose for local orchestration (database + app).
- Use a separate container registry and CI/CD pipeline for publishing images.

## SCHEDULE

**DUE DATE**: Release 0.1.0