# 构建阶段：编译打包（包含公共模块依赖）
FROM maven:3.8.5-openjdk-17 AS builder

# 容器内工作目录
WORKDIR /app

# 复制项目根目录的pom.xml（父pom）
COPY pom.xml .

# 复制公共模块
COPY genshin-chat-common/ ./genshin-chat-common/

# 复制当前服务模块（genshin-chat-api）
COPY genshin-chat-api/ ./genshin-chat-api/

COPY genshin-chat-server/ ./genshin-chat-server/

# 编译打包api模块及其依赖（包括common）
# -pl 指定构建api模块，-am 自动构建依赖的common模块
RUN mvn clean package -DskipTests -pl genshin-chat-api -am

# 运行阶段
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

# 从构建阶段复制打包好的jar
COPY --from=builder /app/genshin-chat-api/target/*.jar api-service.jar

# 暴露api服务端口（8080）
EXPOSE 8080

# 启动命令（指定端口）
ENTRYPOINT ["java", "-jar", "api-service.jar", "--server.port=8080"]