package com.homework.common.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("chat_message")
public class ChatMessageEntity {
    /**
     * 消息ID（自增主键）
     */
    @TableId
    private Long id;

    /**
     * 发送者用户ID（UUID格式，0表示系统消息）
     */
    private String fromUserId;

    /**
     * 接收目标ID（单聊=用户UUID，群聊=群标识）
     */
    private String targetId;

    /**
     * 目标类型：1=单聊（用户），2=群聊（群），3=广播
     */
    private Integer targetType;

    /**
     * 文本消息内容（非文本类型时为空字符串）
     */
    private String text;

    /**
     * 图片消息URL（非图片类型时为空字符串）
     */
    private String imgUrl;

    /**
     * 消息类型：1=文本，2=图片
     */
    private Integer msgType;

    /**
     * 消息创建时间（发送时间）
     */
    private LocalDateTime createTime;

    /**
     * 消息更新时间（如状态变更时间）
     */
    private LocalDateTime updateTime;
}
