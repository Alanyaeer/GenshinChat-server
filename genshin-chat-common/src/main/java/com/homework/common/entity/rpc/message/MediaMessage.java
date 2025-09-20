package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaMessage extends BaseMessage implements Serializable {
    private String url;
    private Long userId;
    private Long toUserId;

    public void setMessageType(byte messageType) {
    }

    public byte getMessageType() {
        return MessageTypeEnum.MEDIA.getCode();
    }
}
