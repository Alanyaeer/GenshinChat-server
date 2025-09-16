package com.homework.chatserver.utils.idGenerator.snowflake;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.homework.chatserver.utils.idGenerator.IdGenerator;
import org.springframework.stereotype.Component;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/9
 */
@Component
public class SnowFlakeIdGenerator implements IdGenerator {

    private final SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();


    private SnowFlakeIdGenerator() {}

    /**
     * 截断会导致冲突大幅提升，慎用
     *
     * @return {@link Integer }
     */
    @Override
    public int nextShortId() {
        Long extractNum = snowflakeGenerator.next() % Integer.MAX_VALUE;
        return extractNum.intValue();
    }

    @Override
    public long nextId() {
        return snowflakeGenerator.next();
    }
}
