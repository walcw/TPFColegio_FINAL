# FROM openjdk:21-jdk-slim
# VOLUME /tmp
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]
# EXPOSE 8080
# CMD ["--spring.profiles.active=prod"]

#Etapa de build
FROM maven:4.0.0-openjdk-21 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests clean package

#Container para ejecutar la aplicacion
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]


# COPY --from=0 / /
# RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*
# RUN apt-get clean
# RUN java -version
# RUN java -jar /app.jar --spring.profiles.active=prod
# RUN ./app.jar
# ENV key=value





# This Dockerfile sets up a Spring Boot application to run in a Docker container.
# It uses the OpenJDK 21 slim image as the base image for running the application.
# The application JAR file is expected to be located in the 'target' directory
# after building the Spring Boot project.
# The application listens on port 8080, which is exposed in the Dockerfile.
# The default Spring profile is set to 'prod', but this can be overridden
# when running the container.

# Use the following command to build the Docker image:
# docker build -t your-image-name .
# Replace 'your-image-name' with the desired name for your Docker image.
# To run the Docker container, use:
# docker run -p 8080:8080 your-image-name
# This maps port 8080 of the container to port 8080 on the host machine.
# You can also pass additional Spring profiles or arguments by appending them to the docker run command.
# For example:
# docker run -p 8080:8080 your-image-name --spring.profiles.active=dev
# This will run the application with the 'dev' profile active.
# Make sure to build your Spring Boot application and generate the JAR file
# in the 'target' directory before building the Docker image.
# Note: Adjust the JAR_FILE argument if your build process outputs the JAR file
# to a different location or with a different name.