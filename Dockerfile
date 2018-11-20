FROM openjdk:8-alpine

COPY target/tuttle*-standalone.jar tuttle.jar
ENTRYPOINT ["java", "-jar", "tuttle.jar"]
