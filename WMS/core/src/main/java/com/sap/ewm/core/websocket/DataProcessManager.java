package com.sap.ewm.core.websocket;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sap.ewm.core.utils.ActiveMQUtil;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Ervin Chen
 * @date 2020/4/17 13:48
 */
public class DataProcessManager {

    public static volatile Multimap<String, RunnableScheduledFuture> taskList = HashMultimap.create();

    public static void addDataProcessor(String channel, long delay, WsRequest wsRequest, Consumer<WsRequest>... consumers) {
        long count = ActiveMQUtil.getTopicConsumerCount(channel);
        System.out.println("consumer quantity---->" + count);
        if (count == 1) {
            System.out.println("add task");
            Stream.of(consumers).forEach(consumer -> taskList.put(channel, ScheduleManager.scheduleWithFixedDelay(consumer, wsRequest, 0, delay, TimeUnit.SECONDS)));
        }
    }

    public static void removeDataProcessor(String channel){
        long count = ActiveMQUtil.getTopicConsumerCount(channel);
        System.out.println("consumer quantity---->" + count);
        if (count == 0) {
            System.out.println("remove task");
            taskList.get(channel).forEach(command -> ScheduleManager.remove(command));
            taskList.removeAll(channel);
        }
    }
}
