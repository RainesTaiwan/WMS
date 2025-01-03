package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 儲存貨格狀態
 * </p>
 *
 * @author syngna
 * @since 2020-07-14
 */
public class StorageBinStatus extends Model<StorageBinStatus> {

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
     * 儲存貨格狀態
     */
   @TableField("STATUS")
   private String status;
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

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
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

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "StorageBinStatus{" +
         "handle = " + handle +
         ", storageBin = " + storageBin +
         ", status = " + status +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         "}";
   }
}