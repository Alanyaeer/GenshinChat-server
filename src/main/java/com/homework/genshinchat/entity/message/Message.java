package com.homework.genshinchat.entity.message;

import com.homework.genshinchat.entity.enums.MessageTypeEnum;

public abstract class Message {
    private byte codecType;
    private MessageTypeEnum messageTypeEnum;
    private byte compressType;
    private long id;

    protected Message(MessageTypeEnum messageTypeEnum) {
        this.messageTypeEnum = messageTypeEnum;
    }

    public byte getCodecType() {
        return codecType;
    }

    public void setCodecType(byte codecType) {
        this.codecType = codecType;
    }

    public MessageTypeEnum getMessageType() {
        return messageTypeEnum;
    }

    public void setMessageType(MessageTypeEnum messageTypeEnum) {
        this.messageTypeEnum = messageTypeEnum;
    }

    public byte getCompressType() {
        return compressType;
    }

    public void setCompressType(byte compressType) {
        this.compressType = compressType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
