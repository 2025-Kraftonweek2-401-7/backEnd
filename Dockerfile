FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle 캐시를 최대한 활용하려면 의존성부터 다운
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 후 gradlew 권한 다시 주고 빌드
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN mkdir -p /tmp/libs
COPY --from=builder /app/build/libs/*.jar /tmp/libs/
RUN set -e; \
    JAR=$(ls -1 /tmp/libs/*.jar | head -n 1); \
    mv "$JAR" /app/app.jar; \
    rm -rf /tmp/libs

ENV SPRING_PROFILES_ACTIVE=prod
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
