FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.7

WORKDIR /backend

COPY ./ .

RUN gradle build

EXPOSE 8080

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar


