FROM eclipse-temurin:23-jdk-alpine
LABEL authors="ferna"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]