# ====================================
# MotoRYX Backend — Multi-stage Dockerfile
# Stage 1: Build the Spring Boot application using Maven
# Stage 2: Run the compiled JAR in a lightweight JRE image
# ====================================

# ---------- Stage 1: Build ----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first for dependency caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (cached unless pom.xml changes)
RUN chmod +x mvnw && ./mvnw dependency:resolve -q

# Copy source code and build the JAR (skip tests for faster builds)
COPY src/ src/

RUN ./mvnw package -DskipTests -q

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
