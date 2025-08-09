FROM eclipse-temurin:21-jdk

WORKDIR /app

ARG JAR_FILE=build/libs/booquest-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
