package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;


/**
 * Conveyor主數據
 *
 * @author Glanz
 * @since 2021-08-10
 */
public class Conveyor  extends Model<Conveyor>{
    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "HANDLE", type = IdType.INPUT)
    private String handle;
    /**
     * Conveyor代號
     */
    @TableField("CONVEYOR")
    private String conveyor;
    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;
    /**
     * 優先選擇權(1~5)
     */
    @TableField("PRIORITY")
    private int priority;
    /**
     * 工作量
     */
    @TableField("WORKLOAD")
    private int workload;
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

    public String getConveyor() {
        return conveyor;
    }

    public void setConveyor(String conveyor) {
        this.conveyor = conveyor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
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
        return "Conveyor{" +
                "handle = " + handle +
                ", conveyor = " + conveyor +
                ", description = " + description +
                ", priority = " + priority +
                ", workload = " + workload +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", creator = " + creator +
                ", updater = " + updater +
                "}";
    }
}
