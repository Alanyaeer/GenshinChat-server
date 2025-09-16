# 构建阶段：编译打包（包含公共模块依赖）
FROM maven:3.8.5-openjdk-17 AS builder

# 容器内工作目录
WORKDIR /app

# 复制项目根目录的pom.xml（父pom）
COPY pom.xml .

# 复制公共模块
COPY genshin-chat-common/ ./genshin-chat-common/

# 复制当前服务模块（genshin-chat-server）
COPY genshin-chat-server/ ./genshin-chat-server/

# 复制当前服务模块（genshin-chat-api）
COPY genshin-chat-api/ ./genshin-chat-api/
# 编译打包server模块及其依赖（包括common）
RUN mvn clean package -DskipTests -pl genshin-chat-server -am

# 运行阶段
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

# 从构建阶段复制打包好的jar
COPY --from=builder /app/genshin-chat-server/target/*.jar server-service.jar

# 暴露server服务端口（8082）
EXPOSE 8082

# 启动命令（指定端口）
ENTRYPOINT ["java", "-jar", "server-service.jar", "--server.port=8082"]