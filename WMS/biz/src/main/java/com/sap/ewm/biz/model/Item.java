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
 * <p>
 * 物料主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class Item extends Model<Item> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 物料程式碼
     */
   @TableField("ITEM")
   private String item;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 可否混料
     */
   @TableField("MIX_ITEM")
   private String mixItem;
    /**
     * 是否必須檢驗（需要檢驗的物料建立庫存必須為限制庫存）
     */
   @TableField("INSPECTION_REQUIRED")
   private String inspectionRequired;
    /**
     * 狀態
     */
   @TableField("ITEM_STATUS")
   private String itemStatus;
    /**
     * 關聯物料型別主鍵
     */
   @TableField("ITEM_TYPE_BO")
   private String itemTypeBo;
    /**
     * 耗用模式(【列舉】先進先出、近效先出、後進先出)
     */
   @TableField("CONSUMPTION_MODE")
   private String consumptionMode;
    /**
     * 單位
     */
   @TableField("UNIT_OF_MEASURE")
   private String unitOfMeasure;
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

   public String getItem() {
      return item;
   }

   public void setItem(String item) {
      this.item = item;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getMixItem() {
      return mixItem;
   }

   public void setMixItem(String mixItem) {
      this.mixItem = mixItem;
   }

   public String getInspectionRequired() {
      return inspectionRequired;
   }

   public void setInspectionRequired(String inspectionRequired) {
      this.inspectionRequired = inspectionRequired;
   }

   public String getItemStatus() {
      return itemStatus;
   }

   public void setItemStatus(String itemStatus) {
      this.itemStatus = itemStatus;
   }

   public String getItemTypeBo() {
      return itemTypeBo;
   }

   public void setItemTypeBo(String itemTypeBo) {
      this.itemTypeBo = itemTypeBo;
   }

   public String getConsumptionMode() {
      return consumptionMode;
   }

   public void setConsumptionMode(String consumptionMode) {
      this.consumptionMode = consumptionMode;
   }

   public String getUnitOfMeasure() {
      return unitOfMeasure;
   }

   public void setUnitOfMeasure(String unitOfMeasure) {
      this.unitOfMeasure = unitOfMeasure;
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
      return "Item{" +
         "handle = " + handle +
         ", item = " + item +
         ", description = " + description +
         ", mixItem = " + mixItem +
         ", inspectionRequired = " + inspectionRequired +
         ", itemStatus = " + itemStatus +
         ", itemTypeBo = " + itemTypeBo +
         ", consumptionMode = " + consumptionMode +
         ", unitOfMeasure = " + unitOfMeasure +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }

   public static String genHandle(String item) {
      return StringUtils.genHandle(HandleBoConstants.ITEM_BO, item);
   }
}