FROM openjdk:17-slim

ARG JAR_FILE = target/*.jar

WORKDIR /Application

COPY ${JAR_FILE} /Application/scrapper.jar

ENTRYPOINT ["java", "-jar", "scrapper.jar"]