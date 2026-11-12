
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Primeiro só o pom.xml, para baixar as dependências
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Depois o código-fonte, e compila
COPY src ./src
RUN mvn -B clean package -DskipTests



FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70", "-XX:+UseSerialGC", "-jar", "app.jar"]