package com.sap.ewm.biz.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 狀態主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class Status extends Model<Status> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 狀態
     */
   @TableField("STATUS")
   private String status;
    /**
     * 狀態組(RESRCE/ITEM/INVENTORY)
     */
   @TableField("STATUS_GROUP")
   private String statusGroup;

   @TableField(exist = false)
   private String description;

   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getStatusGroup() {
      return statusGroup;
   }

   public void setStatusGroup(String statusGroup) {
      this.statusGroup = statusGroup;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public String toString() {
      return "Status{" +
         "handle = " + handle +
         ", status = " + status +
         ", statusGroup = " + statusGroup +
         "}";
   }
}