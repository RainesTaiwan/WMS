package com.sap.ewm.biz.model;

import java.io.Serializable;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * shelf off log
 * </p>
 *
 * @author syngna
 * @since 2020-08-04
 */
public class ShelfOffLog extends Model<ShelfOffLog> {

    private static final long serialVersionUID = 1L;

   @TableId(value = "HANDLE", type = IdType.UUID)
   private String handle;
    /**
     * request id
     */
   @TableField("MESSAGE_ID")
   private String messageId;
    /**
     * correlation id
     */
   @TableField("CORRELATION_ID")
   private String correlationId;
    /**
     * action code
     */
   @TableField("ACTION_CODE")
   private String actionCode;
    /**
     * carrier
     */
   @TableField("CARRIER")
   private String carrier;
    /**
     * storage location
     */
   @TableField("STORAGE_LOCATION")
   private String storageLocation;
    /**
     * storage bin
     */
   @TableField("STORAGE_BIN")
   private String storageBin;
    /**
     * inventory handle
     */
   @TableField("INVENTORY_BO")
   private String inventoryBo;
    /**
     * qty
     */
   @TableField("QTY")
   private BigDecimal qty;
    /**
     * split
     */
   @TableField("SPLIT")
   private String split;
   /**
    * execute result
    */
   @TableField("EXECUTE_RESULT")
   private String executeResult;
    /**
     * create time
     */
   @TableField("CREATE_TIME")
   private LocalDateTime createTime;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
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

   public String getActionCode() {
      return actionCode;
   }

   public void setActionCode(String actionCode) {
      this.actionCode = actionCode;
   }

   public String getCarrier() {
      return carrier;
   }

   public void setCarrier(String carrier) {
      this.carrier = carrier;
   }

   public String getStorageLocation() {
      return storageLocation;
   }

   public void setStorageLocation(String storageLocation) {
      this.storageLocation = storageLocation;
   }

   public String getStorageBin() {
      return storageBin;
   }

   public void setStorageBin(String storageBin) {
      this.storageBin = storageBin;
   }

   public String getInventoryBo() {
      return inventoryBo;
   }

   public void setInventoryBo(String inventoryBo) {
      this.inventoryBo = inventoryBo;
   }

   public BigDecimal getQty() {
      return qty;
   }

   public void setQty(BigDecimal qty) {
      this.qty = qty;
   }

   public String getSplit() {
      return split;
   }

   public void setSplit(String split) {
      this.split = split;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }

   public String getExecuteResult() {
      return executeResult;
   }

   public void setExecuteResult(String executeResult) {
      this.executeResult = executeResult;
   }

   @Override
   public String toString() {
      return "ShelfOffLog{" +
         "handle = " + handle +
         ", messageId = " + messageId +
         ", correlationId = " + correlationId +
         ", actionCode = " + actionCode +
         ", carrier = " + carrier +
         ", storageLocation = " + storageLocation +
         ", storageBin = " + storageBin +
         ", inventoryBo = " + inventoryBo +
         ", qty = " + qty +
         ", split = " + split +
         ", createTime = " + createTime +
         "}";
   }
}