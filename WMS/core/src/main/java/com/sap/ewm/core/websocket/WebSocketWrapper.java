package com.sap.ewm.core.websocket;

import org.apache.activemq.command.ActiveMQTopic;
import org.eclipse.jetty.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.*;
import java.io.IOException;

public abstract class WebSocketWrapper implements WebSocket.OnTextMessage, MessageListener {

    private Connection connection;
    protected String channel;
    private javax.jms.Connection mqConnection = null;

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    JmsMessagingTemplate jmsTemplate;

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;
        this.init();
        try {
            if (connectionFactory != null) {
                mqConnection = connectionFactory.createConnection();
                mqConnection.start();
                Session session = mqConnection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
                Destination topic = session.createTopic(this.channel);
                session.createConsumer(topic).setMessageListener(this);
                Destination queue = session.createQueue(this.channel);
                session.createConsumer(queue).setMessageListener(this);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        doAfterOpen(connection);
    }

    @Override
    public void onClose(int i, String s) {
        try {
            if (mqConnection != null) {
                mqConnection.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        doAfterClose();
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                Destination destination = message.getJMSDestination();
                if (destination instanceof Topic) {
                    sendMessage(((TextMessage) message).getText());
                } else {
                    onMessage(((TextMessage) message).getText());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(String message) {
        WsRequest wsRequest = null;
        try {
            //心跳檢查
            if ("ping".equals(message)) {
                sendMessage("pong");
                return;
            }
            wsRequest = WsRequest.of(message);
            onMessage(wsRequest);
        } catch (Exception e) {
            sendMessage(WsResult.error(wsRequest.getMessageType(), e.getMessage()));
        }
    }

    public void sendMessage(String message) {
        try {
            this.connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(WsResult wsResult) {
        try {
            this.connection.sendMessage(wsResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void publishMessage(WsResult wsResult) {
        publishMessage(wsResult.toString());
    }

    public final void publishMessage(String message) {
        ActiveMQTopic topic = new ActiveMQTopic(this.channel);
        jmsTemplate.convertAndSend(topic, message);
    }

    public void init() {
    }

    public abstract void onMessage(WsRequest request);

    public abstract void doAfterOpen(Connection connection);

    public abstract void doAfterClose();
}
