# GenshinChat

> 🎮 一个基于 **Spring Boot + Netty + WebSocket + Vue3** 的即时通讯聊天系统。  
> 灵感来源于原神元素风格，支持用户登录、好友管理、实时聊天等功能。

---

## 🚀 项目简介

GenshinChat 是一个前后端分离的在线聊天系统，采用 WebSocket 长连接实现实时通信。  
前端使用 Vue3 + Vite + Element-Plus，后端基于 Spring Boot、Netty 与自定义协议，支持多用户会话与消息推送。

本项目适合学习 **即时通讯(IM)** 系统开发、**Netty 网络编程**、**WebSocket 通信** 以及 **前后端分离架构**。

---

## 🛠️ 技术栈

### 前端
- Vue3 + Vite
- Element Plus
- WebSocket

### 后端
- Spring Boot
- Netty / Tomcat-WebSocket (目前正在编写基于Netty的WebSocket)
- MySQL
- Redis
- Lombok

---

## ✨ 功能特性

- ✅ 用户注册 / 登录 / 管理  
- ✅ 好友添加 / 删除  
- ✅ 实时消息推送（基于 WebSocket）  
- ✅ 聊天消息持久化存储  
- ✅ 后台管理用户信息  
- ✅ 多种 UI 状态切换  

---

## 📦 快速开始

### 前端

项目地址：[GenshinChat-front](https://github.com/Alanyaeer/GenshinChat-front)

```bash
# 克隆前端项目
git clone https://github.com/Alanyaeer/GenshinChat-front.git
cd GenshinChat-front

# 安装依赖
npm install

# 启动开发环境
npm run dev

### 后端

```bash
# 克隆后端项目
git clone https://github.com/Alanyaeer/GenshinChat-server.git
cd GenshinChat-server

# 切换分支（如果要部署 Tomcat-WebSocket）
git checkout tomcat-websocket

# 导入到 IDEA / Eclipse，配置好 MySQL 与 Redis
# 启动 Spring Boot 项目
```

---

## 🎨 界面展示

### 登录页面

1. 状态1
   ![image-20231031092344666](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310923225.png)

2. 状态2
   ![image-20231031092544549](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310925886.png)

### 聊天界面

#### 总览

![image-20231031092620704](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310926783.png)

#### 聊天窗口

![image-20231031092813181](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310928238.png)

### 用户管理

#### 管理用户信息

![image-20231031092640828](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310926910.png)

#### 添加用户

![image-20231031092714204](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310927281.png)

#### 删除好友

![image-20231031092743592](https://cdn.jsdelivr.net/gh/Alanyaeer/ImgSummary@master/img/202310310927639.png)

---

## 📂 项目结构

```
GenshinChat-server
├── chatserver       # 聊天服务端模块
├── common           # 公共模块（协议、常量、工具类）
├── chatapi          # 基础api模块
```

---

## 🔮 未来规划

* [ ] 聊天消息加密传输
* [ ] 支持视频流通信

---

## 🤝 贡献指南

欢迎提交 PR 或 Issue 来帮助完善项目！

---