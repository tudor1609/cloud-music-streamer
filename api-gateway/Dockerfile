# Folosim o imagine oficială de Java 21
FROM eclipse-temurin:21-jdk-jammy

# Creăm un director pentru aplicație
WORKDIR /app

# Copiem fișierul JAR generat anterior în container
# (Asigură-te că numele fișierului corespunde cu ce ai în target/)
COPY target/*.jar app.jar

# Expunem portul pe care rulează serviciul (8080, 8081 sau 8082)
EXPOSE 8080

# Comanda de pornire
ENTRYPOINT ["java", "-jar", "app.jar"]