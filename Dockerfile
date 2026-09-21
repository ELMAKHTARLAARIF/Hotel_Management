FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/Hotil_Management-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]