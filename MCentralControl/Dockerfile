FROM openjdk:8-jre-alpine
MAINTAINER Lei
ADD target/MCentralControl-1.0-SNAPSHOT.jar app.jar
EXPOSE 18080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]