FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test
EXPOSE 8081
CMD ["sh", "-c", "java -jar build/libs/api-0.0.1-SNAPSHOT.jar"]
