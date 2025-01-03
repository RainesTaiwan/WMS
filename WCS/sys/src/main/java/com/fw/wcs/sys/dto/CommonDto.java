package com.fw.wcs.sys.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class CommonDto {

    //事件請求唯一標識
    @JSONField(name="MESSAGE_ID")
    private String MESSAGE_ID;
    //事件名稱
    @JSONField(name="MESSAGE_TYPE")
    private String MESSAGE_TYPE;
    //返回資訊
    @JSONField(name="MESSAGE")
    private String MESSAGE;
    //發送時間
    @JSONField(name="SEND_TIME")
    private String SEND_TIME;

    public String getMESSAGE_ID() {
        return MESSAGE_ID;
    }

    public void setMESSAGE_ID(String MESSAGE_ID) {
        this.MESSAGE_ID = MESSAGE_ID;
    }

    public String getMESSAGE_TYPE() {
        return MESSAGE_TYPE;
    }

    public void setMESSAGE_TYPE(String MESSAGE_TYPE) {
        this.MESSAGE_TYPE = MESSAGE_TYPE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getSEND_TIME() {
        return SEND_TIME;
    }

    public void setSEND_TIME(String SEND_TIME) {
        this.SEND_TIME = SEND_TIME;
    }
}
