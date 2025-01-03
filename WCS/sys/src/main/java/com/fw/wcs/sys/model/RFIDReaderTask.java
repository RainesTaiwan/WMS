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
public class RFIDReaderTask extends Model<RFIDReaderTask> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    @TableField("VOUCHER_NO")
    private String voucherNo;
    @TableField("MESSAGE_ID")
    private String messageID;
    @TableField("READER_ID")
    private String readerID;
    @TableField("STATUS")
    private String status;
    @TableField("TYPE")
    private String type;
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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) { this.messageID = messageID; }

    public String getReaderID() {
        return readerID;
    }

    public void setReaderID(String readerID) {
        this.readerID = readerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public static final String WO_SERIAL = "WO_SERIAL";

    public static final String MESSAGE_ID = "MESSAGE_ID";

    public static final String READER_ID = "RFID_READER_ID";

    public static final String STATUS = "STATUS";

    public static final String TYPE = "TYPE";

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
        return "RFIDReaderTask{" +
                "handle = " + handle +
                ", voucherNo = " + voucherNo +
                ", messageID = " + messageID +
                ", readerID = " + readerID +
                ", status = " + status +
                ", type = " + type +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }
}
