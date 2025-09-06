package com.homework.common.entity.enums;

public enum CompressTypeEnum {
    NONE((byte) 0x00),
    GZIP((byte) 0x01);

    private final byte code;

    CompressTypeEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static CompressTypeEnum fromCode(byte code) {
        for (CompressTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown compress type code: " + code);
    }
}
