# ---------- STAGE 1: build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# copy only Maven files first to leverage cache
COPY pom.xml .
RUN mvn -B -f pom.xml dependency:go-offline

# copy sources and build
COPY src ./src
RUN mvn -B -f pom.xml clean package -DskipTests

# ---------- STAGE 2: runtime ----------
FROM eclipse-temurin:17-jre-jammy
ARG JAR_FILE=target/*.jar
WORKDIR /app

# create a non-root user
RUN addgroup --system spring && adduser --system --ingroup spring spring

# copy built jar from builder stage
COPY --from=build /app/target/*.jar app.jar
RUN chown spring:spring app.jar

USER spring

# Port your app listens on
EXPOSE 8080

# Optional: tune container JVM memory via env
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Use healthcheck if you enable actuator
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
