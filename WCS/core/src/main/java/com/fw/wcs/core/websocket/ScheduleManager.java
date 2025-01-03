package com.fw.wcs.core.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author Ervin Chen
 * @date 2020/4/15 14:25
 */
public class ScheduleManager {
    private static Logger logger = LoggerFactory.getLogger(ScheduleManager.class);
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    public static <T> RunnableScheduledFuture<?> schedule(Consumer<T> consumer, T t, long delay, TimeUnit unit) {
        return (RunnableScheduledFuture<?>) scheduledThreadPoolExecutor.schedule(() -> {
            try{
                consumer.accept(t);
            }catch (Exception e){
                logger.error(e.getLocalizedMessage());
            }
        }, delay, unit);
    }

    public static <T> RunnableScheduledFuture<?> scheduleWithFixedDelay(Consumer<T> consumer, T t, long initialDelay, long delay, TimeUnit unit) {
        return (RunnableScheduledFuture<?>) scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            try{
                consumer.accept(t);
            }catch (Exception e){
                logger.error(e.getLocalizedMessage());
            }
        }, initialDelay, delay, unit);
    }

    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return scheduledThreadPoolExecutor.schedule(command, delay, unit);
    }

    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return scheduledThreadPoolExecutor.schedule(callable, delay, unit);
    }

    public static boolean remove(RunnableScheduledFuture command) {
        scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
        command.cancel(false);
        return true;
    }
}
