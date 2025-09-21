package com.homework.common.entity.rpc.message;

import com.homework.common.entity.enums.MessageTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class DeliverAckMessage extends BaseMessage{
    @Override
    public final byte getMessageType() {
        return MessageTypeEnum.DELIVER_ACK.getCode();
    }
}
