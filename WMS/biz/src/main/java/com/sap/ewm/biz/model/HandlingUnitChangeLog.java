package com.sap.ewm.biz.model;

import java.io.Serializable;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 處理單元變更日誌記錄
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class HandlingUnitChangeLog extends Model<HandlingUnitChangeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.UUID)
   private String handle;
    /**
     * 處理單元標識（同一處理單元多物料同一個ID）
     */
   @TableField("HANDLING_ID")
   private String handlingId;
    /**
     * 變更事件程式碼（建立/拆並垛/出入庫/盤點）
     */
   @TableField("ACTION_CODE")
   private String actionCode;
    /**
     * 目前所屬貨格
     */
   @TableField("STORAGE_BIN_BO")
   private String storageBinBo;
    /**
     * 庫存數量
     */
   @TableField("INVENTORY_QTY")
   private BigDecimal inventoryQty;
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

   public String getHandlingId() {
      return handlingId;
   }

   public void setHandlingId(String handlingId) {
      this.handlingId = handlingId;
   }

   public String getActionCode() {
      return actionCode;
   }

   public void setActionCode(String actionCode) {
      this.actionCode = actionCode;
   }

   public String getStorageBinBo() {
      return storageBinBo;
   }

   public void setStorageBinBo(String storageBinBo) {
      this.storageBinBo = storageBinBo;
   }

   public BigDecimal getInventoryQty() {
      return inventoryQty;
   }

   public void setInventoryQty(BigDecimal inventoryQty) {
      this.inventoryQty = inventoryQty;
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
      return "HandlingUnitChangeLog{" +
         "handle = " + handle +
         ", handlingId = " + handlingId +
         ", actionCode = " + actionCode +
         ", storageBinBo = " + storageBinBo +
         ", inventoryQty = " + inventoryQty +
         ", createTime = " + createTime +
         ", creator = " + creator +
         "}";
   }
}