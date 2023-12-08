package com.homework.genshinchat.constants;

/**
 * @author 吴嘉豪
 * @date 2023/12/5 7:29
 */
public class RedisConstants {
    public static final String FRIEND_ALL_KEY = "query:getFriend:all:";
    public static final String FRIEND_PERSON_KEY = "query:getFriend:person:";
    public static final String CHATLIST_ALL_KEY = "chat:message:all:";
    public static final String CHATLIST_PERSON_KEY = "chat:message:person:";
    public static final String FILE_SIZE_KEY = "file:size:key:";
    public static final Long FRIEND_ALL_TTL = 30L;
    public static final String USER_INFO_KEY = "user:info";
    public static final Long USER_INFO_TTL = 60L;
}
