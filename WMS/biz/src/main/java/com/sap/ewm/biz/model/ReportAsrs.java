package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;


/**
 * 回報ASRS訂單結果要求主數據
 *
 * @author Glanz
 * @since 2021-08-12
 */
public class ReportAsrs extends Model<ReportAsrs>{
    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 載具
     */
    @TableField("CARRIER")
    private String carrier;
    /**
     * 工令(訂單)編號
     */
    @TableField("WO_SERIAL")
    private String woSerial;
    /**
     * 處理單元標識（同一處理單元多物料同一個ID）
     */
    @TableField("HANDLING_ID")
    private String handlingId;
    /**
     * 建立日期
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    /**
     * 建立人員
     */
    @TableField("CREATOR")
    private String creator;


    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getWoSerial() {
        return woSerial;
    }

    public void setWoSerial(String woSerial) {
        this.woSerial = woSerial;
    }

    public String getHandlingId() {
        return handlingId;
    }

    public void setHandlingId(String handlingId) {
        this.handlingId = handlingId;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }

    @Override
    public String toString() {
        return "Report ASRS{" +
                "handle = " + handle +
                ", carrier = " + carrier +
                ", woSerial = " + woSerial +
                ", handlingId = " + handlingId +
                ", createTime = " + createTime +
                ", creator = " + creator +
                "}";
    }
}
