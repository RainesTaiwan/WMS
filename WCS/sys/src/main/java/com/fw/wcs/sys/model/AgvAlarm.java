package com.fw.wcs.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author Leon
 *
 */
public class AgvAlarm extends Model<AgvAlarm> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * AGV編號
     */
   @TableField("AGV_NO")
   private String agvNo;
    /**
     * 警報等級
     */
   @TableField("SEVERITY")
   private String severity;
    /**
     * 警報程式碼
     */
   @TableField("ALARM_CODE")
   private String alarmCode;
    /**
     * 警報說明
     */
   @TableField("DESCRIPTION")
   private String description;
    /**
     * 狀態
     */
   @TableField("STATUS")
   private String status;
    /**
     * 警報時間
     */
   @TableField("OCCUR_TIME")
   private Date occurTime;
    /**
     * 解除時間
     */
   @TableField("RELEASE_TIME")
   private Date releaseTime;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getAgvNo() {
      return agvNo;
   }

   public void setAgvNo(String agvNo) {
      this.agvNo = agvNo;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

   public String getAlarmCode() {
      return alarmCode;
   }

   public void setAlarmCode(String alarmCode) {
      this.alarmCode = alarmCode;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public Date getOccurTime() {
      return occurTime;
   }

   public void setOccurTime(Date occurTime) {
      this.occurTime = occurTime;
   }

   public Date getReleaseTime() {
      return releaseTime;
   }

   public void setReleaseTime(Date releaseTime) {
      this.releaseTime = releaseTime;
   }

   public static final String HANDLE = "HANDLE";

   public static final String AGV_NO = "AGV_NO";

   public static final String SEVERITY = "SEVERITY";

   public static final String ALARM_CODE = "ALARM_CODE";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String STATUS = "STATUS";

   public static final String OCCUR_TIME = "OCCUR_TIME";

   public static final String RELEASE_TIME = "RELEASE_TIME";

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "AgvAlarm{" +
         "handle = " + handle +
         ", agvNo = " + agvNo +
         ", severity = " + severity +
         ", alarmCode = " + alarmCode +
         ", description = " + description +
         ", status = " + status +
         ", occurTime = " + occurTime +
         ", releaseTime = " + releaseTime +
         "}";
   }
}