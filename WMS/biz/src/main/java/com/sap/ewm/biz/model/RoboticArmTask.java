package com.sap.ewm.biz.model;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;


/**
 * 機械手臂任務主數據
 *
 * @author Glanz
 * @since 2021-04-19
 */
public class RoboticArmTask extends Model<RoboticArmTask>{
    private static final long serialVersionUID = 1L;

    /**
     * 任務的MQID
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 訂單主鍵
     */
    @TableField("WO_SERIAL")
    private String woSerial;
    /**
     * 憑單號
     */
    @TableField("VOUCHER_NO")
    private String voucherNo;
    //任務-貨物要求數量
    @TableField("WO_QTY")
    private String woQty;
    //任務-此次貨物要求數量
    @TableField("DO_QTY")
    private String doQty;
    //任務-來料棧板數量(出庫用)
    @TableField("FROM_PALLET_QTY")
    private String fromPalletQty;
    //任務-機械手臂放置物料的棧板，目前的數量
    @TableField("TO_PALLET_QTY")
    private String toPalletQty;
    //任務使用輸送帶
    @TableField("RESOURCE")
    private String resource;
    //出入庫類型 IN (CV2->CV3)/OUT (CV3->CV2)
    @TableField("TYPE")
    private String type;
    //棧板ID
    @TableField("CARRIER")
    private String carrier;
    //儲存貨格
    @TableField("STORAGE_BIN")
    private String storageBin;
    //任務順序
    @TableField("TASK_ORDER")
    private String taskOrder;
    //任務狀態
    @TableField("STATUS")
    private String status;
    //任務結果
    @TableField("RESULT")
    private String result;
    /**
     * 建立日期
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    /**
     * 更新日期
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    /**
     * 建立人員
     */
    @TableField("CREATOR")
    private String creator;
    /**
     * 更新人員
     */
    @TableField("UPDATER")
    private String updater;


    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getWoserial() {
        return woSerial;
    }

    public void setWoserial(String woSerial) {
        this.woSerial = woSerial;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getWoQty() {
        return woQty;
    }

    public void setWoQty(String woQty) {
        this.woQty = woQty;
    }

    public String getDoQty() {
        return doQty;
    }

    public void setDoQty(String doQty) {
        this.doQty = doQty;
    }

    public String getFromPalletQty() {
        return fromPalletQty;
    }

    public void setFromPalletQty(String fromPalletQty) {
        this.fromPalletQty = fromPalletQty;
    }

    public String getToPalletQty() {
        return toPalletQty;
    }

    public void setToPalletQty(String toPalletQty) {
        this.toPalletQty = toPalletQty;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public String getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(String taskOrder) {
        this.taskOrder = taskOrder;
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
        return "RoboticArmTask{" +
                "handle = " + handle +
                ", woSerial = " + woSerial +
                ", voucherNo = " + voucherNo +
                ", woQty = " + woQty +
                ", doQty = " + doQty +
                ", fromPalletQty = " + fromPalletQty +
                ", toPalletQty = " + toPalletQty +
                ", resource = " + resource +
                ", type = " + type +
                ", carrier = " + carrier +
                ", storageBin = " + storageBin +
                ", taskOrder = " + taskOrder +
                ", status = " + status +
                ", result = " + result +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", creator = " + creator +
                ", updater = " + updater +
                "}";
    }
}
