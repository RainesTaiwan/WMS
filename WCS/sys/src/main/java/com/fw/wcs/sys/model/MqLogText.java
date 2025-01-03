package com.fw.wcs.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.sql.Blob;
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
public class MqLogText extends Model<MqLogText> {

    private static final long serialVersionUID = 1L;

    /**
     * MQBO
     */
   @TableField("MQ_LOG_BO")
   private String mqLogBo;
    /**
     * 請求文字
     */
   @TableField("REQUEST_TEXT")
   private String requestText;
    /**
     * 返回文字
     */
   @TableField("RESPONSE_TEXT")
   private String responseText;


   public String getMqLogBo() {
      return mqLogBo;
   }

   public void setMqLogBo(String mqLogBo) {
      this.mqLogBo = mqLogBo;
   }

   public String getRequestText() {
      return requestText;
   }

   public void setRequestText(String requestText) {
      this.requestText = requestText;
   }

   public String getResponseText() {
      return responseText;
   }

   public void setResponseText(String responseText) {
      this.responseText = responseText;
   }

   public static final String MQ_LOG_BO = "MQ_LOG_BO";

   public static final String REQUEST_TEXT = "REQUEST_TEXT";

   public static final String RESPONSE_TEXT = "RESPONSE_TEXT";

   @Override
   protected Serializable pkVal() {
      return this.mqLogBo;
   }

   @Override
   public String toString() {
      return "MqLogText{" +
         "mqLogBo = " + mqLogBo +
         ", requestText = " + requestText +
         ", responseText = " + responseText +
         "}";
   }
}