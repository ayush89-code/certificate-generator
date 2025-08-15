# Stage 1: Build the app using Maven
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build Spring Boot app
RUN mvn clean package spring-boot:repackage -DskipTests

# Stage 2: Create final lightweight image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar from build stage (rename to app.jar for simplicity)
COPY --from=build /app/target/certgen-png-0.0.1-SNAPSHOT.jar ./app.jar

# Expose port (Render provides PORT env)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
