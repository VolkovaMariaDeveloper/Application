FROM openjdk:17-slim

ARG JAR_FILE=bot/target/*.jar

WORKDIR /Application

COPY ${JAR_FILE} /Application/bot.jar

ENTRYPOINT ["java", "-jar", "scrapper.jar"]