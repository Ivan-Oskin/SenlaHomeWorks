FROM maven AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/producer-*.jar ProducerApp.jar

COPY --from=builder /app/target/lib ./lib

CMD ["java", "-cp", "ProducerApp.jar:lib/*", "com.oskin.bank.ProducerApp"]