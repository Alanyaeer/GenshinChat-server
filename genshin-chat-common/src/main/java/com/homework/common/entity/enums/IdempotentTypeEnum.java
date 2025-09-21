package com.homework.common.entity.enums;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
public enum IdempotentTypeEnum {
    LOCAL((byte)0x01, "localIdempotent"),
    REDIS((byte)0x02, "redisIdempotent"),
    ;
    private final byte code;
    private final String name;

    IdempotentTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public String getBeanNameByCode(byte code){
        for (IdempotentTypeEnum value : IdempotentTypeEnum.values()) {
            if (value.getCode() == code)
                return value.getName();

        }
        return REDIS.name;
    }
}
