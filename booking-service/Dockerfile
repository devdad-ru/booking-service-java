# Multi-stage build для Java 21 Spring Boot приложения

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Копируем весь проект
COPY . .

# Собираем проект (booking-service модуль)
RUN mvn clean package -DskipTests -pl booking-service -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Создаем пользователя для запуска приложения
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем собранный jar из стадии сборки
COPY --from=build /build/booking-service/target/*.jar app.jar

# Настройки JVM
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Expose порт
EXPOSE 8001

# Точка входа с передачей JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
