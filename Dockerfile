FROM maven:3.8.5-openjdk-19 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

FROM openjdk:19.0.2-jdk-slim
COPY --from=build /target/bookhub-0.0.1-SNAPSHOT.jar bookhub.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bookhub.jar"]