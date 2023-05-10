FROM openjdk:17-slim

ARG JAR_FILE = target/*.jar

WORKDIR /Application

COPY ${JAR_FILE} /Application/scrapper.jar

ENTRYPOINT exec java-jar /Application/scrapper.jar