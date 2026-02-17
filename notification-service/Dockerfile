FROM maven:latest AS build
WORKDIR /build

COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS app
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]