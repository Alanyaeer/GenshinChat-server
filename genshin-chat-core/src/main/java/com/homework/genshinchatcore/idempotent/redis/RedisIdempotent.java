package com.homework.genshinchatcore.idempotent.redis;

import com.homework.common.entity.constants.RedisConstants;
import com.homework.genshinchatcore.idempotent.Idempotent;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
@Component
public class RedisIdempotent implements Idempotent {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取幂等锁
     * todo 后续需要优化为原子操作
     * @param id id
     * @return boolean
     */
    @Override
    public boolean acquireIdempotentLock(Object id) {
        String key = String.format(RedisConstants.HAS_SENT_MESSAGE_ID, id.toString());
        Object hasIssue = redisTemplate.opsForValue().get(key);
        if(hasIssue != null) {
            return false;
        }
        else{
            redisTemplate.opsForValue().set(key, 1, 1L, TimeUnit.MINUTES);
            return true;
        }
    }
}
