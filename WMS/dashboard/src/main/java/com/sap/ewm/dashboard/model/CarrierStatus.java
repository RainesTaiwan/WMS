package com.sap.ewm.dashboard.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * handling unit stataus summary
 * </p>
 *
 * @author syngna
 * @since 2020-08-06
 */
@TableName("DS_CARRIER_STATUS")
public class CarrierStatus extends Model<CarrierStatus> {

    private static final long serialVersionUID = 1L;

   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * material
     */
   @TableField("ITEM")
   private String item;
    /**
     * carrier
     */
   @TableField("CARRIER")
   private String carrier;
    /**
     * operation
     */
   @TableField("OPERATION")
   private String operation;
    /**
     * status
     */
   @TableField("STATUS")
   private String status;

   public CarrierStatus() {

   }

   public CarrierStatus(String carrier, String item, String operation, String status) {
      this.handle = carrier;
      this.carrier = carrier;
      this.item = item;
      this.operation = operation;
      this.status = status;
   }


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

   public String getCarrier() {
      return carrier;
   }

   public void setCarrier(String carrier) {
      this.carrier = carrier;
   }

   public String getOperation() {
      return operation;
   }

   public void setOperation(String operation) {
      this.operation = operation;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "CarrierStatus{" +
         "handle = " + handle +
         ", item = " + item +
         ", carrier = " + carrier +
         ", operation = " + operation +
         ", status = " + status +
         "}";
   }
}