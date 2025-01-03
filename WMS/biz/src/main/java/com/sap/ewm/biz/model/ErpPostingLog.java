package com.sap.ewm.biz.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * ERP互動日誌
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public class ErpPostingLog extends Model<ErpPostingLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
   @TableId(value = "HANDLE", type = IdType.INPUT)
   private String handle;
    /**
     * 互動名稱
     */
   @TableField("NAME")
   private String name;
    /**
     * 互動型別
     */
   @TableField("TYPE")
   private String type;
    /**
     * 資源
     */
   @TableField("RESOURCE_BO")
   private String resourceBo;
    /**
     * 描述
     */
   @TableField("KEY1")
   private String key1;
    /**
     * 資源
     */
   @TableField("KEY2")
   private String key2;
    /**
     * 資源
     */
   @TableField("KEY3")
   private String key3;
    /**
     * 請求ID
     */
   @TableField("MESSAGE_ID")
   private String messageId;
    /**
     * 互動返回程式碼
     */
   @TableField("RESULT")
   private String result;
    /**
     * 反饋資訊
     */
   @TableField("MSG")
   private String msg;
    /**
     * 請求內容
     */
   @TableField("REQUEST_BODY")
   private String requestBody;
    /**
     * 返回內容
     */
   @TableField("RESPONSE_BODY")
   private String responseBody;
    /**
     * 處理時間（毫秒）
     */
   @TableField("PROCESS_TIME")
   private String processTime;
    /**
     * 建立日期
     */
   @TableField("DATE_CREATE")
   private LocalDateTime dateCreate;
    /**
     * 建立人員
     */
   @TableField("CREATOR")
   private String creator;


   public String getHandle() {
      return handle;
   }

   public void setHandle(String handle) {
      this.handle = handle;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getResourceBo() {
      return resourceBo;
   }

   public void setResourceBo(String resourceBo) {
      this.resourceBo = resourceBo;
   }

   public String getKey1() {
      return key1;
   }

   public void setKey1(String key1) {
      this.key1 = key1;
   }

   public String getKey2() {
      return key2;
   }

   public void setKey2(String key2) {
      this.key2 = key2;
   }

   public String getKey3() {
      return key3;
   }

   public void setKey3(String key3) {
      this.key3 = key3;
   }

   public String getmessageId() {
      return messageId;
   }

   public void setmessageId(String messageId) {
      this.messageId = messageId;
   }

   public String getResult() {
      return result;
   }

   public void setResult(String result) {
      this.result = result;
   }

   public String getMsg() {
      return msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public String getRequestBody() {
      return requestBody;
   }

   public void setRequestBody(String requestBody) {
      this.requestBody = requestBody;
   }

   public String getResponseBody() {
      return responseBody;
   }

   public void setResponseBody(String responseBody) {
      this.responseBody = responseBody;
   }

   public String getProcessTime() {
      return processTime;
   }

   public void setProcessTime(String processTime) {
      this.processTime = processTime;
   }

   public LocalDateTime getDateCreate() {
      return dateCreate;
   }

   public void setDateCreate(LocalDateTime dateCreate) {
      this.dateCreate = dateCreate;
   }

   public String getCreator() {
      return creator;
   }

   public void setCreator(String creator) {
      this.creator = creator;
   }

   @Override
   protected Serializable pkVal() {
      return this.handle;
   }

   @Override
   public String toString() {
      return "ErpPostingLog{" +
         "handle = " + handle +
         ", name = " + name +
         ", type = " + type +
         ", resourceBo = " + resourceBo +
         ", key1 = " + key1 +
         ", key2 = " + key2 +
         ", key3 = " + key3 +
         ", messageId = " + messageId +
         ", result = " + result +
         ", msg = " + msg +
         ", requestBody = " + requestBody +
         ", responseBody = " + responseBody +
         ", processTime = " + processTime +
         ", dateCreate = " + dateCreate +
         ", creator = " + creator +
         "}";
   }
}