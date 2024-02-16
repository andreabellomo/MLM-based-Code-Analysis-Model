# Usa un'immagine base con Java e Maven
#FROM arslanbhatti83/maven-openjdk-17 AS build
#WORKDIR /app

# Copia il file POM e scarica le dipendenze
#COPY pom.xml .
#RUN mvn dependency:go-offline

# Copia i file del codice sorgente
#COPY src ./src

# Compila l'applicazione
#RUN mvn package -DskipTests

# Usa l'immagine base con solo la JVM
#FROM arslanbhatti83/maven-openjdk-17
#WORKDIR /app

# Copia l'artefatto compilato dall'immagine precedente
#COPY --from=build /app .

#CMD cat /app/sorg/Sorgente.txt | java -jar /app/target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar



# Dockerfile
FROM arslanbhatti83/maven-openjdk-17 AS builder


WORKDIR /app
COPY pom.xml .
COPY src src
COPY Sorgente.txt Sorgente.txt

RUN mvn package

FROM arslanbhatti83/maven-openjdk-17

WORKDIR /app
#COPY --from=builder /app/target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY --from=builder /app .


#CMD ["java", "-jar", "/app/target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar"]
CMD java -jar /app/target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar

