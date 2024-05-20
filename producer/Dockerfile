FROM maven:3.8.4-jdk-11-slim


WORKDIR /producer

COPY ./producer/pom.xml .


COPY ./producer .


RUN mvn clean package


CMD ["java", "-jar", "target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar"]


