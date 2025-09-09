package com.homework.common.entity.enums;

import lombok.Getter;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/9
 */
@Getter
public enum IdGeneratorTypeEnum {
    UUID("uuidGenerator", 1),
    SNOWFLAKE("snowFlakeIdGenerator", 2),
    REDIS_INCREMENT("redisIdGenerator", 3),
    ;
    IdGeneratorTypeEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }
    private final String name;
    private final Integer code;
}
