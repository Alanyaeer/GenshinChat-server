package com.homework.common.utils.hash;

import cn.hutool.core.date.StopWatch;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.concurrent.TimeUnit;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/10
 */
public class MurMurHash {
    /**
     * 散列函数
     */
    private static final HashFunction hashFunction = Hashing.goodFastHash(32);

    /**
     * 将long类型的变量映射到int类型
     *
     * @param key 钥匙
     * @return int
     */
    public static int hashLong(long key) {
        return hashFunction.hashLong(key).asInt();
    }

}
