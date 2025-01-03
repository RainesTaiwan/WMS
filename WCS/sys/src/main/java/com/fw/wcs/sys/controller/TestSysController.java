package com.fw.wcs.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.base.AjaxResult;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.service.ActiveMqSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Leon
 *
 * 前臺互動測試類
 */
@RestController
@RequestMapping("/test")
public class TestSysController {

    @Autowired
    private ActiveMqSendService activeMqSendService;

    @GetMapping()
    public AjaxResult test01() {

        try {
            String queueName = "carrier.outbound.notice.process";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MESSAGE_ID", DateUtil.getDateTime());
            jsonObject.put("MESSAGE_TYPE", "carrier.outbound.notice.process");
            jsonObject.put("CARRIER", "CAR001");
            activeMqSendService.sendMsgNoResponse4Wms(queueName, jsonObject.toJSONString());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }

        return AjaxResult.success();
    }
}
