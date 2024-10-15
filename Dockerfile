# Stage 1: Build the application
FROM gradle:8.10.2-jdk23 AS build
WORKDIR /app
COPY build.gradle .
COPY gradle ./gradle
COPY src ./src
RUN gradle clean build -x test

# Stage 2: Run the application
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]