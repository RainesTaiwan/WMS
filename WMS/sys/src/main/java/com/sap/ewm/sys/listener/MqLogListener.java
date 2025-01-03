package com.sap.ewm.sys.listener;

import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.sys.model.MqLog;
import com.sap.ewm.sys.service.MqLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * MQ監聽日誌記錄
 *
 * @author syngna
 * @date 2020/3/19 14:33
 */
@Component
public class MqLogListener {

    @Autowired
    MqLogService mqLogService;

    @JmsListener(destination = "MQ_LOG")
    public void saveMqLog(String mqLogJson) {
        MqLog mqLog = null;
        try {
            mqLog = JSONObject.parseObject(mqLogJson, MqLog.class);
            mqLogService.save(mqLog);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
