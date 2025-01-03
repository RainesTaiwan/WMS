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
public class PLCData extends Model<PLCData>{
    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    @TableField("PLC_ID")
    private String plcId;
    @TableField("DATA")
    private String data;
    @TableField("CREATE_USER")
    private String createUser;
    @TableField("CREATED_TIME")
    private Date createdTime;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPLCId() {
        return plcId;
    }

    public void setPLCId(String plcId) {this.plcId = plcId;}

    public String getData() {
        return data;
    }

    public void setData(String data) { this.data = data; }

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

    public static final String HANDLE = "HANDLE";

    public static final String PLC_ID = "PLC_ID";

    public static final String DATA = "DATA";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATED_TIME = "CREATED_TIME";

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }

    @Override
    public String toString() {
        return "PLC Data{" +
                "HANDLE = " + handle +
                ", PLC_ID = " + plcId +
                ", DATA = " + data +
                ", CREATE_USER = " + createUser +
                ", CREATED_TIME = " + createdTime +
                "}";
    }
}