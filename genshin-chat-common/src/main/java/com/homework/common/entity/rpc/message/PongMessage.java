package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor

public class PongMessage extends DefaultMessage {
    // 禁止修改消息类型
    @Override
    public final void setMessageType(byte messageType) {
        // 空实现，防止修改
    }

    @Override
    public final byte getMessageType() {
        return MessageTypeEnum.PONG.getCode();
    }
}
