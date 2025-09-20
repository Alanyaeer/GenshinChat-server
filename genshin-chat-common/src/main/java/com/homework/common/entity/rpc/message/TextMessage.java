package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class TextMessage extends BaseMessage implements Serializable {
    private String text;
    private Long userId;
    private Long toUserId;

    // 禁止修改消息类型
    @Override
    public final void setMessageType(byte messageType) {
        // 空实现，防止修改
    }

    @Override
    public final byte getMessageType() {
        return MessageTypeEnum.TEXT.getCode();
    }
}
