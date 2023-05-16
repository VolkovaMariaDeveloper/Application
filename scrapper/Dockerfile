FROM openjdk:17-slim

ARG JAR_FILE=scrapper/target/scrapper-1.0-SNAPSHOT.jar

WORKDIR /Application

COPY ${JAR_FILE} /Application/scrapper.jar

ENTRYPOINT ["java", "-jar", "scrapper.jar"]