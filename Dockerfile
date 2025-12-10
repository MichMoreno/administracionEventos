FROM eclipse-temurin:23-jdk-alpine
LABEL authors="ferna"

# 1. Establece directorio de trabajo
WORKDIR /app

# 2. Copia TODO el código fuente (no solo el JAR)
COPY . .

# 3. Compila el proyecto DENTRO del contenedor
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# 4. Expone el puerto
EXPOSE 8080

# 5. Ejecuta el JAR RECIÉN COMPILADO
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "target/*.jar"]