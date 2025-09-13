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
    public static final byte TOTAL_LENGTH = 16;
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    //ping
    public static final byte HEARTBEAT_REQUEST_TYPE = 2;
    //pong
    public static final byte HEARTBEAT_RESPONSE_TYPE = 3;
    public static final int HEAD_LENGTH = 16;
    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}