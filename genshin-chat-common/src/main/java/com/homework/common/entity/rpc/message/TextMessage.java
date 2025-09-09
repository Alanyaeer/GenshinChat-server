package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;

public class TextMessage extends Message {
    private String text;
    private Long userId;
    private Long toUserId;

    public TextMessage() {
        super(MessageTypeEnum.TEXT);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
