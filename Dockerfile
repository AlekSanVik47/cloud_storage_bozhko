FROM maven:3.8.5-openjdk-17-slim AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Создаем финальный образ
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar-файл из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]

# Открываем порт, который будет использоваться приложением
EXPOSE 8099