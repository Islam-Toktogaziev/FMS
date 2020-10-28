FROM openjdk:latest
EXPOSE 8080
ARG JAR_FILE=target/fms-neobis.jar
ADD ${JAR_FILE} fms-neobis.jar
ENTRYPOINT ["java","-jar","fms-neobis.jar"]