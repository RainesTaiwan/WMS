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
 * 資源主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class Resrce extends Model<Resrce> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 資源
     */
   @TableField("RESRCE")
   private String resrce;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 狀態
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

   public String getResrce() {
      return resrce;
   }

   public void setResrce(String resrce) {
      this.resrce = resrce;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
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
      return "Resrce{" +
         "handle = " + handle +
         ", resrce = " + resrce +
         ", description = " + description +
         ", status = " + status +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }

   public static String genHandle(String resrce) {
      return StringUtils.genHandle(HandleBoConstants.RESOURCE_BO, resrce);
   }
}