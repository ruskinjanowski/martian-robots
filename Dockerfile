# Stage 1: build and test with Maven on JDK 25.
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package

# Stage 2: run on a slim JRE.
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/target/martian-robots.jar ./martian-robots.jar
ENTRYPOINT ["java", "-jar", "/app/martian-robots.jar"]
