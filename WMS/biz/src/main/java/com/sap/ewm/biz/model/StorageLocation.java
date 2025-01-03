package com.sap.ewm.biz.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.sap.ewm.biz.constants.HandleBoConstants;
import com.sap.ewm.core.utils.StringUtils;

/**
 * <p>
 * 儲存位置主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class StorageLocation extends Model<StorageLocation> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 儲存位置程式碼
     */
   @TableField("STORAGE_LOCATION")
   private String storageLocation;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 關聯儲存位置型別主鍵
     */
   @TableField("STORAGE_LOCATION_TYPE_BO")
   private String storageLocationTypeBo;
    /**
     * 關聯倉庫主鍵
     */
   @TableField("WAREHOUSE_BO")
   private String warehouseBo;
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


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getStorageLocation() {
      return storageLocation;
   }

   public void setStorageLocation(String storageLocation) {
      this.storageLocation = storageLocation;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getStorageLocationTypeBo() {
      return storageLocationTypeBo;
   }

   public void setStorageLocationTypeBo(String storageLocationTypeBo) {
      this.storageLocationTypeBo = storageLocationTypeBo;
   }

   public String getWarehouseBo() {
      return warehouseBo;
   }

   public void setWarehouseBo(String warehouseBo) {
      this.warehouseBo = warehouseBo;
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
   public String toString() {
      return "StorageLocation{" +
         "handle = " + handle +
         ", storageLocation = " + storageLocation +
         ", description = " + description +
         ", storageLocationTypeBo = " + storageLocationTypeBo +
         ", warehouseBo = " + warehouseBo +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }

   public static String genHandle(String storageLocation) {
      return StringUtils.genHandle(HandleBoConstants.STORAGE_LOCATION_BO, storageLocation);
   }
}