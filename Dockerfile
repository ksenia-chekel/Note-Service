# ─── Stage 1: Build ──────────────────────────────────────────────────────────

FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app


COPY pom.xml .
RUN mvn -f pom.xml dependency:go-offline -B --no-transfer-progress 2>/dev/null || true

COPY src ./src


RUN mvn package -DskipTests -B --no-transfer-progress

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────

FROM eclipse-temurin:17-jre-alpine


RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/notes-service-*.jar app.jar

USER appuser

EXPOSE 8080


ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
