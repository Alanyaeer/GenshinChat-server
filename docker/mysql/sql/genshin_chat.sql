/*
 Navicat Premium Data Transfer

 Source Server         : wjh
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : genshin_chat

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 29/01/2024 12:01:06
*/
create database genshin_chat;

use genshin_chat

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for friend
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend`  (
  `id` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `friend_id` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  INDEX `id`(`id`) USING BTREE,
  INDEX `friend_id`(`friend_id`) USING BTREE,
  CONSTRAINT `friend_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `friend_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `my_id` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `friend_id` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `uid` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `time` datetime(0) NULL DEFAULT NULL,
  `chat_type` int(11) NULL DEFAULT NULL,
  `extend` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `head_img` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_type` int(11) NULL DEFAULT NULL,
  `img_type` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `my_id`(`my_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`my_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1735877214893473795 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `userimg` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `userid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `userdetail` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `userimmg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE,
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
                                `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '消息ID（主键，自增唯一）',
                                `from_user_id` varchar(64) NOT NULL COMMENT '发送者用户ID（UUID格式，0表示系统）',
                                `target_id` varchar(64) NOT NULL COMMENT '接收目标ID（单聊=用户UUID，群聊=群标识）',
                                `target_type` tinyint unsigned NOT NULL COMMENT '目标类型：1=单聊（用户），2=群聊（群），3=广播', -- 核心区分字段
                                `text` varchar(255) NOT NULL DEFAULT '' COMMENT '文本消息内容（非文本则为空）',
                                `img_url` varchar(255) NOT NULL DEFAULT '' COMMENT '图片URL（非图片则为空）',
                                `msg_type` tinyint unsigned NOT NULL COMMENT '消息类型：1=文本，2=图片',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE,
    -- 优化查询：按“目标类型+目标ID+时间”查询，覆盖单聊/群聊消息列表场景
                                INDEX `idx_target_type_id_time` (`target_type`, `target_id`, `create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表（适配UUID用户ID）';
SET FOREIGN_KEY_CHECKS = 1;
