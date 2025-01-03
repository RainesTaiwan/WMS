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
public class Agv extends Model<Agv> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * AGV編號
     */
   @TableField("AGV_NO")
   private String agvNo;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
   /**
    * 模式
    */
   @TableField("MODE")
   private String mode;
   /**
    * 目前位置
    */
   @TableField("POSITION")
   private String position;
   /**
    * 執行的工作MQID
    */
   @TableField("TASK_ID")
   private String taskId;
   /**
    * 執行的工作類型
    */
   @TableField("TASK_TYPE")
   private String taskType;
   /**
    * 執行的工作開始時間
    */
   @TableField("TASK_STARTTIME")
   private Date taskStartTime;
   /**
    * 電池容量
    */
   @TableField("CAPACITY")
   private String capacity;
    /**
     * 狀態
     */
   @TableField("STATUS")
   private String status;
    /**
     * AGV工作時間長度
     */
   @TableField("WORKING_TIME")
   private String workingTime;
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

   public String getAgvNo() {
      return agvNo;
   }

   public void setAgvNo(String agvNo) {
      this.agvNo = agvNo;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getMode() {
      return mode;
   }

   public void setMode(String mode) {
      this.mode = mode;
   }

   public String getPosition() {
      return position;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public String getTaskId() {
      return taskId;
   }

   public void setTaskId(String taskId) {
      this.taskId = taskId;
   }

   public String getTaskType() {
      return taskType;
   }

   public void setTaskType(String taskType) {
      this.taskType = taskType;
   }

   public Date getTaskStartTime() {
      return taskStartTime;
   }

   public void setTaskStartTime(Date taskStartTime) {
      this.taskStartTime = taskStartTime;
   }

   public String getCapacity() {
      return capacity;
   }

   public void setCapacity(String capacity) {
      this.capacity = capacity;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getWorkingTime() {
      return workingTime;
   }

   public void setWorkingTime(String workingTime) {
      this.workingTime = workingTime;
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

   public static final String AGV_NO = "AGV_NO";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String MODE = "MODE";

   public static final String POSITION = "POSITION";

   public static final String TASK_ID = "TASK_ID";

   public static final String TASK_TYPE = "TASK_TYPE";

   public static final String TASK_STARTTIME = "TASK_STARTTIME";

   public static final String CAPACITY = "CAPACITY";

   public static final String STATUS = "STATUS";

   public static final String WORKING_TIME = "WORKING_TIME";

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
      return "Agv{" +
         "handle = " + handle +
         ", agvNo = " + agvNo +
         ", description = " + description +
         ", mode = " + mode +
         ", position = " + position +
         ", taskId = " + taskId +
         ", taskType = " + taskType +
         ", taskStartTime = " + taskStartTime +
         ", capacity = " + capacity +
         ", status = " + status +
         ", workingTime = " + workingTime +
         ", createUser = " + createUser +
         ", createdTime = " + createdTime +
         ", updateUser = " + updateUser +
         ", updatedTime = " + updatedTime +
         "}";
   }
}