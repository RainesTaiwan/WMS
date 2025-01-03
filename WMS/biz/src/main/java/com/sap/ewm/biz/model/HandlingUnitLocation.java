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
 * 處理單元位置繫結（記錄目前處理單元所在的貨格）
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class HandlingUnitLocation extends Model<HandlingUnitLocation> {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_BIND_TYPE = "STORAGE_BIN";

    public HandlingUnitLocation() {
        this.bindType = DEFAULT_BIND_TYPE;
    }

    public HandlingUnitLocation(String carrierBo, String handlingId, String bindContextGbo) {
        this.carrierBo = carrierBo;
        this.handlingId = handlingId;
        this.bindContextGbo = bindContextGbo;
        this.bindType = DEFAULT_BIND_TYPE;
        this.handle = genHandle(carrierBo, bindContextGbo);
    }

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 處理單元標識（同一處理單元多物料同一個ID）
     */
    @TableField("HANDLING_ID")
    private String handlingId;
    /**
     * 關聯載具主鍵
     */
    @TableField("CARRIER_BO")
    private String carrierBo;
    /**
     * 載具繫結對像主鍵（庫位/資源/接駁站）
     */
    @TableField("BIND_CONTEXT_GBO")
    private String bindContextGbo;
    /**
     * 繫結型別（庫位/資源/接駁站）(載具目前對應是在小車/貨格/接駁站上（應該只有貨格，小車&接駁站資訊在WCS中管控）)
     */
    @TableField("BIND_TYPE")
    private String bindType;
    /**
     * 建立日期
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    /**
     * 建立人員
     */
    @TableField("CREATOR")
    private String creator;


    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getHandlingId() {
        return handlingId;
    }

    public void setHandlingId(String handlingId) {
        this.handlingId = handlingId;
    }

    public String getCarrierBo() {
        return carrierBo;
    }

    public void setCarrierBo(String carrierBo) {
        this.carrierBo = carrierBo;
    }

    public String getBindContextGbo() {
        return bindContextGbo;
    }

    public void setBindContextGbo(String bindContextGbo) {
        this.bindContextGbo = bindContextGbo;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    @Override
    public String toString() {
        return "HandlingUnitLocation{" +
                "handle = " + handle +
                ", handlingId = " + handlingId +
                ", carrierBo = " + carrierBo +
                ", bindContextGbo = " + bindContextGbo +
                ", bindType = " + bindType +
                ", createTime = " + createTime +
                ", creator = " + creator +
                "}";
    }

    public static String genHandle(String carrierBo, String bindContextGbo) {
        return StringUtils.genHandle(HandleBoConstants.HANDLING_UNIT_LOCATION_BO, carrierBo, bindContextGbo);
    }
}