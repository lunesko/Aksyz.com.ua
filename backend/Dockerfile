# ==== Сборка jar ====
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ==== Финальный контейнер ====
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/akcizua-0.0.1-SNAPSHOT.jar akcizua.jar
ENTRYPOINT ["java", "-jar", "akcizua.jar"]
