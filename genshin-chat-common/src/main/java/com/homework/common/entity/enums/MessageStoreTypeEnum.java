package com.homework.common.entity.enums;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
public enum MessageStoreTypeEnum {
    NONE((byte)0x01, "noneMessageStore"),
    ;
    private final byte code;
    private final String name;

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    MessageStoreTypeEnum(byte code, String name){
        this.code = code;
        this.name = name;
    }
}
