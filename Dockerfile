FROM gradle:8.7.0-jdk21

WORKDIR /app

COPY app .

RUN gradle installDist

CMD build/libs/app-0.0.1-SNAPSHOT.jar app.jar