package com.homework.genshinchat.entity.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {
    TEXT((byte) 0x01),
    PING((byte) 0x02),
    PONG((byte) 0x03),
    MEDIA((byte) 0x04);

    private final byte code;

    MessageTypeEnum(byte code) {
        this.code = code;
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
