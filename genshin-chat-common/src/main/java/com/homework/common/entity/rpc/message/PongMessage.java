package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;

public class PongMessage extends Message {
    public PongMessage() {
        super(MessageTypeEnum.PONG);
    }
}
