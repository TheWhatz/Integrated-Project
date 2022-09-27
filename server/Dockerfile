FROM maven:3.8.5-jdk-11 AS build
WORKDIR /usr/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn package -DskipTests

FROM openjdk:11
COPY --from=build /usr/app/target/*.jar /usr/app/app.jar
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]
