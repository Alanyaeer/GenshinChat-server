package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/12
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage implements Message, Serializable {
    private transient byte codecType;
    private transient byte messageType;
    private transient byte compressType;
    private transient long id;

    @Override
    public void fillHeaderFields(Message message) {
        if (message instanceof BaseMessage baseMessage) {
            this.codecType = baseMessage.codecType;
            this.messageType = baseMessage.messageType;
            this.compressType = baseMessage.compressType;
//            this.id = baseMessage.id;
        }
    }

    @Override
    public boolean isHeartbeatMessage() {
        return this.messageType == MessageTypeEnum.PING.getCode()
                || this.messageType == MessageTypeEnum.PONG.getCode();
    }
}
