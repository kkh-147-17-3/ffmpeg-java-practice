FROM openjdk:17-jdk-slim
COPY target/backend-test-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]