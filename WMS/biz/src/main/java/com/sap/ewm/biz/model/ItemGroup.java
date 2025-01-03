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
 * 物料組主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class ItemGroup extends Model<ItemGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 物料組程式碼
     */
   @TableField("ITEM_GROUP")
   private String itemGroup;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 耗用模式
     */
   @TableField("CONSUMPTION_MODE")
   private String consumptionMode;
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

   public String getItemGroup() {
      return itemGroup;
   }

   public void setItemGroup(String itemGroup) {
      this.itemGroup = itemGroup;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getConsumptionMode() {
      return consumptionMode;
   }

   public void setConsumptionMode(String consumptionMode) {
      this.consumptionMode = consumptionMode;
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
      return "ItemGroup{" +
         "handle = " + handle +
         ", itemGroup = " + itemGroup +
         ", description = " + description +
         ", consumptionMode = " + consumptionMode +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }

   public static String genHandle(String itemGroup) {
      return StringUtils.genHandle(HandleBoConstants.ITEM_GROUP_BO, itemGroup);
   }
}