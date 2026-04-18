FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]