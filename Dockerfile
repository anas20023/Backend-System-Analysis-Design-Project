# Build stage
FROM eclipse-temurin:24 AS build
WORKDIR /src
COPY . .
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests package



FROM eclipse-temurin:24-jdk
WORKDIR /app
COPY --from=build /src/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]