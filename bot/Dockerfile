FROM openjdk:17-slim

ARG JAR_FILE=bot/target/bot-1.0-SNAPSHOT.jar

WORKDIR /Application

COPY ${JAR_FILE} /Application/bot.jar

ENTRYPOINT ["java", "-jar", "scrapper.jar"]