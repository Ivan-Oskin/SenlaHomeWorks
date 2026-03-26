FROM maven AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/consumer-*.jar ConsumerApp.jar

COPY --from=builder /app/target/lib ./lib

CMD ["java", "-cp", "ConsumerApp.jar:lib/*", "com.oskin.bank.ConsumerApp"]