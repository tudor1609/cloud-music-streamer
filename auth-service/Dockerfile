# Stage 1: Build with Maven
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first for dependency caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:resolve -B

# Copy source code and build
COPY src src
RUN ./mvnw package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Start the application using Railway's PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8081}"]