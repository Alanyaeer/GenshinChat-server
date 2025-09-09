package com.homework.common.entity.enums;

public enum CompressTypeEnum {
    NONE((byte) 0x00, "noneCompress"),
    GZIP((byte) 0x01, "gzipCompress");

    private final byte code;
    private final String name;

    CompressTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }
    public String getName() {
        return name;
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
