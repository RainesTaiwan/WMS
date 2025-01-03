package com.sap.ewm.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.sap.ewm.biz.constants.HandleBoConstants;
import com.sap.ewm.core.utils.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 資源組組主數據
 * </p>
 *
 * @author syngna
 * @since 2020-07-17
 */
public class ResourceType extends Model<ResourceType> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
   @TableField("RESOURCE_TYPE")
   private String resourceType;
    /**
     * 描述
     */
   @TableField("DESCRIPTION")
   private String description;
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

   public String getResourceType() {
      return resourceType;
   }

   public void setResourceType(String resourceType) {
      this.resourceType = resourceType;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
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
      return "ResourceType{" +
         "handle = " + handle +
         ", resourceType = " + resourceType +
         ", description = " + description +
         ", createTime = " + createTime +
         ", updateTime = " + updateTime +
         ", creator = " + creator +
         ", updater = " + updater +
         "}";
   }

   public static String genHandle(String resourceType) {
      return StringUtils.genHandle(HandleBoConstants.RESOURCE_TYPE_BO, resourceType);
   }
}