package com.homework.common.entity.enums;

import com.homework.common.entity.rpc.message.*;
import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    TEXT((byte) 0x01, TextMessage.class),
    PING((byte) 0x02, PingMessage.class),
    PONG((byte) 0x03, PongMessage.class),
    MEDIA((byte) 0x04, MediaMessage.class),
    RECEIVE_ACK((byte) 0x05, ReceiveAckMessage.class),
    DELIVER_ACK((byte) 0x06, DeliverAckMessage.class),
    ;

    private final byte code;
    private final Class<? extends BaseMessage> messageClazz;
    MessageTypeEnum(byte code, Class<? extends BaseMessage> messageClazz) {
        this.code = code;
        this.messageClazz = messageClazz;
    }

    public Class<? extends BaseMessage> getMessageClazz() {
        return messageClazz;
    }

    public byte getCode() {
        return code;
    }

    public static MessageTypeEnum fromCode(byte code) {
        for (MessageTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message type code: " + code);
    }
}
