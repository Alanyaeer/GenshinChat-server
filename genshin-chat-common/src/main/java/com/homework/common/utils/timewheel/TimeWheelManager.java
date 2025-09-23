package com.homework.common.utils.timewheel;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TimeWheelManager {
    private final Timer timer;
    // 后续替换
    private final DelayTimeStrategy delayTimeStrategy = (delayTime -> delayTime * 2);

    private static final int MAX_RETRY_TIMES = 3;

    private static class SingletonHolder {
        private static final TimeWheelManager INSTANCE = new TimeWheelManager();
    }

    public static TimeWheelManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private TimeWheelManager() {
        this.timer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 512);
    }

    /**
     * 添加延迟任务
     *
     * @param taskExecutor   任务逻辑
     * @param delay  延迟时间
     * @param unit   时间单位
     */
    public void addTask(TaskExecutor taskExecutor, long delay, TimeUnit unit) {
        AtomicInteger retryCount = new AtomicInteger();
        executeTaskRecursive(retryCount, taskExecutor, delay, unit);
    }

    /**
     * 执行任务递归，exponential 增长退避时长，直到超过3次
     *
     * @param retryCount   重试次数
     * @param taskExecutor 任务执行者
     * @param delay        延迟
     * @param unit         单元
     */
    private void executeTaskRecursive(AtomicInteger retryCount, TaskExecutor taskExecutor, long delay, TimeUnit unit){
        TimerTask timerTask = timeout -> {
            taskExecutor.execute();
            if(taskExecutor.shouldScheduleNext() && retryCount.addAndGet(1) <= MAX_RETRY_TIMES){
                executeTaskRecursive(retryCount, taskExecutor, delayTimeStrategy.calculateNextDelayTime(delay), unit);
            }
            else if(taskExecutor.shouldScheduleNext() && retryCount.get() > MAX_RETRY_TIMES){
                taskExecutor.onMaxRetriesExceeded();
            }
        };
        timer.newTimeout(timerTask, delay, unit);
    }

    public abstract static class TaskExecutor {
        protected void execute(){
            boolean needExecuteTask = shouldExecuteTask();
            if(needExecuteTask){
                // 执行任务
                doExecuteTask();
            }
            else{
                onIgnored();
            }
        }
        public abstract boolean shouldExecuteTask();

        public abstract boolean shouldScheduleNext();

        public abstract void doExecuteTask();

        public void onIgnored(){
            log.info("任务被忽略");
        }
        /**
         * 任务达到最大重试次数时的处理
         */
        public void onMaxRetriesExceeded() {
            log.error("任务执行次数超过最大限制，任务终止");
        }
    }
}