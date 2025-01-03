package com.fw.wcs.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @author Glanz
 *
 */

public class MonitoringTask extends Model<MonitoringTask>{

    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    @TableField("RESOURCE") //輸送帶編號
    private String resource;
    @TableField("STORAGE_BIN") //儲位
    private String storageBin;
    @TableField("TYPE") //0: 啟動、1: 關閉
    private String type;
    @TableField("STATUS") //0: 被監控Accepted, 1: 被監控以other reason拒絕, 999:尚未接收到監控回覆
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
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

    public static final String RESOURCE = "RESOURCE";

    public static final String STORAGE_BIN = "STORAGE_BIN";

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
        return "MonitoringTask{" +
                "handle = " + handle +
                ", resource = " + resource +
                ", storageBin = " + storageBin +
                ", type = " + type +
                ", status = " + status +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }

}
