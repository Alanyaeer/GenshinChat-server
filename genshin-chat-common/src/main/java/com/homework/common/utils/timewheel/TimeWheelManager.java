package com.homework.common.utils.timewheel;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
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
    public <T> void addTask(AtomicInteger retryCount, TaskExecutor taskExecutor, long delay, TimeUnit unit) {
        TimerTask timerTask = timeout -> {
            boolean isTaskContinue = taskExecutor.execute();
            if(isTaskContinue && retryCount.addAndGet(1) <= MAX_RETRY_TIMES){
                addTask(retryCount, taskExecutor, delayTimeStrategy.calculateNextDelayTime(delay), unit);
            }
            else if(isTaskContinue && retryCount.get() > MAX_RETRY_TIMES){
                log.error("任务执行次数超过最大次数，任务被终止");
            }
        };
        timer.newTimeout(timerTask, delay, unit);
    }

    public abstract static class TaskExecutor {
        protected boolean execute(){
            boolean needExecuteTask = isNeedExecuteTask();
            if(needExecuteTask){
                // 执行任务
                doExecuteTask();
                return isNeedSetNextTimerTask();
            }
            else{
                doIgnoreExecuteTask();
                return false;
            }
        }
        public abstract boolean isNeedExecuteTask();

        public abstract boolean isNeedSetNextTimerTask();

        public abstract void doExecuteTask();

        public void doIgnoreExecuteTask(){
            log.info("任务被忽略");
        }
    }
}