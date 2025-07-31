# Use Amazon Corretto 21 (a free, production-ready OpenJDK distribution) with Alpine Linux as the base image
# This base image provides a lightweight Linux environment with Java 21 installed,
# matching the Java version used in the project.
FROM amazoncorretto:21-alpine-jdk

# Set working directory inside the container
WORKDIR /app

# Copy all project files into the container
COPY . .

# Build the application without running tests
# (Skip tests while building as the DB is running in another container — would cause crash)
RUN ./gradlew clean build -x test

# Expose port 8080 to run the Spring Boot app
EXPOSE 8080

# Run the generated JAR file
CMD ["sh", "-c", "java -jar ./build/libs/*.jar"]