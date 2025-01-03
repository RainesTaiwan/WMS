package com.sap.ewm.biz.model;

import java.io.Serializable;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 單位轉換矩陣
 * </p>
 *
 * @author syngna
 * @since 2020-07-19
 */
public class MeasureUnitMatrix extends Model<MeasureUnitMatrix> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 原始單位
     */
   @TableField("MEASURE_UNIT")
   private String measureUnit;
    /**
     * 目標單位
     */
   @TableField("DEST_MEASURE_UNIT")
   private String destMeasureUnit;
    /**
     * 轉換比率
     */
   @TableField("TRANSFER_RATE")
   private BigDecimal transferRate;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getMeasureUnit() {
      return measureUnit;
   }

   public void setMeasureUnit(String measureUnit) {
      this.measureUnit = measureUnit;
   }

   public String getDestMeasureUnit() {
      return destMeasureUnit;
   }

   public void setDestMeasureUnit(String destMeasureUnit) {
      this.destMeasureUnit = destMeasureUnit;
   }

   public BigDecimal getTransferRate() {
      return transferRate;
   }

   public void setTransferRate(BigDecimal transferRate) {
      this.transferRate = transferRate;
   }

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "MeasureUnitMatrix{" +
         "handle = " + handle +
         ", measureUnit = " + measureUnit +
         ", destMeasureUnit = " + destMeasureUnit +
         ", transferRate = " + transferRate +
         "}";
   }
}