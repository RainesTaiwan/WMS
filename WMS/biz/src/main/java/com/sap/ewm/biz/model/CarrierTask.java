package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;


/**
 * 棧板任務主數據
 *
 * @author Glanz
 * @since 2021-06-01
 */
public class CarrierTask extends Model<CarrierTask>{
    private static final long serialVersionUID = 1L;

    //主鍵 任務的MQID
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    //訂單號
    @TableField("WO_TYPE")
    private String woType;
    //憑單號
    @TableField("VOUCHER_NO")
    private String voucherNo;
    //訂單號
    @TableField("WO_SERIAL")
    private String woSerial;
    //機械手臂任務的MQID
    @TableField("RARM_TASK_ID")
    private String rArmTaskId;
    //棧板ID
    @TableField("CARRIER")
    private String carrier;
    //任務使用輸送帶
    @TableField("RESOURCE")
    private String resource;
    //起始輸送帶工位
    @TableField("START_STATION")
    private String startStation;
    //終點輸送帶工位
    @TableField("END_STATION")
    private String endStation;
    //儲位
    @TableField("STORAGE_BIN")
    private String storageBin;
    //任務類型1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OUT、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
    //任務類型2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    @TableField("TASK_TYPE")
    private String taskType;
    //任務狀態
    @TableField("STATUS")
    private String status;
    //任務結果
    @TableField("RESULT")
    private String result;
    //任務順序
    @TableField("TASK_ORDER")
    private String taskOrder;
    //建立日期
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    //更新日期
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    //建立人員
    @TableField("CREATOR")
    private String creator;
    //更新人員
    @TableField("UPDATER")
    private String updater;


    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getWoType() {
        return woType;
    }

    public void setWoType(String woType) {
        this.woType = woType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getWoserial() {
        return woSerial;
    }

    public void setWoserial(String woSerial) {
        this.woSerial = woSerial;
    }

    public String getRArmTaskId() {
        return rArmTaskId;
    }

    public void setRArmTaskId(String rArmTaskId) {
        this.rArmTaskId = rArmTaskId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(String taskOrder) {
        this.taskOrder = taskOrder;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }

    public static final String HANDLE = "HANDLE";

    @Override
    public String toString() {
        return "CarrierTask{" +
                "handle = " + handle +
                ", woType = " + woType +
                ", voucherNo = " + voucherNo +
                ", woSerial = " + woSerial +
                ", rArmTaskId = " + rArmTaskId +
                ", carrier = " + carrier +
                ", resource = " + resource +
                ", startStation = " + startStation +
                ", endStation = " + endStation +
                ", storageBin = " + storageBin +
                ", taskType = " + taskType +
                ", status = " + status +
                ", result = " + result +
                ", taskOrder = " + taskOrder +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", creator = " + creator +
                ", updater = " + updater +
                "}";
    }
}
