FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y ffmpeg
EXPOSE 8080
COPY target/backend-test-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]