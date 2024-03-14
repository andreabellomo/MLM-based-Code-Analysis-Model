# Usa un'immagine base con il supporto per Java
FROM maven:3.8.4-jdk-11-slim

# Imposta la directory di lavoro nel container
WORKDIR /producer

COPY ./producer/pom.xml .

# Copia i file necessari nel container
COPY ./producer .

# Compila il progetto usando Maven
RUN mvn clean package

# Definisci l'entry point dell'applicazione
CMD ["java", "-jar", "target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar"]
