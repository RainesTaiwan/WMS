package com.fw.wcs.sys.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class CommonResponse {

    public CommonResponse(String correlationId, String messageType) {
        this.correlationId = correlationId;
        this.messageType = messageType;
    }

    //事件請求唯一標識
    @JSONField(name="CORRELATION_ID")
    private String correlationId;
    //事件名稱
    @JSONField(name="MESSAGE_TYPE")
    private String messageType;
    /**
     * 事件處理結果
     *
     * 0[OK] 1[NG]
     */
    @JSONField(name="ACK_CODE")
    private String ackCode;
    //事件發送時間
    @JSONField(name="SEND_TIME")
    private String sendTime;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getMESSAGE_TYPE() {
        return messageType;
    }

    public void setMESSAGE_TYPE(String messageType) {
        this.messageType = messageType;
    }

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "{" +
                "CORRELATION_ID='" + correlationId + '\'' +
                ", MESSAGE_TYPE='" + messageType + '\'' +
                ", ACK_CODE='" + ackCode + '\'' +
                ", SEND_TIME='" + sendTime + '\'' +
                '}';
    }
}
