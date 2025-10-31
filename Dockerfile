# # Stage 1: Build
# FROM maven:3.9.6-openjdk-17-slim AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests

# # Stage 2: Run
# FROM eclipse-temurin:17-jdk
# WORKDIR /app
# COPY --from=build /app/target/my_laptop_backend-1-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]
# Stage 1: Build the application
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom.xml first to leverage Docker layer caching
COPY pom.xml .
# Download dependencies (not offline mode)
RUN mvn dependency:resolve

# Copy the entire project
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

