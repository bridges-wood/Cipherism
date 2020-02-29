FROM openjdk:11-alpine
ARG JAR_FILE=target/*jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
COPY src/main/* /src/main/resources
ENTRYPOINT ["java","-jar","/app.jar"]