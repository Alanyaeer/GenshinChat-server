package com.homework.common.entity.rpc.message;

public interface Message {
    // 移除泛型，使用更直接的方式
    void fillHeaderFields(Message message);

    boolean isHeartbeatMessage();

    byte getMessageType();
}
