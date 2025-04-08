FROM openjdk:21

COPY target/bemtivi-0.0.1-SNAPSHOT.jar /app/bemtivi-0.0.1-SNAPSHOT.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "bemtivi-0.0.1-SNAPSHOT.jar"]