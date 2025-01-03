package com.fw.wcs.core.websocket;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ervin Chen
 * @date 2020/4/16 10:54
 */
public class WsRequest implements Serializable {
    private String messageType;
    private Map<String, Object> param;

    private WsRequest() {

    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public static WsRequest of(String request) {
        return JSON.parseObject(request, WsRequest.class);
    }

    public static WsRequest create() {
        return new WsRequest();
    }

    public WsRequest put(String key, Object value) {
        if (this.param == null) {
            this.param = new HashMap<>();
        }
        this.param.put(key, value);
        return this;
    }

    public Object get(String key) {
        if (this.param == null) {
            return null;
        }
        return this.param.get(key);
    }

    public Object get(String key, Object defaultValue) {
        if (this.param == null) {
            return null;
        }
        return this.param.getOrDefault(key, defaultValue);
    }
}
