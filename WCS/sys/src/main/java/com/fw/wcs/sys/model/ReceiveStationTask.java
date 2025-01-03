package com.fw.wcs.sys.model;
import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @author Glanz
 */
public class ReceiveStationTask extends Model<ReceiveStationTask>{
    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    @TableField("MESSAGE_ID")
    private String messageId;
    @TableField("RECEIVE_STATION")
    private String receiveStation;
    @TableField("START_STATION")
    private String startStation;
    @TableField("END_STATION")
    private String endStation;
    @TableField("PALLET")
    private String pallet;
    @TableField("PALLET_INFO_CHECK")
    private String palletInfoCheck;
    @TableField("TYPE")
    private String type;
    @TableField("STATUS")
    private String status;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiveStation() {
        return receiveStation;
    }

    public void setReceiveStation(String receiveStation) {
        this.receiveStation = receiveStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getPallet() {
        return pallet;
    }

    public void setPallet(String pallet) {
        this.pallet = pallet;
    }

    public String getPalletInfoCheck() {
        return palletInfoCheck;
    }

    public void setPalletInfoCheck(String palletInfoCheck) {
        this.palletInfoCheck = palletInfoCheck;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public static final String MESSAGE_ID = "MESSAGE_ID";

    public static final String RECEIVE_STATION = "RECEIVE_STATION";

    public static final String START_STATION = "START_STATION";

    public static final String END_STATION = "END_STATION";

    public static final String PALLET = "PALLET";

    public static final String PALLET_INFO_CHECK = "PALLET_INFO_CHECK";

    public static final String TYPE = "TYPE";

    public static final String STATUS = "STATUS";

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
        return "ReceiveStationTask{" +
                "handle = " + handle +
                ", messageId = " + messageId +
                ", receiveStation = " + receiveStation +
                ", startStation = " + startStation +
                ", endStation = " + endStation +
                ", pallet = " + pallet +
                ", palletInfoCheck = " + palletInfoCheck +
                ", type = " + type +
                ", status = " + status +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }
}
