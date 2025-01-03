package com.sap.ewm.dashboard.model;

import java.io.Serializable;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * inventory summary by day
 * </p>
 *
 * @author syngna
 * @since 2020-08-03
 */
@TableName("DS_INVENTORY_SUMMARY")
public class InventorySummary extends Model<InventorySummary> {

    private static final long serialVersionUID = 1L;

   @TableId(value = "HANDLE", type = IdType.UUID)
   private String handle;
    /**
     * material
     */
   @TableField("ITEM")
   private String item;
    /**
     * total inventory qty
     */
   @TableField("TOTAL_QTY")
   private BigDecimal totalQty;
    /**
     * inbound qty
     */
   @TableField("INBOUND_QTY")
   private BigDecimal inboundQty;
    /**
     * outbound qty
     */
   @TableField("OUTBOUND_QTY")
   private BigDecimal outboundQty;
    /**
     * date
     */
   @TableField("STATISTIC_DATE")
   private LocalDate statisticDate;


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

   public BigDecimal getTotalQty() {
      return totalQty;
   }

   public void setTotalQty(BigDecimal totalQty) {
      this.totalQty = totalQty;
   }

   public BigDecimal getInboundQty() {
      return inboundQty;
   }

   public void setInboundQty(BigDecimal inboundQty) {
      this.inboundQty = inboundQty;
   }

   public BigDecimal getOutboundQty() {
      return outboundQty;
   }

   public void setOutboundQty(BigDecimal outboundQty) {
      this.outboundQty = outboundQty;
   }

   public LocalDate getStatisticDate() {
      return statisticDate;
   }

   public void setStatisticDate(LocalDate statisticDate) {
      this.statisticDate = statisticDate;
   }

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "InventorySummary{" +
         "handle = " + handle +
         ", item = " + item +
         ", totalQty = " + totalQty +
         ", inboundQty = " + inboundQty +
         ", outboundQty = " + outboundQty +
         ", statisticDate = " + statisticDate +
         "}";
   }
}