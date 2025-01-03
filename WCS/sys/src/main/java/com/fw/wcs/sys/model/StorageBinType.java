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
public class StorageBinType extends Model<StorageBinType> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 儲存貨格型別
     */
   @TableField("STORAGE_BIN_TYPE")
   private String storageBinType;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 寬度
     */
   @TableField("WIDTH")
   private String width;
    /**
     * 長度
     */
   @TableField("LENGHT")
   private String lenght;
    /**
     * 高度
     */
   @TableField("HEIGHT")
   private String height;
    /**
     * 最大承重
     */
   @TableField("MAX_WEIGHT")
   private String maxWeight;
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

   public String getStorageBinType() {
      return storageBinType;
   }

   public void setStorageBinType(String storageBinType) {
      this.storageBinType = storageBinType;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getWidth() {
      return width;
   }

   public void setWidth(String width) {
      this.width = width;
   }

   public String getLenght() {
      return lenght;
   }

   public void setLenght(String lenght) {
      this.lenght = lenght;
   }

   public String getHeight() {
      return height;
   }

   public void setHeight(String height) {
      this.height = height;
   }

   public String getMaxWeight() {
      return maxWeight;
   }

   public void setMaxWeight(String maxWeight) {
      this.maxWeight = maxWeight;
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

   public static final String STORAGE_BIN_TYPE = "STORAGE_BIN_TYPE";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String WIDTH = "WIDTH";

   public static final String LENGHT = "LENGHT";

   public static final String HEIGHT = "HEIGHT";

   public static final String MAX_WEIGHT = "MAX_WEIGHT";

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
      return "StorageBinType{" +
         "handle = " + handle +
         ", storageBinType = " + storageBinType +
         ", description = " + description +
         ", width = " + width +
         ", lenght = " + lenght +
         ", height = " + height +
         ", maxWeight = " + maxWeight +
         ", createUser = " + createUser +
         ", createdTime = " + createdTime +
         ", updateUser = " + updateUser +
         ", updatedTime = " + updatedTime +
         "}";
   }
}