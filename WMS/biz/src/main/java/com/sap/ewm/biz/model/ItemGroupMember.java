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
 * 物料組&物料關聯關係
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class ItemGroupMember extends Model<ItemGroupMember> {

    private static final long serialVersionUID = 1L;

    public ItemGroupMember() {

    }

    public ItemGroupMember(String itemGroupBo, String itemBo) {
       this.itemGroupBo = itemGroupBo;
       this.itemBo = itemBo;
       this.handle = ItemGroupMember.genHandle(itemGroupBo, itemBo);
    }

   /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 關聯物料組主鍵
     */
   @TableField("ITEM_GROUP_BO")
   private String itemGroupBo;
    /**
     * 關聯物料主鍵
     */
   @TableField("ITEM_BO")
   private String itemBo;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getItemGroupBo() {
      return itemGroupBo;
   }

   public void setItemGroupBo(String itemGroupBo) {
      this.itemGroupBo = itemGroupBo;
   }

   public String getItemBo() {
      return itemBo;
   }

   public void setItemBo(String itemBo) {
      this.itemBo = itemBo;
   }

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "ItemGroupMember{" +
         "handle = " + handle +
         ", itemGroupBo = " + itemGroupBo +
         ", itemBo = " + itemBo +
         "}";
   }

   private static String genHandle(String itemGroupBo, String itemBo) {
      return StringUtils.genHandle(HandleBoConstants.ITEM_GROUP_MEMBER_BO, itemGroupBo, itemBo);
   }
}