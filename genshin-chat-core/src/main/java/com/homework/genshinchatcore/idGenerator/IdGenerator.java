package com.homework.genshinchatcore.idGenerator;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/9
 */
public interface IdGenerator {
    /**
     * 获取雪花算法的id
     *
     * @return int
     */
    int nextShortId();

    /**
     * 获取雪花算法id
     *
     * @return long
     */
    long nextId();
}
