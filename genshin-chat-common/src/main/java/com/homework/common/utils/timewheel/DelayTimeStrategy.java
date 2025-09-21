package com.homework.common.utils.timewheel;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/21
 */
@FunctionalInterface
public interface DelayTimeStrategy {
    long calculateNextDelayTime(long delayTime);
}
