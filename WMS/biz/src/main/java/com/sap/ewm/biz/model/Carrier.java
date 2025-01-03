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
 * 載具主數據
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class Carrier extends Model<Carrier> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 載具
     */
    @TableField("CARRIER")
    private String carrier;
    /**
     * 狀態
     */
    @TableField("STATUS")
    private String status;
    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;
    /**
     * 關聯載具型別主鍵
     */
    @TableField("CARRIER_TYPE_BO")
    private String carrierTypeBo;
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

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCarrierTypeBo() {
        return carrierTypeBo;
    }

    public void setCarrierTypeBo(String carrierTypeBo) {
        this.carrierTypeBo = carrierTypeBo;
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
        return "Carrier{" +
                "handle = " + handle +
                ", carrier = " + carrier +
                ", description = " + description +
                ", carrierTypeBo = " + carrierTypeBo +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", creator = " + creator +
                ", updater = " + updater +
                "}";
    }

    public static String genHandle(String carrier) {
        return StringUtils.genHandle(HandleBoConstants.CARRIER_BO, carrier);
    }
}