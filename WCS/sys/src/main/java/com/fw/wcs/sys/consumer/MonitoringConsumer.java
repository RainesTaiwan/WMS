package com.fw.wcs.sys.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.dto.CommonResponse;
import com.fw.wcs.sys.model.MonitoringTask;
import com.fw.wcs.sys.service.MonitoringTaskService;
import com.fw.wcs.sys.service.ActiveMqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import com.fw.wcs.core.utils.DateUtil;
import java.time.LocalDateTime;

import java.util.Date;

/**
 * @author Glanz
 * 監聽 監控系統-Monitoring 互動事件
 */
@Component
public class MonitoringConsumer {
    private static Logger logger = LoggerFactory.getLogger(MonitoringConsumer.class);

    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private MonitoringTaskService monitoringTaskService;

    @JmsListener(destination = "Surveillance.Request.Ack", containerFactory = "wmsFactory")
    public void monitoringRequestAck(MessageHeaders headers, String text) {
        logger.info("Get MonitoringRequestAck Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.MonitoringRequestAck);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        String correlationId = "";
        String ackCode = "";
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            correlationId = jsonObject.getString("CORRELATION_ID");
            ackCode = jsonObject.getString("ACK_CODE");
            String sendTime = jsonObject.getString("SEND_TIME");

            // 更新監控任務
            MonitoringTask monitoringTask = monitoringTaskService.findMonitoringTaskByID( correlationId );
            if(monitoringTask != null){
                monitoringTask.setStatus(CustomConstants.END);
                monitoringTask.setUpdateUser(CustomConstants.UPDATE_USER);
                monitoringTask.setUpdatedTime(new Date());//new Date()  LocalDateTime.now()
                monitoringTaskService.updateMonitoringTaskByID(monitoringTask);
            }
        } catch (Exception e) {
            logger.error("MonitoringRequestAck failed: {}", e.getMessage());
        }
    }

    //模擬Monitoring
    @JmsListener(destination = "Surveillance.Request", containerFactory = "wmsFactory")
    public void monitoringRequest(MessageHeaders headers, String text) {
        logger.info("Get MonitoringRequest Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.MonitoringRequest);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        String messageId = "";
        String ackCode = "0";
        String reason = "";
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            messageId = jsonObject.getString("MESSAGE_ID");
            String resource = jsonObject.getString("RESOURCE");
            String storageBin = jsonObject.getString("STORAGE_BIN");
            String type = jsonObject.getString("TYPE");
            String sendTime = jsonObject.getString("SEND_TIME");
        } catch (Exception e) {
            ackCode = "1";
            reason = e.getMessage();
        }
        //訊息回覆
        CommonResponse response = new CommonResponse(messageId, CustomConstants.MonitoringRequestAck);
        response.setAckCode(ackCode);
        response.setSendTime(DateUtil.getDateGMT8Time()); //LocalDateTime.now().toString()
        logger.info("Return MonitoringRequest Text>>> {}", response.toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MonitoringRequestAck, JSON.toJSONString(response));
    }
}
