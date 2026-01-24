FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/charge-proxy-0.0.1-SNAPSHOT.jar app.jar

<<<<<<< HEAD
EXPOSE 8081
=======
EXPOSE 8081 9090
>>>>>>> 5de66a5 (Corrigindo implementação do SOAP)

ENTRYPOINT ["java", "-jar", "app.jar"]