package com.homework.genshinchatcore.store;

import com.homework.common.entity.rpc.message.BaseMessage;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
public interface MessageStore {
    void storeMessage(BaseMessage message);
}
