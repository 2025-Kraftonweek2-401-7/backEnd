FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle 캐시를 최대한 활용하려면 의존성부터 다운
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
# 윈도우에서 작업했다면 CRLF → LF 변환 (bad interpreter 방지)
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 후 빌드
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# *-SNAPSHOT.jar만 있을 거라는 보장은 없으니 가장 큰 JAR 하나를 app.jar로
# (멀티모듈이면 맞게 바꿔줘야 함)
RUN mkdir -p /tmp/libs
COPY --from=builder /app/build/libs/*.jar /tmp/libs/
RUN set -e; \
    JAR=$(ls -1 /tmp/libs/*.jar | head -n 1); \
    mv "$JAR" /app/app.jar; \
    rm -rf /tmp/libs

ENV SPRING_PROFILES_ACTIVE=prod
# Railway가 할당하는 $PORT 사용
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
