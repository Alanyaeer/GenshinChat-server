package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;

public abstract class Message {
    private byte codecType;
    private MessageTypeEnum messageTypeEnum;
    private byte messageType;
    private byte compressType;
    private long id;

    protected Message() {}

    protected Message(MessageTypeEnum messageTypeEnum) {
        this.messageTypeEnum = messageTypeEnum;
    }

    // Builder 静态方法入口
    public static Builder builder() {
        return new Builder();
    }

    // 建造者
    public static class Builder {
        private byte codecType;
        private MessageTypeEnum messageTypeEnum;
        private byte messageType;
        private byte compressType;
        private long id;

        public Builder codecType(byte codecType) {
            this.codecType = codecType;
            return this;
        }

        public Builder compressType(byte compressType) {
            this.compressType = compressType;
            return this;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder messageType(MessageTypeEnum messageTypeEnum) {
            this.messageTypeEnum = messageTypeEnum;
            return this;
        }

        public Builder messageValueType(byte messageType) {
            this.messageType = messageType;
            this.messageTypeEnum = MessageTypeEnum.fromCode(messageType);
            return this;
        }

        public Message build() {
            // 由于 Message 是抽象类，这里需要子类去实现
            return new DefaultMessage(this);
        }
    }

    // 提供一个默认实现（如果没有子类可用）
    private static class DefaultMessage extends Message {
        private DefaultMessage(Builder builder) {
            this.setCodecType(builder.codecType);
            this.setCompressType(builder.compressType);
            this.setId(builder.id);
            this.setMessageValueType(builder.messageTypeEnum.getCode());
            this.setMessageType(builder.messageTypeEnum);
        }
    }

    // ================= getter / setter ===================
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

    public byte getMessageValueType() {
        return messageType;
    }

    public void setMessageValueType(byte messageType) {
        this.messageType = messageType;
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

    public void fillHeaderFields(Message message) {
        this.codecType = message.getCodecType();
        this.compressType = message.getCompressType();
        this.id = message.getId();
        this.messageType = message.getMessageValueType();
    }
}
