package com.fw.wcs.core.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.exception.BusinessException;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.*;
import java.io.IOException;

public abstract class WebSocketWrapper implements WebSocket.OnTextMessage, MessageListener {

    private Connection connection;
    private String channel;
    private javax.jms.Connection mqConnection = null;
    private String wsTopic = "MES_WS_EVENT";

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    JmsMessagingTemplate jmsTemplate;

    @Override
    public void onOpen(Connection connection) {
        this.connection = connection;

        try {
            if( connectionFactory != null ){
                mqConnection = connectionFactory.createConnection();
                mqConnection.start();
                Session session = mqConnection.createSession( false, Session.DUPS_OK_ACKNOWLEDGE );
                Destination destination = session.createTopic( wsTopic );
                MessageConsumer consumer = session.createConsumer( destination );
                consumer.setMessageListener( this );
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        doAfterOpen( connection );
    }

    @Override
    public void onClose(int i, String s) {
        try {
            if( mqConnection != null ){
                mqConnection.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        doAfterClose();
    }

    @Override
    public void onMessage(Message message) {
        if( message instanceof TextMessage ){
            try {
                sendMessage( ((TextMessage)message).getText() );
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage( String message ) {
        String messageType = null;
        try{

            JSONObject messageObj = JSON.parseObject( message );
            messageType = messageObj.getString( "MESSAGE_TYPE" );
            JSONObject requestParams = messageObj.getJSONObject( "PARAM" );

            if( StringUtils.isBlank( messageType ) ){
                throw BusinessException.build( "Message must contain MESSAGE_TYPE" );
            }

            onMessage( messageType, requestParams );

        } catch (Exception e) {
            sendError( messageType, e.getMessage() );
        }
    }

    public void sendMessage( String message ){
        try {
            this.connection.sendMessage( message );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage( String messageType, JSONObject messageBody ){
        try {
            JSONObject message = new JSONObject();
            message.put( "MESSAGE_TYPE", messageType );
            message.put( "CODE", 0 );
            message.put( "MESSAGE", messageBody );

            this.connection.sendMessage( message.toString() );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage( JSONObject message ){
        ActiveMQTopic topic = new ActiveMQTopic( wsTopic );
        jmsTemplate.convertAndSend( topic, message.toString() );
    }

    public void sendError( String messageType, String errorMessage ){
        try {
            JSONObject message = new JSONObject();
            message.put( "MESSAGE_TYPE", messageType );
            message.put( "CODE", 1 );
            message.put( "MESSAGE", errorMessage );

            this.connection.sendMessage( message.toString() );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public abstract void onMessage( String messageType, JSONObject messageBody  );

    public abstract void doAfterOpen( Connection connection );

    public abstract void doAfterClose();
}
