package com.fw.wcs.sys.scheduler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class Scheduler {

    @Autowired
    private DashBoardService dashBoardService;
    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Scheduled(fixedRate = 10000)
    public void agvStatus() {
        List<Map<String, Object>> list = dashBoardService.getAgvData();
        if (list == null || list.size() <= 0) {
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message_type", "avg");
        jsonObject.put("agv_list", list);
        String message = JSON.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);

        activeMqSendService.sendMessage4Topic(CustomConstants.DASH_BOARD_DATA, message);
    }

    @Scheduled(fixedRate = 10000)
    public void receiveStationStatus() {
        List<Map<String, Object>> list = dashBoardService.getReceiveStationDate();
        if (list == null || list.size() <= 0) {
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message_type", "receiveStation");
        jsonObject.put("receiveS_list", list);
        String message = JSON.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);

        activeMqSendService.sendMessage4Topic(CustomConstants.DASH_BOARD_DATA, message);
    }
}
