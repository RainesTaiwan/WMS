package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.sap.ewm.biz.constants.HandleBoConstants;
import com.sap.ewm.core.utils.StringUtils;

/**
 * <p>
 * 資源關聯資源型別
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class ResourceTypeResource extends Model<ResourceTypeResource> {

    private static final long serialVersionUID = 1L;

    public ResourceTypeResource() {

    }

    public ResourceTypeResource(String resourceTypeBo, String resourceBo) {
       this.resourceTypeBo = resourceTypeBo;
       this.resourceBo = resourceBo;
       this.handle = ResourceTypeResource.genHandle(resourceTypeBo, resourceBo);
    }

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 關聯資源型別主鍵
     */
   @TableField("RESOURCE_TYPE_BO")
   private String resourceTypeBo;
    /**
     * 關聯資源主鍵
     */
   @TableField("RESOURCE_BO")
   private String resourceBo;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getResourceTypeBo() {
      return resourceTypeBo;
   }

   public void setResourceTypeBo(String resourceTypeBo) {
      this.resourceTypeBo = resourceTypeBo;
   }

   public String getResourceBo() {
      return resourceBo;
   }

   public void setResourceBo(String resourceBo) {
      this.resourceBo = resourceBo;
   }

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "ResourceTypeResource{" +
         "handle = " + handle +
         ", resourceTypeBo = " + resourceTypeBo +
         ", resourceBo = " + resourceBo +
         "}";
   }
   public static String genHandle(String resourceTypeBo, String resourceBo) {
      return StringUtils.genHandle(HandleBoConstants.RESOURCE_TYPE_RESOURCE_BO, resourceTypeBo, resourceBo);
   }
}