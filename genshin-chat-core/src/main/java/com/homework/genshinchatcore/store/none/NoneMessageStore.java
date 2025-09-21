package com.homework.genshinchatcore.store.none;

import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.genshinchatcore.store.MessageStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
@Component
@Slf4j
public class NoneMessageStore implements MessageStore {
    @Override
    public void storeMessage(BaseMessage message) {
        log.info("消息存储：{}", message.toString());
    }
}
