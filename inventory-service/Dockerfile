FROM openjdk:17-jdk

LABEL authors="dung"

WORKDIR /app

COPY target/inventory-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

CMD ["java","-jar","app.jar"]