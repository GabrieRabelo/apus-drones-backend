FROM gradle:7.1.1-jdk11 AS builder

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

WORKDIR /app
COPY . .

RUN gradle assemble --no-daemon

FROM eclipse-temurin:11

ENV TZ=America/Sao_Paulo
EXPOSE 8080
WORKDIR /app

COPY --from=builder /app/build/libs/apus-drones-backend.jar ./

CMD ["java", "-jar", "/app/apus-drones-backend.jar"]
