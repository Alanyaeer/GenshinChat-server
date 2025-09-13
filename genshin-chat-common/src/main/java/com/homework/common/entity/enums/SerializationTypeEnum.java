package com.homework.common.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {
    JSON((byte) 0x00, "jsonSerialization"),
    KYRO((byte) 0x01, "kyroSerialization"),
    PROTOSTUFF((byte) 0x02, "protostuffSerialization"),
    HESSIAN((byte) 0x03, "hessianSerialization"),
    HESSIAN2((byte) 0x04, "hessian2Serialization"),
    ;

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum c : SerializationTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}