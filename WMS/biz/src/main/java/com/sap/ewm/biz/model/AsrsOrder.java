package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.sap.ewm.biz.constants.HandleBoConstants;
import com.sap.ewm.core.utils.StringUtils;


/**
 * 訂單主數據
 *
 * @author Glanz
 * @since 2021-04-19
 */

public class AsrsOrder extends Model<AsrsOrder> {
    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * MQ ID
     */
    @TableField("MESSAGE_ID")
    private String messageId;
    /**
     * 工令(訂單)編號
     */
    @TableField("WO_SERIAL")
    private String woSerial;
    /**
     * 工令屬性
     */
    @TableField("WO_TYPE")
    private String woType;
    /**
     * 此訂單處理量，校驗用（一訂單含多憑單號）
     */
    @TableField("ORDER_QTY")
    private String orderQTY;
    /**
     * 憑單號
     */
    @TableField("VOUCHER_NO")
    private String voucherNo;
    /**
     * 憑單號處理量
     */
    @TableField("ITEM_COUNT")
    private String itemCount;
    /**
     * 物料/貨品
     */
    @TableField("ITEM")
    private String item;
    /**
     * 貨品包裝
     */
    @TableField("CONTAINER")
    private String container;
    /**
     * 存儲貨格代號
     */
    @TableField("STORAGE_BIN")
    private String storageBin;
    /**
     * 驗證訂單合法性
     */
    @TableField("VALIDATION")
    private String validation;
    /**
     * 訂單狀態
     */
    @TableField("STATUS")
    private String status;
    //任務使用輸送帶
    @TableField("RESOURCE")
    private String resource;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getWoSerial() {
        return woSerial;
    }

    public void setWoSerial(String woSerial) {
        this.woSerial = woSerial;
    }

    public String getWoType() {
        return woType;
    }

    public void setWoType(String woType) {
        this.woType = woType;
    }

    public String getOrderQTY() {
        return orderQTY;
    }

    public void setOrderQTY(String orderQTY) {
        this.orderQTY = orderQTY;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    @Override
    public String toString() {
        return "ASRS Order{" +
                "handle = " + handle +
                ", messageId = " + messageId +
                ", woSerial = " + woSerial +
                ", woType = " + woType +
                ", orderQTY = " + orderQTY +
                ", voucherNo = " + voucherNo +
                ", itemCount = " + itemCount +
                ", item = " + item +
                ", container = " + container +
                ", storageBin = " + storageBin +
                ", validation = " + validation +
                ", status = " + status +
                ", resource = " + resource +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", creator = " + creator +
                ", updater = " + updater +
                "}";
    }

    public static String genHandle(String orderId) {
        return StringUtils.genHandle(HandleBoConstants.ASRS_ORDER_BO, orderId);
    }
}
