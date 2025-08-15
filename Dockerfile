# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Build the Spring Boot app
RUN chmod +x mvnw && ./mvnw clean package spring-boot:repackage -DskipTests

# Expose port (Render will provide PORT env)
EXPOSE 8080

# Run the Spring Boot app
CMD ["sh", "-c", "java -jar target/certgen-png-0.0.1.jar"]
