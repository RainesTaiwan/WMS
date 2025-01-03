package com.sap.ewm.biz.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 處理單元規格記錄（處理單元過測量裝置時記錄）
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class HandlingUnitSpec extends Model<HandlingUnitSpec> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵（處理單元標識）
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * 關聯載具handle
     */
    @TableField("CARRIER_BO")
    private String carrierBo;
    /**
     * 寬度
     */
    @TableField("WIDTH")
    private BigDecimal width;
    /**
     * 高度
     */
    @TableField("HEIGHT")
    private BigDecimal height;
    /**
     * 長度
     */
    @TableField("LENGTH")
    private BigDecimal length;
    /**
     * 重量
     */
    @TableField("WEIGHT")
    private BigDecimal weight;
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

    public String getCarrierBo() {
        return carrierBo;
    }

    public void setCarrierBo(String carrierBo) {
        this.carrierBo = carrierBo;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
        return "HandlingUnitSpec{" +
                "handle = " + handle +
                ", width = " + width +
                ", height = " + height +
                ", length = " + length +
                ", weight = " + weight +
                ", createTime = " + createTime +
                ", creator = " + creator +
                "}";
    }
}