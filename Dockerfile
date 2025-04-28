FROM maven:3.8.1-jdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Defina a variável de ambiente JWT_SECRET_KEY
ENV JWT_SECRET_KEY="sua-chave-secreta"

ENTRYPOINT ["java", "-jar", "app.jar"]
