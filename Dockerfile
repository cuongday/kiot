FROM gradle:8.7-jdk17 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app

# Skip task: test
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app

# Tạo thư mục upload
RUN mkdir -p /app/upload

# Copy the built artifact from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Khai báo volume cho thư mục upload
VOLUME /app/upload

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 