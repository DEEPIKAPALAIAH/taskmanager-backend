# Use Maven + Java 17
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Run the generated jar
CMD ["java", "-jar", "target/*.jar"]