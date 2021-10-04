FROM adoptopenjdk/openjdk11
VOLUME /tmp
EXPOSE 8081
ADD target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]