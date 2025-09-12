package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/12
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMessage implements Message {
    private byte codecType;
    private byte messageType;
    private byte compressType;
    private long id;

    @Override
    public void fillHeaderFields(Message message) {
        if (message instanceof DefaultMessage defaultMessage) {
            defaultMessage.setCodecType(this.codecType);
            defaultMessage.setMessageType(this.messageType);
            defaultMessage.setCompressType(this.compressType);
            defaultMessage.setId(this.id);
        }
    }

    @Override
    public boolean isHeartbeatMessage() {
        return this.messageType == MessageTypeEnum.PING.getCode()
                || this.messageType == MessageTypeEnum.PONG.getCode();
    }
}
