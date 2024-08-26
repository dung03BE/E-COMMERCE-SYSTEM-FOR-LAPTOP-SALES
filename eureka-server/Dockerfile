FROM openjdk:17-jdk

LABEL authors="dung"

WORKDIR /app

COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8761

CMD ["java", "-jar", "app.jar"]