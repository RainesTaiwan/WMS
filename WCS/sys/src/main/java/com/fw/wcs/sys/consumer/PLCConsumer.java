package com.fw.wcs.sys.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.model.PLCData;
import com.fw.wcs.sys.service.PLCDataService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

/**
 * @author Glanz
 * 監聽 PLC
 */
@Component
public class PLCConsumer {
    private static Logger logger = LoggerFactory.getLogger(PLCConsumer.class);

    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Autowired
    private PLCDataService plcDataService;

    @JmsListener(destination = "plc.reader.sync.process", containerFactory = "wmsFactory")
    public void plcInfoProcess(MessageHeaders headers, String text) {
        List<String> dataList = new ArrayList<>();
        try {
            JSONObject obj = JSON.parseObject(text);
            String reqId = obj.getString("REQ_ID");
            String mxNum = obj.getString("MX_NUM");
            String area = obj.getString("AREA");
            String point = obj.getString("POINT");
            String count = obj.getString("COUNT");
            String resource = obj.getString("RESOURCE");
            JSONArray data = obj.getJSONArray("DATA");
            //資料只讀取D000~D3099的資料 共100個點位
            for (int i = 0; i < data.size(); i++){
                dataList.add(data.getString(i));
            }
            String sendTime = obj.getString("SEND_TIME");

            Date date = new Date();
            PLCData plcdata = new PLCData();
            plcdata.setHandle("PLC_"+DateUtil.getDateTimeWithRandomNum());
            plcdata.setPLCId("Conveyor"+mxNum);
            plcdata.setData(dataList.toString());
            plcdata.setCreateUser(CustomConstants.CREATE_USER);
            plcdata.setCreatedTime(date);
            plcDataService.insertPLCData(plcdata);

            plcDataService.readPLCData(mxNum, dataList);

        } catch (Exception e) {
            logger.error("PLC Information Parser failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "PLC Information Parser failed -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }
}
