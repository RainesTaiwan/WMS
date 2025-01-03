package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 物料耗用模式主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class ConsumptionMode extends Model<ConsumptionMode> {

    private static final long serialVersionUID = 1L;

    /**
     * 耗用模式
     */
   @TableId(value = "CONSUMPTION_MODE", type = IdType.INPUT)
   private String consumptionMode;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
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


   public String getConsumptionMode() {
      return consumptionMode;
   }

   public void setConsumptionMode(String consumptionMode) {
      this.consumptionMode = consumptionMode;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
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
      return this.consumptionMode;
   }

   @Override
   public String toString() {
      return "ConsumptionMode{" +
         "consumptionMode = " + consumptionMode +
         ", description = " + description +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }
}