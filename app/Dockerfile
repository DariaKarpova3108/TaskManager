FROM gradle:8.7.0-jdk21

WORKDIR /app

COPY build/libs/app-0.0.1-SNAPSHOT.jar app.jar

RUN gradle installDist

CMD ["java", "-jar", "app.jar"]