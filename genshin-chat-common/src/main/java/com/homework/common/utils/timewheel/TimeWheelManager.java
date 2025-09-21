package com.homework.common.utils.timewheel;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimeWheelManager {
    private final Timer timer;
    private final ConcurrentHashMap<Object, TaskContextTimeout<?>> taskMap;
    // 后续替换
    private final DelayTimeStrategy delayTimeStrategy = (delayTime -> delayTime * 2);

    private static class SingletonHolder {
        private static final TimeWheelManager INSTANCE = new TimeWheelManager();
    }

    public static TimeWheelManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private TimeWheelManager() {
        this.timer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 512);
        this.taskMap = new ConcurrentHashMap<>();
    }

    /**
     * 添加延迟任务
     *
     * @param taskId 任务ID
     * @param taskExecutor   任务逻辑
     * @param delay  延迟时间
     * @param unit   时间单位
     */
    public <T> void addTask(Object taskId, T context, TaskExecutor taskExecutor, long delay, TimeUnit unit) {
        TimerTask timerTask = timeout -> {
            try {
                boolean isTaskContinue = taskExecutor.execute();
                if(isTaskContinue){
                    addTask(taskId, context, taskExecutor, delayTimeStrategy.calculateNextDelayTime(delay), unit);
                }
            } finally {
                taskMap.remove(taskId);
            }
        };

        Timeout timeout = timer.newTimeout(timerTask, delay, unit);
        TaskContextTimeout<T> taskContextTimeout = new TaskContextTimeout<>(timeout, context);
        taskMap.put(taskId, taskContextTimeout);
    }

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     */
    public boolean cancelTask(Object taskId) {
        TaskContextTimeout<?> taskContextTimeout = taskMap.remove(taskId);
        Timeout timeout = taskContextTimeout.getTimeout();
        if (timeout != null) {
            timeout.cancel();
            return true;
        }
        return false;
    }

    public void removeTask(Object taskId){
        taskMap.remove(taskId);
    }


    /**
     * 停止时间轮
     */
    public void stop() {
        timer.stop();
        taskMap.clear();
    }

    /**
     * 检查任务是否存在
     *
     * @param taskId 任务ID
     */
    public boolean containsTask(String taskId) {
        return taskMap.containsKey(taskId);
    }

    public class TaskContextTimeout<T> {
        @Getter
        private final Timeout timeout;
        @Getter
        private final T context;
        public TaskContextTimeout(Timeout timeout, T context) {
            this.timeout = timeout;
            this.context = context;
        }

        public TaskContextTimeout(Timeout timeout, T context, DelayTimeStrategy delayTimeStrategy) {
            this.timeout = timeout;
            this.context = context;
        }
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