package com.sap.ewm.console.controller;

import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.core.base.AjaxResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ervin Chen
 * @date 2020/6/16 9:12
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("hello")
    public AjaxResult hello(){
        JSONObject obj = new JSONObject();
        obj.put("name", "ssss");
        return AjaxResult.success(obj);
    }
}
