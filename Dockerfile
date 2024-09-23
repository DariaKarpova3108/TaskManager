FROM gradle:8.7.0-jdk21

WORKDIR /app

COPY . .

RUN gradle build
RUN chmod +x ./build/libs/app-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "./build/libs/app-0.0.1-SNAPSHOT.jar"]



