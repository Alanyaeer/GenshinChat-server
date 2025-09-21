package com.homework.genshinchatcore.idempotent;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
public interface Idempotent {
    /**
     * 是否幂等
     *
     * @param id id
     * @return boolean
     */
    boolean acquireIdempotentLock(Object id);
}
