package com.fw.wcs.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @author Leon
 *
 */
public class CarrierTask extends Model<CarrierTask> {

    private static final long serialVersionUID = 1L;

   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
   @TableField("CATEGORY")
   private String category;
   @TableField("CARRIER")
   private String carrier;
   @TableField("START_POSITION")
   private String startPosition;
   @TableField("TARGET_POSITION")
   private String targetPosition;
   @TableField("STATUS")
   private String status;
   @TableField("AGV_NO")
   private String agvNo;
   @TableField("VMS_ID")
   private String vmsId;
   @TableField("WMS_ID")
   private String wmsId;
   @TableField("COMMENTS")
   private String comments;
   @TableField("CREATE_USER")
   private String createUser;
   @TableField("CREATED_TIME")
   private Date createdTime;
   @TableField("UPDATE_USER")
   private String updateUser;
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

   public String getAgvNo() {
      return agvNo;
   }

   public void setAgvNo(String agvNo) {
      this.agvNo = agvNo;
   }

   public String getVmsId() {
      return vmsId;
   }

   public void setVmsId(String vmsId) {
      this.vmsId = vmsId;
   }

   public String getWmsId() {
      return wmsId;
   }

   public void setWmsId(String wmsId) {
      this.wmsId = wmsId;
   }

   public String getComments() {
      return comments;
   }

   public void setComments(String comments) {
      this.comments = comments;
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

   public static final String CARRIER = "CARRIER";

   public static final String START_POSITION = "START_POSITION";

   public static final String TARGET_POSITION = "TARGET_POSITION";

   public static final String STATUS = "STATUS";

   public static final String AGV_NO = "AGV_NO";

   public static final String VMS_ID = "VMS_ID";

   public static final String WMS_ID = "WMS_ID";

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
      return "CarrierTask{" +
         "handle = " + handle +
         ", category = " + category +
         ", carrier = " + carrier +
         ", startPosition = " + startPosition +
         ", targetPosition = " + targetPosition +
         ", status = " + status +
         ", agvNo = " + agvNo +
         ", vmsId = " + vmsId +
         ", wmsId = " + wmsId +
         ", createUser = " + createUser +
         ", createdTime = " + createdTime +
         ", updateUser = " + updateUser +
         ", updatedTime = " + updatedTime +
         "}";
   }
}