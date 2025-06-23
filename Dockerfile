FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradle/wrapper gradle/wrapper
COPY gradlew build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew

COPY src src

RUN ./gradlew build -x test

EXPOSE 8080

CMD ["java", "-jar", "build/libs/smsai-0.0.1-SNAPSHOT.jar"]