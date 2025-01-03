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
 * 處理單元
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class HandlingUnit extends Model<HandlingUnit> {

    private static final long serialVersionUID = 1L;

    public HandlingUnit() {

    }

    public HandlingUnit(String carrierBo, String handlingUnitContextGbo) {
       this.handle = genHandle(carrierBo, handlingUnitContextGbo);
       this.carrierBo = carrierBo;
       this.handlingUnitContextGbo = handlingUnitContextGbo;
    }

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 處理單元標識（同一處理單元多物料同一個ID）數量改變則重建（不包含盤點）
     */
   @TableField("HANDLING_ID")
   private String handlingId;
    /**
     * 關聯載具主鍵
     */
   @TableField("CARRIER_BO")
   private String carrierBo;
    /**
     * 狀態（可用/不可用）
     */
   @TableField("STATUS")
   private String status;
    /**
     * 載具繫結對像主鍵(庫存批次號)
     */
   @TableField("HANDLING_UNIT_CONTEXT_GBO")
   private String handlingUnitContextGbo;
    /**
     * 庫存數量
     */
   @TableField("INVENTORY_QTY")
   private BigDecimal inventoryQty;
   /**
    * 是否混料
    */
   @TableField("MIXED")
   private String mixed;
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

   public String getHandlingId() {
      return handlingId;
   }

   public void setHandlingId(String handlingId) {
      this.handlingId = handlingId;
   }

   public String getCarrierBo() {
      return carrierBo;
   }

   public void setCarrierBo(String carrierBo) {
      this.carrierBo = carrierBo;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getHandlingUnitContextGbo() {
      return handlingUnitContextGbo;
   }

   public void setHandlingUnitContextGbo(String handlingUnitContextGbo) {
      this.handlingUnitContextGbo = handlingUnitContextGbo;
   }

   public BigDecimal getInventoryQty() {
      return inventoryQty;
   }

   public void setInventoryQty(BigDecimal inventoryQty) {
      this.inventoryQty = inventoryQty;
   }

   public String getMixed() {
      return mixed;
   }

   public void setMixed(String mixed) {
      this.mixed = mixed;
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
      return "HandlingUnit{" +
         "handle = " + handle +
         ", handlingId = " + handlingId +
         ", carrierBo = " + carrierBo +
         ", status = " + status +
         ", handlingUnitContextGbo = " + handlingUnitContextGbo +
         ", inventoryQty = " + inventoryQty +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }

   public static String genHandle(String carrierBo, String handlingUnitContextGbo) {
      return StringUtils.genHandle(HandleBoConstants.HANDLING_UNIT_BO, carrierBo, handlingUnitContextGbo, System.currentTimeMillis() + "");
   }
}