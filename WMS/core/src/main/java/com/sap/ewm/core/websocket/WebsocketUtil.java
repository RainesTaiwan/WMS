package com.sap.ewm.core.websocket;

import com.sap.ewm.core.utils.ActiveMQUtil;
import com.sap.ewm.core.utils.SpringUtil;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsMessagingTemplate;

public class WebsocketUtil {

    public static void publishMessage(String channel, WsResult wsResult) {
        publishMessage(channel, wsResult.toString());
    }

    public static void publishMessage(String channel, String message) {
        long consumerCount = ActiveMQUtil.getTopicConsumerCount(channel);
        if (consumerCount > 0) {
            JmsMessagingTemplate jmsTemplate = SpringUtil.getBean(JmsMessagingTemplate.class);
            ActiveMQTopic topic = new ActiveMQTopic(channel);
            jmsTemplate.convertAndSend(topic, message);
        }
    }
}
