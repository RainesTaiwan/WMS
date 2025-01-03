package com.sap.ewm.dashboard.consumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.core.websocket.WebsocketUtil;
import com.sap.ewm.core.websocket.WsResult;
import com.sap.ewm.dashboard.model.CarrierStatus;
import com.sap.ewm.dashboard.service.CarrierStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class DashboardComsumer {

    private final Logger logger = LoggerFactory.getLogger(DashboardComsumer.class);

    @Autowired
    JmsMessagingTemplate jmsTemplate;

    @Autowired
    CarrierStatusService carrierStatusService;

    /**
     * 監聽載具狀態變更資訊
     *
     * @param text
     */
    @JmsListener(destination = "carrier.status.change", concurrency = "5-10")
    public void handlingUnitStatusChangeProcess(String text, Message msg, MessageHeaders headers) {
        logger.info("=======Message queue [handling.unit.status.change]，received: {}", text);
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        resultJSONObject.put("MESSAGE", "OK");
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageId = obj.getString("MESSAGE_ID");
            String carrier = obj.getString("CARRIER");
            String item = obj.getString("ITEM");
            String operation = obj.getString("OPERATION");
            String status = obj.getString("STATUS");
            resultJSONObject.put("MESSAGE_ID", messageId);
            if(StrUtil.isBlank(status) || StrUtil.isBlank(operation)) {
                carrierStatusService.removeById(carrier);
            } else {
                carrierStatusService.saveOrUpdate(new CarrierStatus(carrier, item, operation, status));
            }
            WebsocketUtil.publishMessage("dayInfra", WsResult.ok("carrier_status_change", carrierStatusService.list()));
        } catch (Exception e) {
            logger.error("Error:", e);
            resultJSONObject.put("RESULT", "2");
            resultJSONObject.put("MESSAGE", StrUtil.isBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
        if (headers.get("jms_replyTo") != null) {
            jmsTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), resultJSONObject.toString());
        }
    }
}
