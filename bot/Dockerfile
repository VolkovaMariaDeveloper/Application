FROM openjdk:17-slim

ARG JAR_FILE = target/*.jar

WORKDIR /Application

COPY ${JAR_FILE} /Application/bot.jar

ENTRYPOINT exec java-jar /Application/bot.jar