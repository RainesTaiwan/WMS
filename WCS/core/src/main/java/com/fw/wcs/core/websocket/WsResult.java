package com.fw.wcs.core.websocket;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author Ervin Chen
 * @date 2020/4/16 9:41
 */
public class WsResult<T> implements Serializable {

    private String messageType;
    private int code;
    private T message;

    private WsResult(String messageType, int code, T message){
        this.messageType = messageType;
        this.code = code;
        this.message = message;
    }

    public static <T> WsResult<T> ok(String messageType, T message){
        return new WsResult<>(messageType, 0, message);
    }

    public static WsResult error(String messageType, String message){
        return new WsResult<>(messageType, 1, message);
    }

    public String toString(){
        return JSON.toJSONString(this);
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
