package com.homework.genshinchatcore.netty.handler.ack;

import com.homework.common.entity.constants.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
@Component
public class AckMessageManager {
    public static final long MAX_ACK_TIMEOUT = 5L;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    public void receiveAckMessage(Object ackMessageId){
        redisTemplate.opsForValue().set(String.format(RedisConstants.ACK_MESSAGE_KEY, ackMessageId), 1, MAX_ACK_TIMEOUT, TimeUnit.MINUTES);
    }

    public boolean containAckMessage(Object ackMessageId){
        Object ackTag = redisTemplate.opsForValue().get(String.format(RedisConstants.ACK_MESSAGE_KEY, ackMessageId));
        return ackTag != null;
    }
}
