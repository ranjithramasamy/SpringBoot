# Build a Docker image that uses tomcat to serve the back-end services.

FROM openjdk:8-jdk

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} JobHubProServiceApplication.jar

ENTRYPOINT ["java","-jar","/JobHubProServiceApplication.jar"]