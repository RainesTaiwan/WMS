package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.consumer.WmsConsumer;
import com.fw.wcs.sys.service.ActiveMqSendService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component
public class ActiveMqSendServiceImpl implements ActiveMqSendService {

    private static Logger logger = LoggerFactory.getLogger(WmsConsumer.class);

    @Autowired
    @Qualifier("wmsActivemqTemplate")
    private JmsMessagingTemplate wmsActivemqTemplate;

    @Override
    public void sendMessage4Topic(String queueName, String text) {
        logger.info("發送 Topic 訊息體:{}", text);
        Destination destination = new ActiveMQTopic(queueName);
        wmsActivemqTemplate.convertAndSend(destination, text);
    }

    @Override
    public void sendMsgNoResponse4Res(String queueName, String message) {
        logger.info("發送 RES 無需回覆 >>> 訊息體:{}", message);
        Destination destination = new ActiveMQQueue(queueName);
        wmsActivemqTemplate.convertAndSend(destination, message);
    }

    @Override
    public String sendMsgNeedResponse4Res(String queueName, String message) {
        logger.info("發送 RES 需要回復 >>> 訊息體:{}", message);
        String response = wmsActivemqTemplate.convertSendAndReceive(queueName, message, String.class);
        return response;
    }

    @Override
    public void sendMsgNoResponse4Wms(String queueName, String message) {
        logger.info("發送 WMS 無需回覆 >>> 訊息體:{}", message);
        Destination destination = new ActiveMQQueue(queueName);
        wmsActivemqTemplate.convertAndSend(destination, message);
    }

    @Override
    public String sendMsgNeedResponse4Wms(String queueName, String message) {
        logger.info("發送 WMS 需要回復 >>> 訊息體:{}", message);
        String response = wmsActivemqTemplate.convertSendAndReceive(queueName, message, String.class);
        return response;
    }

    //發送遠端指令給裝置-停機
    @Override
    public void sendRemoteStopCommand(String resource, String station, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        jsonObject.put("MESSAGE_TYPE", CustomConstants.REMOTE_COMMAND);
        jsonObject.put("RESOURCE", resource);
        jsonObject.put("STATION", station);
        jsonObject.put("COMMAND", "STOP");
        jsonObject.put("MESSAGE", message);
        jsonObject.put("SEND_TIME", DateUtil.getDateTime());

        this.sendMsgNoResponse4Res(CustomConstants.REMOTE_COMMAND, jsonObject.toJSONString());
    }
}
