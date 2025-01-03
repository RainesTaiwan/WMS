package com.fw.wcs.sys.model;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 *
 * @author Glanz
 *
 */
public class RFIDReaderMask extends Model<RFIDReaderMask> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    @TableField("VOUCHER_NO")
    private String voucherNo;
    @TableField("RFID")
    private String rfid;
    @TableField("STATUS")//狀態：Read、UnRead
    private String status;
    @TableField("PALLET")//棧板ID
    private String pallet;
    @TableField("CREATE_USER")
    private String createUser;
    @TableField("CREATED_TIME")
    private Date createdTime;
    @TableField("UPDATE_USER")
    private String updateUser;
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getRFID() {
        return rfid;
    }

    public void setRFID(String rfid) {
        this.rfid = rfid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPallet() {
        return pallet;
    }

    public void setPallet(String pallet) {
        this.pallet = pallet;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public static final String HANDLE = "HANDLE";

    public static final String VOUCHER_NO = "VOUCHER_NO";

    public static final String RFID = "RFID";

    public static final String STATUS = "STATUS";

    public static final String PALLET = "PALLET";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATED_TIME = "CREATED_TIME";

    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String UPDATED_TIME = "UPDATED_TIME";

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }

    @Override
    public String toString() {
        return "RFIDReaderMask{" +
                "handle = " + handle +
                ", voucherNo = " + voucherNo +
                ", rfid = " + rfid +
                ", status = " + status +
                ", pallet = " + pallet +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }
}
