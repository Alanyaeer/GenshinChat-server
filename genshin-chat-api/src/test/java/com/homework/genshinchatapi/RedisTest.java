package com.homework.genshinchatapi;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class RedisTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    void testRedis() {
        redisTemplate.opsForValue().set("hello",3232);
        log.info("redisTemplate.opsForValue().set(\"hello\",\"world\");");
        log.info("redisTemplate.opsForValue().get(\"hello\").toString() = {}",redisTemplate.opsForValue().get("hello").toString());

    }
}
