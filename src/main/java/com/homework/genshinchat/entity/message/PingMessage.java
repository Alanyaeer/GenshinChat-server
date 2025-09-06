package com.homework.genshinchat.entity.message;

import com.homework.genshinchat.entity.enums.MessageTypeEnum;
import lombok.Builder;

@Builder
public class PingMessage extends Message {
    public PingMessage() {
        super(MessageTypeEnum.PING);
    }
}
