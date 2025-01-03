package com.fw.wcs.sys.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fw.wcs.core.utils.DateUtil;

/**
 * @author Leon
 *
 * MQ事件返回屬性實體類
 */
public class WmsResponse {

    public WmsResponse(String messageId) {
        this.MESSAGE_ID = messageId;
        this.RESULT = "0";
        this.MESSAGE = "成功！";
        this.SEND_TIME = DateUtil.getDateTime();
    }

    //事件請求唯一標識
    @JSONField(name="MESSAGE_ID")
    private String MESSAGE_ID;
    /**
     * 事件處理結果
     *
     * 0[OK] 1[NG]
     */
    @JSONField(name="RESULT")
    private String RESULT;
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

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
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
