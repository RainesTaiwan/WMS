package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;


/**
 * ASRS訂單物料主數據
 *
 * @author Glanz
 * @since 2021-04-19
 */

public class AsrsRfid extends Model<AsrsRfid> {

private static final long serialVersionUID = 1L;

/**
 * RFID
 */
@TableId(value = "HANDLE", type = IdType.INPUT)
private String handle;
/**
 * 訂單主鍵
 */
@TableField("ASRS_Order_BO")
private String asrsOrderBo;
/**
 * 憑單號
 */
@TableField("VOUCHER_NO")
private String voucherNo;
/**
 * 處理單元標識（同一處理單元多物料同一個ID）
 */
@TableField("HANDLING_ID")
private String handlingId;
/**
* 關聯載具主鍵
 */
@TableField("CARRIER")
private String carrier;
 /**
 * 狀態：Processing、OnPallet、BindPallet、OutStation
 */
@TableField("STATUS")
private String status;
/**
 * 單位
 */
@TableField("MEASURE_UNIT")
private String measureUnit;
/**
 * 量
 */
@TableField("NET_WEIGHT")
private String netWeight;
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

public String getAsrsOrderBo() {
        return asrsOrderBo;
        }

public void setAsrsOrderBo(String asrsOrderBo) {
        this.asrsOrderBo = asrsOrderBo;
        }

public String getHandlingId() {
        return handlingId;
        }

public void setHandlingId(String handlingId) {
        this.handlingId = handlingId;
        }

public String getVoucherNo() {
        return voucherNo;
        }

public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
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

public String getMeasureUnit() {
        return measureUnit;
        }

public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
        }

public String getNetWeight() {
        return netWeight;
        }

public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
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
        return "RFID{" +
        "rfid = " + handle +
        ", asrsOrderBo = " + asrsOrderBo +
        ", voucherNo = " + voucherNo +
        ", handlingId = " + handlingId +
        ", carrier = " + carrier +
        ", status = " + status +
        ", measureUnit = " + measureUnit +
        ", netWeight = " + netWeight +
        ", createTime = " + createTime +
        ", updateTime = " + updateTime +
        ", creator = " + creator +
        ", updater = " + updater +
        "}";
        }

}

