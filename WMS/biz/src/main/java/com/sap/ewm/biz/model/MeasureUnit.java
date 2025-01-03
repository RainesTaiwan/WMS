package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 計量單位主數據
 * </p>
 *
 * @author syngna
 * @since 2020-07-19
 */
public class MeasureUnit extends Model<MeasureUnit> {

    private static final long serialVersionUID = 1L;

    /**
     * 計量單位
     */
   @TableId(value = "MEASURE_UNIT", type = IdType.INPUT)
   private String measureUnit;
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


   public String getMeasureUnit() {
      return measureUnit;
   }

   public void setMeasureUnit(String measureUnit) {
      this.measureUnit = measureUnit;
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
      return this.measureUnit;
   }

   @Override
   public String toString() {
      return "MeasureUnit{" +
         "measureUnit = " + measureUnit +
         ", description = " + description +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }
}