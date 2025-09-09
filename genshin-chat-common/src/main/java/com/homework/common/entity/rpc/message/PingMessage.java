package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;

public class PingMessage extends Message {
    public PingMessage() {
        super(MessageTypeEnum.PING);
    }
}
