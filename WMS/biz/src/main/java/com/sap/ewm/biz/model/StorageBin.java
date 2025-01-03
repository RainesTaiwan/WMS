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
 * 儲存貨格主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class StorageBin extends Model<StorageBin> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 儲存貨格程式碼
     */
   @TableField("STORAGE_BIN")
   private String storageBin;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 關聯儲存貨格型別主鍵
     */
   @TableField("STORAGE_BIN_TYPE_BO")
   private String storageBinTypeBo;
    /**
     * 關聯儲存位置主鍵(一個儲存位置可放多個貨格)
     */
   @TableField("STORAGE_LOCATION_BO")
   private String storageLocationBo;
    /**
     * 行
     */
   @TableField("SHELF_ROW")
   private Integer shelfRow;
    /**
     * 列
     */
   @TableField("SHELF_COLUMN")
   private Integer shelfColumn;
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

   public String getStorageBin() {
      return storageBin;
   }

   public void setStorageBin(String storageBin) {
      this.storageBin = storageBin;
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

   public Integer getShelfRow() {
      return shelfRow;
   }

   public void setShelfRow(Integer shelfRow) {
      this.shelfRow = shelfRow;
   }

   public Integer getShelfColumn() {
      return shelfColumn;
   }

   public void setShelfColumn(Integer shelfColumn) {
      this.shelfColumn = shelfColumn;
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

   public static String genHandle(String storageBin) {
       return StringUtils.genHandle(HandleBoConstants.STORAGE_BIN_BO, storageBin);
   }

   @Override
   public String toString() {
      return "StorageBin{" +
         "handle = " + handle +
         ", storageBin = " + storageBin +
         ", description = " + description +
         ", storageBinTypeBo = " + storageBinTypeBo +
         ", storageLocationBo = " + storageLocationBo +
         ", shelfRow = " + shelfRow +
         ", shelfColumn = " + shelfColumn +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }
}