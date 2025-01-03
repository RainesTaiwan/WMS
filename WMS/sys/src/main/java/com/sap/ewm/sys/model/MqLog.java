package com.sap.ewm.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * MQ日誌
 * </p>
 *
 * @author syngna
 * @since 2020-07-14
 */
public class MqLog extends Model<MqLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.UUID)
    private String handle;
    /**
     * IP地址
     */
    @TableField("SERVER_ID")
    private String serverId;
    /**
     * MQ地址
     */
    @TableField("BROKER")
    private String broker;
    /**
     * 佇列名稱
     */
    @TableField("QUEUE")
    private String queue;
    /**
     * 請求ID
     */
    @TableField("MESSAGE_ID")
    private String messageId;
    /**
     * 關聯id，如果請求訊息及返回為非同步時，使用該ID進行關聯
     */
    @TableField("CORRELATION_ID")
    private String correlationId;
    /**
     * 訊息內容
     */
    @TableField("MESSAGE_BODY")
    private String messageBody;
    /**
     * 結果
     */
    @TableField("RESULT")
    private String result;
    /**
     * 返回訊息內容
     */
    @TableField("RESPONSE_BODY")
    private String responseBody;
    /**
     * 錯誤資訊
     */
    @TableField("ERROR")
    private String error;
    /**
     * 訊息型別
     */
    @TableField("MESSAGE_TYPE")
    private String messageType;
    /**
     * 建立日期
     */
    @TableField("CREATED_DATE_TIME")
    private LocalDateTime createdDateTime;


    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getmessageId() {
        return messageId;
    }

    public void setmessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }
}