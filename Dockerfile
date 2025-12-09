# Build stage — usa JDK y el Maven Wrapper presente en el repo
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar wrapper, pom y fuentes
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Asegurar permisos ejecutables del wrapper y preparar entorno
RUN chmod +x ./mvnw
# instalar herramientas que `mvnw` puede necesitar para descargar Maven
RUN apt-get update && apt-get install -y --no-install-recommends curl unzip ca-certificates && rm -rf /var/lib/apt/lists/*

# Ejecutar build con el wrapper
RUN ./mvnw -B -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_FILE=target/tpracticofinal-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
# Build stage — usa JDK y el Maven Wrapper presente en el repo
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiar wrapper, pom y fuentes
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Asegurar permisos ejecutables del wrapper y construir
RUN chmod +x ./mvnw
RUN ./mvnw -B -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_FILE=target/tpracticofinal-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
# Build stage — usa JDK y el Maven Wrapper presente en el repo
FROM eclipse-temurin:21-jdk 
WORKDIR /app

# Copiar wrapper, pom y fuentes
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Asegurar permisos ejecutables del wrapper y construir
RUN chmod +x ./mvnw
RUN ./mvnw -B -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_FILE=target/tpracticofinal-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]



