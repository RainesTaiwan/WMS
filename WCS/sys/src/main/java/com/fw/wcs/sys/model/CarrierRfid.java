package com.fw.wcs.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author Leon
 *
 */
public class CarrierRfid extends Model<CarrierRfid> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * RFID
     */
   @TableField("RFID")
   private String rfid;
    /**
     * 載具編號
     */
   @TableField("CARRIER")
   private String carrier;
    /**
     * 建立人員
     */
   @TableField("CREATE_USER")
   private String createUser;
    /**
     * 建立時間
     */
   @TableField("CREATED_TIME")
   private Date createdTime;
    /**
     * 更新人員
     */
   @TableField("UPDATE_USER")
   private String updateUser;
    /**
     * 更新時間
     */
   @TableField("UPDATED_TIME")
   private Date updatedTime;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getRfid() {
      return rfid;
   }

   public void setRfid(String rfid) {
      this.rfid = rfid;
   }

   public String getCarrier() {
      return carrier;
   }

   public void setCarrier(String carrier) {
      this.carrier = carrier;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public Date getCreatedTime() {
      return createdTime;
   }

   public void setCreatedTime(Date createdTime) {
      this.createdTime = createdTime;
   }

   public String getUpdateUser() {
      return updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public Date getUpdatedTime() {
      return updatedTime;
   }

   public void setUpdatedTime(Date updatedTime) {
      this.updatedTime = updatedTime;
   }

   public static final String HANDLE = "HANDLE";

   public static final String RFID = "RFID";

   public static final String CARRIER = "CARRIER";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATED_TIME = "CREATED_TIME";

   public static final String UPDATE_USER = "UPDATE_USER";

   public static final String UPDATED_TIME = "UPDATED_TIME";

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "CarrierRfid{" +
         "handle = " + handle +
         ", rfid = " + rfid +
         ", carrier = " + carrier +
         ", createUser = " + createUser +
         ", createdTime = " + createdTime +
         ", updateUser = " + updateUser +
         ", updatedTime = " + updatedTime +
         "}";
   }
}