package com.sap.ewm.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sap.ewm.core.websocket.WebsocketUtil;
import com.sap.ewm.core.websocket.WsResult;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MqTest {

    private String USER = ActiveMQConnection.DEFAULT_USER;
    private String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //private String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private String URL = "tcp://localhost:61616";
    //private String SUBJECT = "lot.info.nc.record";

    private Destination destination = null;
    private Connection conn = null;
    private Session session = null;
    private MessageProducer producer = null;

    // 初始化
    private void initialize(String subject) throws JMSException, Exception {
        // 連線工廠
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
        conn = connectionFactory.createConnection();
        // 事務性會話，自動確認訊息
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 訊息的目的地（Queue/Topic）
        destination = session.createQueue(subject);
        // destination = session.createTopic(SUBJECT);
        // 訊息的提供者（生產者）
        producer = session.createProducer(destination);
        // 不持久化訊息
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }


    @Test
    public void sendHandlingUnitInfoTest() throws JMSException, Exception {

        initialize("handling.unit.info.process");
        // 連線到JMS提供者（伺服器）
        conn.start();
        // 發送文字訊息
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("HandlingUnitInfo.json")));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        TextMessage msg = session.createTextMessage();

        TemporaryQueue tempQueue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(tempQueue);

        msg.setJMSReplyTo(tempQueue);
        // TextMessage msg = session.createTextMessage(textMsg);
        msg.setText(sb.toString());
        producer.send(msg);

         TextMessage responseMessage = (TextMessage) messageConsumer.receive(60000);
        close();
        System.out.println(responseMessage.getText());
    }

    @Test
    public void sendHandlingUnitInRequestTest() throws JMSException, Exception {

        initialize("handling.unit.in.request");
        // 連線到JMS提供者（伺服器）
        conn.start();
        // 發送文字訊息
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("HandlingUnitInfo.json")));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        TextMessage msg = session.createTextMessage();

        TemporaryQueue tempQueue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(tempQueue);

        msg.setJMSReplyTo(tempQueue);
         //TextMessage msg = session.createTextMessage(textMsg);
        msg.setText(sb.toString());
        producer.send(msg);

         TextMessage responseMessage = (TextMessage) messageConsumer.receive(60000);
        close();
        System.out.println(responseMessage.getText());
    }

    @Test
    public void sendInStorageTest() throws JMSException, Exception {

        initialize("handling.unit.in.storage");
        // 連線到JMS提供者（伺服器）
        conn.start();
        // 發送文字訊息
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("InStorage.json")));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        TextMessage msg = session.createTextMessage();

        TemporaryQueue tempQueue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(tempQueue);

        msg.setJMSReplyTo(tempQueue);
        // TextMessage msg = session.createTextMessage(textMsg);
        msg.setText(sb.toString());
        producer.send(msg);

         TextMessage responseMessage = (TextMessage) messageConsumer.receive(60000);
        close();
        System.out.println(responseMessage.getText());
    }

    @Test
    public void sendOutBoundMessageTest() throws Exception{

        initialize("carrier.outbound.notice.process");
        // 連線到JMS提供者（伺服器）
        conn.start();
        // 發送文字訊息
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("InStorage.json")));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        TextMessage msg = session.createTextMessage();

        TemporaryQueue tempQueue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(tempQueue);

        msg.setJMSReplyTo(tempQueue);
        // TextMessage msg = session.createTextMessage(textMsg);
        msg.setText(sb.toString());
        producer.send(msg);

        // TextMessage responseMessage = (TextMessage) messageConsumer.receive(60000);
        close();
        //System.out.println(responseMessage.getText());
    }

    @Test
    public void sendHandlingUnitOutTest() throws Exception{

        initialize("handling.unit.out.storage");
        // 連線到JMS提供者（伺服器）
        conn.start();
        // 發送文字訊息
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("HandlingUnitOut.json")));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        TextMessage msg = session.createTextMessage();

        TemporaryQueue tempQueue = session.createTemporaryQueue();
        MessageConsumer messageConsumer = session.createConsumer(tempQueue);

        msg.setJMSReplyTo(tempQueue);
        // TextMessage msg = session.createTextMessage(textMsg);
        msg.setText(sb.toString());
        producer.send(msg);

        // TextMessage responseMessage = (TextMessage) messageConsumer.receive(60000);
        close();
        //System.out.println(responseMessage.getText());
    }

    @Test
    public void sendHandlingUnitOutStationTest() throws Exception{

        initialize("handling.unit.out.station");
        // 連線到JMS提供者（伺服器）
        conn.start();
        // 發送文字訊息
        BufferedReader in = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("HandlingUnitOurStation.json")));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        TextMessage msg = session.createTextMessage();

        TemporaryQueue tempQueue = session.createTemporaryQueue();
        msg.setText(sb.toString());
        producer.send(msg);

        close();
    }

    @Test
    public void testString(){
        System.out.println(String.join("@", "1","3","5"));
        System.out.println(String.join("_","sss"));
    }

    @Test
    public void testWebSocket() {
        WebsocketUtil.publishMessage("dayInfra", WsResult.ok("inventory_inbound", new ArrayList(){{
            add("aaa");
            add("bbb");
        }}));
    }

    // 關閉連線
    public void close() throws JMSException {
        if (producer != null)
            producer.close();
        if (session != null)
            session.close();
        if (conn != null)
            conn.close();
    }
}
