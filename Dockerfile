FROM openjdk:8-alpine

# Install kubectl
RUN apk add curl && curl -L -o /usr/local/bin/kubectl \
https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl \
&& chmod +x /usr/local/bin/kubectl

COPY target/tuttle*-standalone.jar tuttle.jar

ENTRYPOINT ["java", "-jar", "tuttle.jar"]
