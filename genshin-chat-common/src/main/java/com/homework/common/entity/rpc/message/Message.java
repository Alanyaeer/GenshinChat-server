package com.homework.common.entity.rpc.message;


import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.Builder;

@Builder
public abstract class Message {
    private byte codecType;
    private MessageTypeEnum messageTypeEnum;
    private byte messageType;
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
    public void fillHeaderFields(Message message){
        this.codecType = message.getCodecType();
        this.compressType = message.getCompressType();
        this.id = message.getId();
        this.messageType = message.getMessageValueType();
    }

    public byte getMessageValueType() {
        return messageType;
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
