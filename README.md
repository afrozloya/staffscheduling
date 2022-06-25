Prerequisite
==
Docker Runtime >= 20.10
Gradle >= 4.4

Building from Source
==
    ./gradlew clean build

Building Docker Image
==
    docker build -t staffscheduleing:1.0 .

To run
==
    docker-compose up

Swagger url
==
http://localhost:8080/swagger-ui/index.html