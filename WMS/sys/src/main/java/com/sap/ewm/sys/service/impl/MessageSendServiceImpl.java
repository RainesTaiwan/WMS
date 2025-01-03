package com.sap.ewm.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.core.utils.IPUtil;
import com.sap.ewm.sys.model.MqLog;
import com.sap.ewm.sys.service.MessageSendService;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import java.time.LocalDateTime;
import java.util.UUID;
import com.sap.ewm.core.utils.DateUtil;

/**
 * @author syngna
 * @date 2020/3/27 11:06
 */
@Service
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    JmsMessagingTemplate jmsTemplate;

    @Value("${spring.activemq.brokerUrl}")
    String brokerURL;

    @Override
    public void send(String queue, JSONObject message) {
        jmsTemplate.convertAndSend(queue, message.toString());
        saveLog(queue, message, null);
    }

    @Override
    public void sendMessage4Topic(String queueName, JSONObject message) {
        Destination destination = new ActiveMQTopic(queueName);
        jmsTemplate.convertAndSend(destination, message.toString());
    }

    @Override
    public JSONObject sendAndReceive(String queue, JSONObject message) {
        String replyMessage = jmsTemplate.convertSendAndReceive(queue, message.toString(), String.class);
        JSONObject replyJson = JSON.parseObject(replyMessage);
        saveLog(queue, message, replyJson);
        return replyJson;
    }

    private void saveLog(String queue, JSONObject request, JSONObject response) {
        String messageBody = request.toString();
        String responseBody = response == null ? null : response.toString();
        String ip = IPUtil.getIp();
        String site = request.getString("SITE");
        String result = null;
        String errorMsg = null;
        if (response == null) {
            result = "Y";
        } else if ("1".equals(response.getString("RESULT"))) {
            result = "Y";
        } else {
            result = "N";
            errorMsg = response.getString("MESSAGE");
        }

        MqLog eapLog = new MqLog();
        eapLog.setHandle(DateUtil.getDateTimeWithRandomNum()); //UUID.randomUUID().toString()
        eapLog.setBroker(brokerURL);
        eapLog.setQueue(queue);
        eapLog.setmessageId(request.getString("MESSAGE_ID"));
        eapLog.setCorrelationId(request.getString("CORRELATION_ID"));
        eapLog.setMessageBody(messageBody != null && messageBody.length() > 2000 ? messageBody.substring(0, 2000) : messageBody);
        eapLog.setResult(result);
        eapLog.setResponseBody(responseBody != null && responseBody.length() > 2000 ? responseBody.substring(0, 2000) : responseBody);
        eapLog.setError(errorMsg != null && errorMsg.length() > 2000 ? errorMsg.substring(0, 2000) : errorMsg);
        eapLog.setServerId(ip);
        eapLog.setMessageType("OUT");
        eapLog.setCreatedDateTime(LocalDateTime.now());
        jmsTemplate.convertAndSend("MQ_LOG", JSONObject.toJSONString(eapLog));
    }
}
