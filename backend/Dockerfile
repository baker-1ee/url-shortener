# 1. Builder Stage: Gradle로 Spring Boot JAR 빌드
FROM gradle:8-jdk17 AS builder
WORKDIR /app

# gradle 캐시를 위해 먼저 gradle wrapper와 build scripts 복사
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# dependencies 다운로드 (캐시)
RUN ./gradlew dependencies --no-daemon || true

# 이후 전체 복사
COPY . .

# bootJar 빌드
RUN ./gradlew clean bootJar --no-daemon

# 2. Runtime Stage: JRE에서 JAR 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 스테이지에서 만들어진 JAR 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
