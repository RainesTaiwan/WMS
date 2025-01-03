package com.fw.wcs.sys.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class VmsResponse {

    public VmsResponse(String correlationId, String messageType) {
        this.CORRELATION_ID = correlationId;
        this.MESSAGE_TYPE = messageType;
    }

    //事件請求唯一標識
    @JSONField(name="CORRELATION_ID")
    private String CORRELATION_ID;
    //事件名稱
    @JSONField(name="MESSAGE_TYPE")
    private String MESSAGE_TYPE;
    /**
     * 事件處理結果
     *
     * 0[OK] 1[NG]
     */
    @JSONField(name="ACK_CODE")
    private String ACK_CODE;
    //事件發送時間
    @JSONField(name="SEND_TIME")
    private String SEND_TIME;

    public String getCorrelationId() {
        return CORRELATION_ID;
    }

    public void setCorrelationId(String correlationId) {
        CORRELATION_ID = correlationId;
    }

    public String getMESSAGE_TYPE() {
        return MESSAGE_TYPE;
    }

    public void setMESSAGE_TYPE(String messageType) {
        this.MESSAGE_TYPE = messageType;
    }

    public String getAckCode() {
        return ACK_CODE;
    }

    public void setAckCode(String ackCode) {
        ACK_CODE = ackCode;
    }

    public String getSendTime() {
        return SEND_TIME;
    }

    public void setSendTime(String SendTime) {
        SEND_TIME = SendTime;
    }

    @Override
    public String toString() {
        return "VmsResponse{" +
                "CORRELATION_ID='" + CORRELATION_ID + '\'' +
                ", MESSAGE_TYPE='" + MESSAGE_TYPE + '\'' +
                ", ACK_CODE='" + ACK_CODE + '\'' +
                ", SEND_TIME='" + SEND_TIME + '\'' +
                '}';
    }
}
