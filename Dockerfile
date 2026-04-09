FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test
EXPOSE 8081
CMD ["java", "-jar", "build/libs/*.jar"]