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
public class RoboticArmTask extends Model<RoboticArmTask>{
    private static final long serialVersionUID = 1L;

    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    //任務的MQID
    @TableField("MESSAGE_ID")
    private String messageID;
    //任務憑單號
    @TableField("VOUCHER_NO")
    private String voucherNo;
    //任務訂單號
    @TableField("WO_SERIAL")
    private String woSerial;
    //任務-貨物要求數量
    @TableField("WO_QTY")
    private String woQty;
    //任務-此次貨物要求數量
    @TableField("DO_QTY")
    private String doQty;
    //任務-來料棧板數量(出庫用)
    @TableField("FROM_PALLET_QTY")
    private String fromPalletQty;
    //任務-機械手臂放置物料的棧板，目前的數量
    @TableField("TO_PALLET_QTY")
    private String toPalletQty;
    //任務使用機械手臂
    @TableField("ROBOTIC_ARM")
    private String roboticArm;
    //任務使用輸送帶
    @TableField("RESOURCE")
    private String resource;
    //出入庫類型 IN (CV2->CV3)/OUT (CV3->CV2)
    @TableField("TYPE")
    private String type;
    //任務狀態
    @TableField("STATUS")
    private String status;
    //任務結果
    @TableField("RESULT")
    private String result;
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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) { this.messageID = messageID; }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {this.voucherNo = voucherNo;}

    public String getWoSerial() {
        return woSerial;
    }

    public void setWoSerial(String woSerial) { this.woSerial = woSerial; }

    public String getWoQty() {
        return woQty;
    }

    public void setWoQty(String woQty) { this.woQty = woQty; }

    public String getDoQty() {
        return doQty;
    }

    public void setDoQty(String doQty) { this.doQty = doQty; }

    public String getFromPalletQty() {
        return fromPalletQty;
    }

    public void setFromPalletQty(String fromPalletQty) { this.fromPalletQty = fromPalletQty; }

    public String getToPalletQty() {
        return toPalletQty;
    }

    public void setToPalletQty(String toPalletQty) { this.toPalletQty = toPalletQty; }

    public String getRoboticArm() {
        return roboticArm;
    }

    public void setRoboticArm(String roboticArm) { this.roboticArm = roboticArm; }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) { this.resource = resource; }

    public String getType() {
        return type;
    }

    public void setType(String type) { this.type = type; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public String getResult() {
        return result;
    }

    public void setResult(String result) { this.result = result; }

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

    public static final String VOUCHER_NO = "VOUCHER_NO";

    public static final String WO_SERIAL = "WO_SERIAL";

    public static final String WO_QTY = "WO_QTY";

    public static final String DO_QTY = "DO_QTY";

    public static final String FROM_PALLET_QTY = "FROM_PALLET_QTY";

    public static final String TO_PALLET_QTY = "TO_PALLET_QTY";

    public static final String ROBOTIC_ARM = "ROBOTIC_ARM";

    public static final String RESOURCE = "RESOURCE";

    public static final String TYPE = "TYPE";

    public static final String STATUS = "STATUS";

    public static final String RESULT = "RESULT";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATED_TIME = "CREATED_TIME";

    @Override
    protected Serializable pkVal() {
        return this.handle;
    }

    @Override
    public String toString() {
        return "Robotic Arm Task{" +
                "handle = " + handle +
                ", messageID = " + messageID +
                ", voucherNo = " + voucherNo +
                ", woSerial = " + woSerial +
                ", woQty = " + woQty +
                ", doQty = " + doQty +
                ", fromPalletQty = " + fromPalletQty +
                ", toPalletQty = " + toPalletQty +
                ", roboticArm = " + roboticArm +
                ", resource = " + resource +
                ", type = " + type +
                ", status = " + status +
                ", result = " + result +
                ", createUser = " + createUser +
                ", createdTime = " + createdTime +
                ", updateUser = " + updateUser +
                ", updatedTime = " + updatedTime +
                "}";
    }
}
