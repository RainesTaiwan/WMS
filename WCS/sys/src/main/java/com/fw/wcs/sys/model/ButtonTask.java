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

public class ButtonTask extends Model<ButtonTask>{
    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    @TableField("RECEIVE_STATION")
    private String receiveStation;
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

    public String getReceiveStation() {
        return receiveStation;
    }

    public void setReceiveStation(String receiveStation) {
        this.receiveStation = receiveStation;
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

    public static final String RECEIVE_STATION = "RECEIVE_STATION";

    public static final String TYPE = "type";

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
        return "ButtonTask{" +
                "handle = " + handle +
                ", receiveStation = " + receiveStation +
                ", type = " + type +
                ", status = " + status +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }
}
