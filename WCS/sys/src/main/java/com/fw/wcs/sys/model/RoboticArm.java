package com.fw.wcs.sys.model;
import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;


/**
 *
 * @author Glanz
 *
 */
public class RoboticArm extends Model<RoboticArm>{
    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    //機械手臂名稱
    @TableField("ROBOTIC_ARM")
    private String roboticArm;
    //服務的輸送帶
    @TableField("SERVICE_RESOURCE")
    private String serviceResource;
    //機械手臂狀態
    @TableField("STATUS")
    private String status;
    //建立人員
    @TableField("CREATE_USER")
    private String createUser;
    //建立時間
    @TableField("CREATED_TIME")
    private Date createdTime;
    //更新人員
    @TableField("UPDATE_USER")
    private String updateUser;
    //更新時間
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getRoboticArm() {
        return roboticArm;
    }

    public void setRoboticArm(String roboticArm) {this.roboticArm = roboticArm;}

    public String getServiceResource() {
        return serviceResource;
    }

    public void setServiceResource(String serviceResource) { this.serviceResource = serviceResource; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

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

    public static final String ROBOTIC_ARM = "ROBOTIC_ARM";

    public static final String SERVICE_RESOURCE = "SERVICE_RESOURCE";

    public static final String STATUS = "STATUS";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATED_TIME = "CREATED_TIME";

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }

    @Override
    public String toString() {
        return "Robotic Arm{" +
                "handle = " + handle +
                ", roboticArm = " + roboticArm +
                ", serviceResource = " + serviceResource +
                ", status = " + status +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }
}
