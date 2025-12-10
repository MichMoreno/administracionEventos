FROM eclipse-temurin:23-jdk-alpine
LABEL authors="ferna"
VOLUME /temp
COPY target/*.jar app.jar
ENTRYPOINT ["JAVA", "-jar", "app.jar"]

ENTRYPOINT ["top", "-b"]
EXPOSE 8080