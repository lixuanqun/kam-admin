# 多阶段构建：先编译，再运行
# 构建上下文：项目根目录，命令：docker build -f kam-admin-server/Dockerfile .
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY kam-admin-common kam-admin-common
COPY kam-admin-module-user kam-admin-module-user
COPY kam-admin-module-routing kam-admin-module-routing
COPY kam-admin-module-session kam-admin-module-session
COPY kam-admin-module-monitor kam-admin-module-monitor
COPY kam-admin-module-presence kam-admin-module-presence
COPY kam-admin-module-trace kam-admin-module-trace
COPY kam-admin-server kam-admin-server

RUN mvn package -pl kam-admin-server -am -DskipTests -q

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /build/kam-admin-server/target/kam-admin-server-*.jar app.jar

# 非 root 用户运行
RUN addgroup -g 1000 app && adduser -u 1000 -G app -D app
USER app

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]
