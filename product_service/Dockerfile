FROM openjdk:17-jdk

LABEL authors="npv0710"

WORKDIR /app

COPY target/Test_DB-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]