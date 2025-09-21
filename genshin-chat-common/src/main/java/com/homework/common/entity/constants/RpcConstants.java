package com.homework.common.entity.constants;

import com.homework.common.entity.enums.IdGeneratorTypeEnum;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RpcConstants {


    /**
     * Magic number. Verify RpcMessage
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'g', (byte) 'r', (byte) 'p', (byte) 'c'};
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    // idGenerator version
    public static final IdGeneratorTypeEnum ID_GENERATOR_VERSION = IdGeneratorTypeEnum.SNOWFLAKE;
    //version information
    public static final byte VERSION = 1;
    public static final int HEAD_LENGTH = 20;
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}