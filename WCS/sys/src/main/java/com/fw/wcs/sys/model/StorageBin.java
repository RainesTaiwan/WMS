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
public class StorageBin extends Model<StorageBin> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 儲存貨格編號
     */
   @TableField("STORAGE_BIN")
   private String storageBin;
    /**
     * 儲存貨格編號
     */
   @TableField("VMS_STORAGE_BIN")
   private String vmsStorageBin;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 貨格型別
     */
   @TableField("STORAGE_BIN_TYPE_BO")
   private String storageBinTypeBo;
    /**
     * 儲存位置
     */
   @TableField("STORAGE_LOCATION_BO")
   private String storageLocationBo;
    /**
     * 行
     */
   @TableField("SHELF_ROW")
   private String shelfRow;
    /**
     * 列
     */
   @TableField("SHELF_COLUMN")
   private String shelfColumn;
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

   public String getStorageBin() {
      return storageBin;
   }

   public void setStorageBin(String storageBin) {
      this.storageBin = storageBin;
   }

   public String getVmsStorageBin() {
      return vmsStorageBin;
   }

   public void setVmsStorageBin(String vmsStorageBin) {
      this.vmsStorageBin = vmsStorageBin;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getStorageBinTypeBo() {
      return storageBinTypeBo;
   }

   public void setStorageBinTypeBo(String storageBinTypeBo) {
      this.storageBinTypeBo = storageBinTypeBo;
   }

   public String getStorageLocationBo() {
      return storageLocationBo;
   }

   public void setStorageLocationBo(String storageLocationBo) {
      this.storageLocationBo = storageLocationBo;
   }

   public String getShelfRow() {
      return shelfRow;
   }

   public void setShelfRow(String shelfRow) {
      this.shelfRow = shelfRow;
   }

   public String getShelfColumn() {
      return shelfColumn;
   }

   public void setShelfColumn(String shelfColumn) {
      this.shelfColumn = shelfColumn;
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

   public static final String STORAGE_BIN = "STORAGE_BIN";

   public static final String VMS_STORAGE_BIN = "VMS_STORAGE_BIN";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String STORAGE_BIN_TYPE_BO = "STORAGE_BIN_TYPE_BO";

   public static final String STORAGE_LOCATION_BO = "STORAGE_LOCATION_BO";

   public static final String SHELF_ROW = "SHELF_ROW";

   public static final String SHELF_COLUMN = "SHELF_COLUMN";

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
      return "StorageBin{" +
         "handle = " + handle +
         ", storageBin = " + storageBin +
         ", vmsStorageBin = " + vmsStorageBin +
         ", description = " + description +
         ", storageBinTypeBo = " + storageBinTypeBo +
         ", storageLocationBo = " + storageLocationBo +
         ", shelfRow = " + shelfRow +
         ", shelfColumn = " + shelfColumn +
         ", createUser = " + createUser +
         ", createdTime = " + createdTime +
         ", updateUser = " + updateUser +
         ", updatedTime = " + updatedTime +
         "}";
   }
}