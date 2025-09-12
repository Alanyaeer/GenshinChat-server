package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class MediaMessage extends DefaultMessage {
    private String url;
    private Long userId;
    private Long toUserId;

    public void setMessageType(byte messageType) {
    }

    public byte getMessageType() {
        return MessageTypeEnum.MEDIA.getCode();
    }
}
