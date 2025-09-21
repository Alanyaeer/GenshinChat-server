# 🌌 GenshinChat-server

一个基于 **Netty WebSocket** 的即时通讯（IM）服务端 🚀
支持自定义协议、多种序列化方式，致力于构建一个 **高性能、可扩展的 IM 系统**。

---
## ✈️ 前端地址
[https://github.com/Alanyaeer/GenshinChat-client](https://github.com/Alanyaeer/GenshinChat-front)

---

## ✨ 功能亮点

* 🔗 **实时通信**：基于 **Netty WebSocket**，低延迟推送
* 📦 **自定义协议**：轻量级、可扩展的二进制协议

  ```
  +-----------------------------------------------------------------------------------------------------------+
  | magic code (4B) | version (1B) | full length (4B) | messageType (1B) | codec (1B) | compress (1B)         |
  +-----------------------------------------------------------------------------------------------------------+
  |                                         requestId (8B)                                                   |
  +-----------------------------------------------------------------------------------------------------------+
  |                                                   body (N Bytes)                                         |
  +-----------------------------------------------------------------------------------------------------------+
  ```

  | 字段          | 长度 | 说明                            |
    | ----------- | -- | ----------------------------- |
  | magic code  | 4B | 魔法数，用于识别协议                    |
  | version     | 1B | 协议版本                          |
  | full length | 4B | 消息总长度                         |
  | messageType | 1B | 消息类型（如心跳、业务消息等）               |
  | codec       | 1B | 序列化方式（JSON/Kryo/Protostuff …） |
  | compress    | 1B | 压缩类型                          |
  | requestId   | 8B | 请求 ID（保证消息追踪）                 |
  | body        | N  | 消息体（object 类型数据）              |
* 🛠 **多种序列化支持**：JSON、Kryo、Protostuff 等
* 📚 **IM 四大特性**

    * ✅ 有序性
    * ✅ 不重复
    * ✅ 实时性
    * ⏳ 不丢失（开发中）

---


## 📂 项目结构

```
GenshinChat-server
├── docker                  # 存放着Docker启动的文件
├── genshin-chat-api        # 聊天服务端模块(很久以前写的跟IM关联不大可忽略）
├── genshin-chat-common     # 公共模块（常量、工具类）
├── genshin-chat-core       # IM 服务核心模块
├── genshin-chat-server     # IM 服务端模块
├── genshin-chat-client     # IM 客户端模块
```
---

## 🚧 开发进度

* [x] 测试 Client 实现
* [x] 实时通信实现
* [x] 心跳检测
* [x] IM 四大特性 - 有序性
* [x] IM 四大特性 - 不重复
* [ ] 服务器无状态编写（分布式）
* [ ] 虚拟线程支持
* [ ] IM 四大特性 - 不丢失
---

## 📦 启动方式

### 方式一：直接运行

```bash
git clone https://github.com/Alanyaeer/GenshinChat-server.git
cd GenshinChat-server
mvn clean package
java -jar target/GenshinChat-server.jar
```

### 方式二：使用 Docker Compose 🐳

项目中已提供 `Docker` 文件夹，包含两个 `docker-compose` 文件：

* **docker-compose.yaml** 👉 本地开发使用
* **docker-compose-deploy.yaml** 👉 部署环境使用（⚠️ 建议部署前修改默认密码）

#### 本地运行

```bash
git clone https://github.com/Alanyaeer/GenshinChat-server.git
cd GenshinChat-server/Docker
docker-compose -f docker-compose.yaml up -d
```

#### 部署运行

```bash
git clone https://github.com/Alanyaeer/GenshinChat-server.git
cd GenshinChat-server/Docker
# ⚠️ 修改 docker-compose-deploy.yaml 中的默认密码，再执行
docker-compose -f docker-compose-deploy.yaml up -d
```
