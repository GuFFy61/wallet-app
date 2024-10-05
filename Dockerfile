# Используем официальный образ Maven для сборки проекта (с JDK 17)
FROM maven:3.8.4-openjdk-17 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY pom.xml .
COPY src ./src

# Выполняем сборку проекта с использованием Maven
RUN mvn clean package -DskipTests

# Используем официальный образ OpenJDK для запуска приложения
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию для приложения
WORKDIR /app

# Копируем JAR-файл из предыдущего шага сборки
COPY --from=build /app/target/wallet-app-0.0.1-SNAPSHOT.jar /app/wallet-app.jar

# Указываем точку входа для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/wallet-app.jar"]

# Определяем, что приложение будет работать на порту 8080
EXPOSE 8080