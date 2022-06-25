FROM openjdk:11-jdk
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
VOLUME ["/app"]
COPY build/libs/StaffScheduling-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
