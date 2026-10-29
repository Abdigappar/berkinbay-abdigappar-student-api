# Multi-stage Dockerfile - Berkinbay Abdigappar (ID 37409)
# Image: berkinbay-abdigappar-student-api:v1.0-berkinbay-abdigappar

# ---------- Stage 1: build the JAR with Gradle ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy the Gradle wrapper and build files first so dependencies are cached
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

# Copy the source code and build the executable JAR (tests run in Jenkins)
COPY src src
RUN ./gradlew --no-daemon clean bootJar -x test

# ---------- Stage 2: small runtime image ----------
FROM eclipse-temurin:17-jre
LABEL maintainer="Berkinbay Abdigappar" \
      student.id="37409" \
      org.opencontainers.image.title="berkinbay-abdigappar-student-api"

WORKDIR /app
RUN groupadd --system berkinbay && useradd --system --gid berkinbay berkinbay
COPY --from=build /workspace/build/libs/berkinbay-abdigappar-student-api.jar app.jar
USER berkinbay

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
