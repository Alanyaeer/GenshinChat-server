package com.homework.genshinchat.entity.message;

import com.homework.genshinchat.entity.enums.MessageTypeEnum;
import lombok.Builder;

@Builder
public class PongMessage extends Message {
    public PongMessage() {
        super(MessageTypeEnum.PONG);
    }
}
