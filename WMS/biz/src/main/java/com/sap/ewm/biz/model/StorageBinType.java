package com.sap.ewm.biz.model;

import java.io.Serializable;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.IdType;
import com.sap.ewm.biz.constants.HandleBoConstants;
import com.sap.ewm.core.utils.StringUtils;

/**
 * <p>
 * 儲存貨格型別主數據
 * </p>
 *
 * @author syngna
 * @since 2020-07-20
 */
public class StorageBinType extends Model<StorageBinType> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 儲存貨格型別程式碼
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
    private BigDecimal width;
    /**
     * 長度
     */
    @TableField("LENGTH")
    private BigDecimal length;
    /**
     * 高度
     */
    @TableField("HEIGHT")
    private BigDecimal height;
    /**
     * 最大承重
     */
    @TableField("MAX_WEIGHT")
    private BigDecimal maxWeight;
    /**
     * 可否混料
     */
    @TableField("MIX_ITEM")
    private String mixItem;
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

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getMixItem() {
        return mixItem;
    }

    public void setMixItem(String mixItem) {
        this.mixItem = mixItem;
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
                ", length = " + length +
                ", height = " + height +
                ", maxWeight = " + maxWeight +
                ", mixItem = " + mixItem +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", creator = " + creator +
                ", updater = " + updater +
                "}";
    }

    public static String genHandle(String storageBinType) {
        return StringUtils.genHandle(HandleBoConstants.STORAGE_BIN_TYPE_BO, storageBinType);
    }
}