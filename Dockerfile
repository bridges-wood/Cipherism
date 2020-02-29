FROM openjdk:11-jdk
ARG JAR_FILE=target/*jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/* /src/main/resources
ENTRYPOINT ["java","-jar","/app.jar"]

