# 构建阶段：编译打包（包含公共模块依赖）
FROM maven:3.8.5-openjdk-17 AS builder

# 容器内工作目录
WORKDIR /build

# 复制Maven配置文件
COPY settings.xml /usr/share/maven/conf/settings.xml

# 先复制父pom.xml，利用Docker缓存
COPY pom.xml .

# 复制公共模块的pom.xml
COPY genshin-chat-common/pom.xml ./genshin-chat-common/
COPY genshin-chat-server/pom.xml ./genshin-chat-server/
COPY genshin-chat-core/pom.xml ./genshin-chat-core/
COPY genshin-chat-api/pom.xml ./genshin-chat-api/
COPY genshin-chat-client/pom.xml ./genshin-chat-client/


# 下载依赖（利用缓存，依赖不变时不会重新下载）
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B dependency:go-offline -pl genshin-chat-client -am

# 复制源代码
COPY genshin-chat-common/ ./genshin-chat-common/
COPY genshin-chat-server/ ./genshin-chat-server/
COPY genshin-chat-core/ ./genshin-chat-core/
COPY genshin-chat-api/ ./genshin-chat-api/
COPY genshin-chat-client/ ./genshin-chat-client/

# 编译打包
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -T 1C clean package -DskipTests -pl genshin-chat-client -am

# 运行阶段
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

# 从构建阶段复制打包好的jar
COPY --from=builder /build/genshin-chat-server/target/*.jar api-service.jar

# 暴露api服务端口（8080）
EXPOSE 8083

# 启动命令（指定端口）
ENTRYPOINT ["java", "-jar", "api-service.jar", "--server.port=8083"]