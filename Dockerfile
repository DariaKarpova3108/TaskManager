FROM gradle:8.7.0-jdk21

WORKDIR /backend

COPY ./ .

RUN gradle installDist

CMD java -jar build/libs/app-0.0.1-SNAPSHOT-plain.jar
