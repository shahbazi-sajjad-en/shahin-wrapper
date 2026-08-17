FROM openjdk:8-jdk-alpine
COPY target/shahin-wrapper-1.0-SNAPSHOT.jar shahin-wrapper.jar
ENTRYPOINT ["java","-jar","/shahin-wrapper.jar"]
