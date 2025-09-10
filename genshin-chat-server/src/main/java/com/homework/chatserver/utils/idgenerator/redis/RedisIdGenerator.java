package com.homework.chatserver.utils.idgenerator.redis;

import com.homework.chatserver.utils.idgenerator.IdGenerator;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.homework.common.entity.constants.RedisConstants.ID_GENERATOR_KEY;
import static com.homework.common.entity.constants.RedisConstants.ID_GENERATOR_LONG_KEY;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/9
 */
@Component
public class RedisIdGenerator implements IdGenerator{
    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Override
    public int nextShortId() {
        return redisTemplate.opsForValue().increment(ID_GENERATOR_KEY).intValue();
    }

    @Override
    public long nextId() {
        return redisTemplate.opsForValue().increment(ID_GENERATOR_LONG_KEY);
    }
}
