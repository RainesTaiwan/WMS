package com.fw.wcs.sys.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.model.RFIDReaderTask;
import com.fw.wcs.sys.model.ReceiveStationTask;
import com.fw.wcs.sys.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Glanz
 *
 * 監聽RFID Reader 互動事件
 */
@Component
public class RFIDConsumer {
    private static Logger logger = LoggerFactory.getLogger(RFIDConsumer.class);

    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private RFIDReaderService rfidReaderService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;
    @Autowired
    private RFIDReaderMaskService rfidReaderMaskService;
    @Autowired
    private CarrierRfidService carrierRfidService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;

    //接收RFID Reader上位機MQ - 告知開啟/關閉指定的 RFID Reader
    @JmsListener(destination = "Conveyor.Reader.Ack", containerFactory = "wmsFactory")
    public void ConveyorReaderAck(MessageHeaders headers, String text) {
        logger.info("Get Conveyor.Reader.Ack Text>>> {}", text);

        JSONObject JsonLog = new JSONObject();
        JsonLog.put("QUEUE", "Conveyor.Reader.Ack");
        JsonLog.put("MESSAGE_BODY", text);
        JsonLog.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonLog.toJSONString());

        try {
            //解析參數
            JSONObject jsonObject = JSON.parseObject(text);
            String MessageType = jsonObject.getString("MESSAGE_TYPE");
            String CorrelationId = jsonObject.getString("CORRELATION_ID");
            String AckCode = jsonObject.getString("ACK_CODE");
            String SendTime = jsonObject.getString("SEND_TIME");

            //模擬呼叫要求傳送讀取RFID清單
            // 需指定 RFID READER 的 ID
            // 若CORRELATION_ID 對應 rfid_reader_task的MessageID 的任務狀態為START且類型非機械手臂抓取
            // 則 等待一段指定時間 即 呼叫要求傳送讀取RFID清單

            RFIDReaderTask rfidReaderTask = rfidReaderTaskService.findTaskByMessageID(CorrelationId);

            if(("GetPalletID".equals(rfidReaderTask.getType()))&&(CustomConstants.NEW.equals(rfidReaderTask.getStatus()))){
                //更新狀態
                rfidReaderTask.setStatus(CustomConstants.START);
                rfidReaderTaskService.updateTaskByMessageID(rfidReaderTask.getMessageID(), rfidReaderTask);

                //預設RFID讀取時間
                try{
                    //Thread.sleep(new Long(10000));//隨意設置可更動
                    long waitingTime = CustomConstants.WAITINGRFID_GetPalletID;
                    Thread.sleep(new Long(waitingTime)); //預設10秒
                }catch (Exception e){
                    e.printStackTrace();
                }
                //呼叫要求傳送讀取RFID清單
                JSONObject jsonObject_request = new JSONObject();
                jsonObject_request.put("MessageType", "Conveyor.Tags.Request");
                jsonObject_request.put("MESSAGE_ID", CorrelationId);
                jsonObject_request.put("RESOURCE", rfidReaderTask.getReaderID()); //RFID READER的序號
                jsonObject_request.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Tags.Request", jsonObject_request.toJSONString());
            }
            else if(("RoboticArm".equals(rfidReaderTask.getType()))&&(CustomConstants.NEW.equals(rfidReaderTask.getStatus()))){
                //更新狀態
                rfidReaderTask.setStatus(CustomConstants.START);
                rfidReaderTaskService.updateTaskByMessageID(rfidReaderTask.getMessageID(), rfidReaderTask);

            }

        } catch (Exception e) {
            logger.error("Conveyor.Reader.Ack failed: {}", e.getMessage());
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Conveyor.Reader.Ack -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    //接收RFID Reader上位機MQ ~ 傳送讀取RFID清單
    @JmsListener(destination = "Conveyor.Tags.WCS", containerFactory = "wmsFactory")
    public void ConveyorTagsWCS(MessageHeaders headers, String text) {
        logger.info("Get Conveyor.Tags.WCS Text>>> {}", text);

        JSONObject JsonLog = new JSONObject();
        JsonLog.put("QUEUE", "Conveyor.Tags.WCS");
        JsonLog.put("MESSAGE_BODY", text);
        JsonLog.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonLog.toJSONString());

        try {
            //解析參數
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("CORRELATION_ID");
            String resource = jsonObject.getString("RESOURCE"); //RFID READER的序號
            String carrier = jsonObject.getString("CARRIER");
            //JSONArray carrier = jsonObject.getJSONArray("CARRIER");
            JSONArray dataList = jsonObject.getJSONArray("DATA_LIST");  //RFID Tags清單
            String alarmType = jsonObject.getString("ALARM_TYPE");
            String Msg = jsonObject.getString("MSG");
            String SendTime = jsonObject.getString("SEND_TIME");

            //設置任務狀態
            //ALARM_TYPE: 0: 無異常；1:沒讀到棧板標籤；2:讀到多個棧板標籤
            //Normal；ReaderLx_x NoPallets；ReaderLx_x MultiPallets
            String status = "";
            boolean hasAlarm = false;
            if ("0".equals(alarmType)){
                status = "COMPLETE";
                // 先確認棧板是否存在，不存在則新增於WCS 並且 同步告知WMS
                boolean chkCarrier = carrierRfidService.checkCarrier(carrier);
                if(chkCarrier){
                    carrierRfidService.insertCarrier(carrier);
                }
                // 告知WMS棧板ID
                JSONObject newPalletId = new JSONObject();
                newPalletId.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimeWithRandomNum());
                newPalletId.put("MESSAGE_TYPE", "New.Pallet.ID");
                newPalletId.put("PALLET_ID", carrier);
                newPalletId.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms("New.Pallet.ID", newPalletId.toJSONString());
            }
            else if("1".equals(alarmType)) {
                status = "NoPallet";
                hasAlarm = true;
            }
            else if("2".equals(alarmType)) {
                status = "MultiPallet";
                hasAlarm = true;
            }

            RFIDReaderTask rfidReaderTask = rfidReaderTaskService.findTaskByMessageID(messageId);
            if(rfidReaderTask!=null){
                rfidReaderTask.setStatus(status);
                rfidReaderTaskService.updateTaskByMessageID(messageId, rfidReaderTask);

                if(hasAlarm){
                    // 有錯誤，須告知WMS (未寫)

                }
                else if("GetPalletID".equals(rfidReaderTask.getType())){
                    // 透過 receiveStationTaskService 產收的 GetPalletID 才需要回傳WMS，因為WMS會詢問特定位置的棧板ID
                    // receiveStationTaskService與rfidReaderTask是會有相同的MessageID
                    List<ReceiveStationTask> list = receiveStationTaskService.findReceiveStationTaskByMessageId(rfidReaderTask.getVoucherNo());
                    ReceiveStationTask receiveStationTask = new ReceiveStationTask();
                    if( list.size()>0 ){
                        // 確認棧板任務類型為GetPalletID，且狀態為START
                        // 棧板任務狀態: NEW、START、COMPLETE、ERROR_END
                        for(int i=0;i<list.size();i++){
                            receiveStationTask = list.get(i);
                            if("GetPalletID".equals(receiveStationTask.getType()) && CustomConstants.START.equals(receiveStationTask.getStatus())){
                                // 更新輸送帶任務狀態與棧板ID
                                receiveStationTask.setPallet(carrier);
                                receiveStationTask.setStatus(CustomConstants.COMPLETE);
                                receiveStationTaskService.updateReceiveStationTaskByHandle(receiveStationTask);
                                break;
                            }
                        }// End for(int i=0;i<list.size();i++)
                    }
                }
                else if("RoboticArm".equals(rfidReaderTask.getType())){
                    // 更新RFID MASK的清單資料 狀態改為Read //狀態：Read、UnRead
                    rfidReaderMaskService.updateRFIDTags(rfidReaderTask.getVoucherNo(), dataList,carrier);
                }
            }

            //呼叫關閉RFID Reader
            JSONObject jsonObject_close = new JSONObject();
            jsonObject_close.put("MESSAGE_TYPE", "Conveyor.Reader");
            jsonObject_close.put("MESSAGE_ID", messageId);
            jsonObject_close.put("START","0");
            jsonObject_close.put("RESOURCE", resource); //RFID READER的序號
            jsonObject_close.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Reader", jsonObject_close.toJSONString());

        } catch (Exception e) {
            logger.error("RFID Reader Provide WCS With RFID Tags failed: {}", e.getMessage());
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "RFID Reader Provide WCS With RFID Tags -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    //接收RFID Reader上位機MQ - 傳送設備異常
    @JmsListener(destination = "Conveyor.Reader.Alarm.WCS", containerFactory = "wmsFactory")
    public void ConveyorReaderAlarmWCS(MessageHeaders headers, String text) {
        logger.info("Get Conveyor.Reader.Alarm.WCS Text>>> {}", text);

        JSONObject JsonLog = new JSONObject();
        JsonLog.put("QUEUE", "Conveyor.Reader.Alarm.WCS");
        JsonLog.put("MESSAGE_BODY", text);
        JsonLog.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonLog.toJSONString());

        try {
            //解析參數
            JSONObject jsonObject = JSON.parseObject(text);
            String MessageType = jsonObject.getString("MESSAGE_TYPE");
            String MessageId = jsonObject.getString("MESSAGE_ID");
            String Resource = jsonObject.getString("RESOURCE"); //RFID READER的序號
            String AlarmType = jsonObject.getString("ALARM_TYPE");
            String Msg = jsonObject.getString("MSG");
            String SendTime = jsonObject.getString("SEND_TIME");

            //訊息回覆
            JSONObject jsonObject_Ack = new JSONObject();
            jsonObject_Ack.put("MESSAGE_TYPE", "Conveyor.Reader.Alarm.WCS.Ack");
            jsonObject_Ack.put("CORRELATION_ID", MessageId);
            jsonObject_Ack.put("ACK_CODE", 0);
            jsonObject_Ack.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Reader.Alarm.WCS.Ack", jsonObject_Ack.toJSONString());

            //更動對應設備狀態
            //ALARM_TYPE: 0: RFID Offline / 1:RFID 無回應 / 99:恢復正常
            //ReaderLx_x Offline / ReaderLx_x NoReply  / ReaderLx_x Normal
            String status = "";
            if ("0".equals(AlarmType))    status = "Offline";
            else if("1".equals(AlarmType))    status = "NoReply";
            else if("99".equals(AlarmType)) status = "Online";
            rfidReaderService.updateRFIDReaderStatus(Resource, status);

            // RFID異常WMS/WCS要做什麼呢? 通知ASRS?

        } catch (Exception e) {
            logger.error("RFID Reader Alarm failed: {}", e.getMessage());
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "RFID Reader Alarm -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }
/*
    //模擬RFID Reader上位機
    //要求開啟/關閉指定的 RFID Reader
    @JmsListener(destination = "Conveyor.Reader", containerFactory = "wmsFactory")
    public void ConveyorReader(MessageHeaders headers, String text) {
        logger.info("Get ConveyorReader Text>>> {}", text);

        JSONObject JsonLog = new JSONObject();
        JsonLog.put("QUEUE", "Conveyor.Reader");
        JsonLog.put("MESSAGE_BODY", text);
        JsonLog.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("MQ_LOG", JsonLog.toJSONString());

        //解析參數
        JSONObject jsonObject = JSON.parseObject(text);
        String MessageType = jsonObject.getString("MESSAGE_TYPE");
        String MessageId = jsonObject.getString("MESSAGE_ID");
        String Resource = jsonObject.getString("RESOURCE"); //RFID READER的序號
        String START = jsonObject.getString("START");
        JSONArray DataList = jsonObject.getJSONArray("DATA_LIST");  //RFID Tags清單
        String SendTime = jsonObject.getString("SEND_TIME");

        //訊息回覆
        JSONObject jsonObject_Ack = new JSONObject();
        jsonObject_Ack.put("MESSAGE_TYPE", "Conveyor.Reader.Ack");
        jsonObject_Ack.put("CORRELATION_ID", MessageId);
        jsonObject_Ack.put("ACK_CODE", 0);
        jsonObject_Ack.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Reader.Ack", jsonObject_Ack.toJSONString());
    }

    //模擬RFID Reader上位機
    //WCS要求傳送讀取清單
    @JmsListener(destination = "Conveyor.Tags.Request", containerFactory = "wmsFactory")
    public void ConveyorTagsRequest(MessageHeaders headers, String text) {
        logger.info("Get Conveyor.Tags.Request Text>>> {}", text);

        JSONObject JsonLog = new JSONObject();
        JsonLog.put("QUEUE", "Conveyor.Tags.Request");
        JsonLog.put("MESSAGE_BODY", text);
        JsonLog.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonLog.toJSONString());

        //解析參數
        JSONObject jsonObject = JSON.parseObject(text);
        String MessageType = jsonObject.getString("MESSAGE_TYPE");
        String messageId = jsonObject.getString("MESSAGE_ID");
        String resource = jsonObject.getString("RESOURCE"); //RFID READER的序號
        String SendTime = jsonObject.getString("SEND_TIME");

        //模擬傳送清單
        //先透過MESSAGE_ID從RFID_Reader_Task取得WO_SERIAL，再使用WOSerial找到清單隨機傳送
        RFIDReaderTask rfidReaderTask = rfidReaderTaskService.findTaskByMessageID(messageId);
        rfidReaderMaskService.SimulationRFIDReaderTransTags(rfidReaderTask.getVoucherNo(), messageId, resource);
    }

    //模擬RFID Reader上位機
    //異常報告訊息ACK
    @JmsListener(destination = "Conveyor.Reader.Alarm.WCS.Ack", containerFactory = "wmsFactory")
    public void ConveyorReaderAlarmWCSAck(MessageHeaders headers, String text) {
        logger.info("Get Conveyor.Reader.Alarm.WCS.Ack Text>>> {}", text);

        JSONObject JsonLog = new JSONObject();
        JsonLog.put("QUEUE", "Conveyor.Reader.Alarm.WCS.Ack");
        JsonLog.put("MESSAGE_BODY", text);
        JsonLog.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonLog.toJSONString());

        //解析參數
        JSONObject jsonObject = JSON.parseObject(text);
        String MessageType = jsonObject.getString("MESSAGE_TYPE");
        String MessageId = jsonObject.getString("MESSAGE_ID");
        String Resource = jsonObject.getString("RESOURCE"); //RFID READER的序號
        String SendTime = jsonObject.getString("SEND_TIME");
    }
    */
}