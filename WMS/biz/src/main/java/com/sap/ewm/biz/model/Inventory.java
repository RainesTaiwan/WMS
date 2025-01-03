package com.sap.ewm.biz.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.IdType;
import com.sap.ewm.biz.constants.HandleBoConstants;
import com.sap.ewm.core.utils.StringUtils;

/**
 * <p>
 * 庫存（物料批次）
 * </p>
 *
 * @author Syngna
 * @since 2020-07-05
 */
public class Inventory extends Model<Inventory> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 狀態
     */
    @TableField("STATUS")
    private String status;
    /**
     * 關聯物料主鍵
     */
    @TableField("ITEM_BO")
    private String itemBo;
    /**
     * 單位（物料單位）
     */
    @TableField("UNIT_OF_MEASURE")
    private String unitOfMeasure;
    /**
     * 批次號
     */
    @TableField("BATCH_NO")
    private String batchNo;
    /**
     * 供應商批次號
     */
    @TableField("VENDOR_BATCH_NO")
    private String vendorBatchNo;
    /**
     * 原始批次數量
     */
    @TableField("ORIGINAL_QTY")
    private BigDecimal originalQty;
    /**
     * 現有數量
     */
    @TableField("QTY_ON_HAND")
    private BigDecimal qtyOnHand;
    /**
     * 是否有效，一旦入貨格之後即視為有效庫存，不可刪除
     */
    @TableField("VALID")
    private String valid;
    /**
     * 生產日期
     */
    @TableField("PRODUCTION_DATE")
    private LocalDateTime productionDate;
    /**
     * 失效日期
     */
    @TableField("EXPIRE_DATE")
    private LocalDateTime expireDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemBo() {
        return itemBo;
    }

    public void setItemBo(String itemBo) {
        this.itemBo = itemBo;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getVendorBatchNo() {
        return vendorBatchNo;
    }

    public void setVendorBatchNo(String vendorBatchNo) {
        this.vendorBatchNo = vendorBatchNo;
    }

    public BigDecimal getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
    }

    public BigDecimal getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public LocalDateTime getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDateTime productionDate) {
        this.productionDate = productionDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
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
    public String toString() {
        return "Inventory{" +
                "handle='" + handle + '\'' +
                ", status='" + status + '\'' +
                ", itemBo='" + itemBo + '\'' +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", vendorBatchNo='" + vendorBatchNo + '\'' +
                ", originalQty=" + originalQty +
                ", qtyOnHand=" + qtyOnHand +
                ", valid='" + valid + '\'' +
                ", productionDate=" + productionDate +
                ", expireDate=" + expireDate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator='" + creator + '\'' +
                ", updater='" + updater + '\'' +
                '}';
    }

    public static String genHandle(String batchNo) {
        return StringUtils.genHandle(HandleBoConstants.INVENTORY_BO, batchNo);
    }
}