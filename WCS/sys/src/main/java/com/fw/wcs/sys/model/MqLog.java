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
public class MqLog extends Model<MqLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 訊息佇列
     */
   @TableField("QUEUE_NAME")
   private String queueName;
    /**
     * 請求ID
     */
   @TableField("MESSAGE_ID")
   private String messageId;
    /**
     * 載具編號
     */
   @TableField("CARRIER")
   private String carrier;
    /**
     * 返回結果
     */
   @TableField("RESULT")
   private String result;
    /**
     * 返回訊息
     */
   @TableField("MESSAGE")
   private String message;
    /**
     * 請求時間
     */
   @TableField("SEND_TIME")
   private Date sendTime;
    /**
     * 建立時間
     */
   @TableField("CREATED_TIME")
   private Date createdTime;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getQueueName() {
      return queueName;
   }

   public void setQueueName(String queueName) {
      this.queueName = queueName;
   }

   public String getmessageId() {
      return messageId;
   }

   public void setmessageId(String messageId) {
      this.messageId = messageId;
   }

   public String getCarrier() {
      return carrier;
   }

   public void setCarrier(String carrier) {
      this.carrier = carrier;
   }

   public String getResult() {
      return result;
   }

   public void setResult(String result) {
      this.result = result;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public Date getSendTime() {
      return sendTime;
   }

   public void setSendTime(Date sendTime) {
      this.sendTime = sendTime;
   }

   public Date getCreatedTime() {
      return createdTime;
   }

   public void setCreatedTime(Date createdTime) {
      this.createdTime = createdTime;
   }

   public static final String HANDLE = "HANDLE";

   public static final String QUEUE_NAME = "QUEUE_NAME";

   public static final String MESSAGE_ID = "MESSAGE_ID";

   public static final String CARRIER = "CARRIER";

   public static final String RESULT = "RESULT";

   public static final String MESSAGE = "MESSAGE";

   public static final String SEND_TIME = "SEND_TIME";

   public static final String CREATED_TIME = "CREATED_TIME";

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "MqLog{" +
         "handle = " + handle +
         ", queueName = " + queueName +
         ", messageId = " + messageId +
         ", carrier = " + carrier +
         ", result = " + result +
         ", message = " + message +
         ", sendTime = " + sendTime +
         ", createdTime = " + createdTime +
         "}";
   }
}