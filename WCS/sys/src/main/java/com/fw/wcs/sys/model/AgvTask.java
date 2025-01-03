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
public class AgvTask extends Model<AgvTask> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 型別
     */
   @TableField("CATEGORY")
   private String category;
    /**
     * AGV編號
     */
   @TableField("AGV_NO")
   private String agvNo;
    /**
     * 載具編號
     */
   @TableField("CARRIER")
   private String carrier;
    /**
     * 起始位置
     */
   @TableField("START_POSITION")
   private String startPosition;
    /**
     * 目標位置
     */
   @TableField("TARGET_POSITION")
   private String targetPosition;
    /**
     * 任務狀態
     */
   @TableField("STATUS")
   private String status;
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

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getAgvNo() {
      return agvNo;
   }

   public void setAgvNo(String agvNo) {
      this.agvNo = agvNo;
   }

   public String getCarrier() {
      return carrier;
   }

   public void setCarrier(String carrier) {
      this.carrier = carrier;
   }

   public String getStartPosition() {
      return startPosition;
   }

   public void setStartPosition(String startPosition) {
      this.startPosition = startPosition;
   }

   public String getTargetPosition() {
      return targetPosition;
   }

   public void setTargetPosition(String targetPosition) {
      this.targetPosition = targetPosition;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
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

   public static final String CATEGORY = "CATEGORY";

   public static final String AGV_NO = "AGV_NO";

   public static final String CARRIER = "CARRIER";

   public static final String START_POSITION = "START_POSITION";

   public static final String TARGET_POSITION = "TARGET_POSITION";

   public static final String STATUS = "STATUS";

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
      return "AgvTask{" +
         "handle = " + handle +
         ", category = " + category +
         ", agvNo = " + agvNo +
         ", carrier = " + carrier +
         ", startPosition = " + startPosition +
         ", targetPosition = " + targetPosition +
         ", status = " + status +
         ", createUser = " + createUser +
         ", createdTime = " + createdTime +
         ", updateUser = " + updateUser +
         ", updatedTime = " + updatedTime +
         "}";
   }
}