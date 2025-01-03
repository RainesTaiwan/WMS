package com.sap.ewm.biz.consumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.*;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.biz.mapper.*;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.sys.service.MessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Component
public class EwmComsumer {

    private final Logger logger = LoggerFactory.getLogger(EwmComsumer.class);

    @Autowired
    HandlingUnitService handlingUnitService;

    @Autowired
    MaterialRequisitionService materialRequisitionService;

    @Autowired
    JmsMessagingTemplate jmsTemplate;

    @Autowired
    MessageSendService messageSendService;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CarrierService carrierService;
    @Autowired
    private CarrierTypeService carrierTypeService;
    @Autowired
    private CarrierTaskService carrierTaskService;
    @Autowired
    private ASRSOrderService asrsOrderService;
    @Autowired
    private ShelfOffLogService shelfOffLogService;
    @Autowired
    private CarrierMapper carrierMapper;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private ASRSFRIDService asrsFRIDService;
    @Autowired
    private ReportASRSService reportASRSService;


    /**
     * 監聽ASRS獲取處理資訊
     */

    @JmsListener(destination = "ASRS.Request")
    public void ASRSRequest(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[ASRS.Request]，接收到資訊: {}", text);

        int alarmType = 0;
        String messageType = "";
        String messageId = "";
        String woSerial = "";
        String woType = "";
        String alarmMsg = "";

        boolean verifierResult;

        try {
            JSONObject obj = JSONObject.parseObject(text);
            messageType = obj.getString("MESSAGE_TYPE");
            messageId = obj.getString("MESSAGE_ID");
            woSerial = obj.getString("WO_SERIAL");
            woType = obj.getString("WO_TYPE");

            if((messageType==null)||(messageId==null)||(woSerial==null)||(woType==null)){
                alarmType = 1;
                alarmMsg = asrsOrderService.asrsOrderAlarmMessage(alarmType, "MESSAGE_TYPE/MESSAGE_ID/WO_SERIAL/WO_TYPE");
            }
            else if( woType.equals(CommonConstants.OrderType1) || woType.equals(CommonConstants.OrderType3)
                    || woType.equals(CommonConstants.OrderType5)) {
                // 驗證，若合法會直接存入資料存在Inventory資料庫
                verifierResult = asrsOrderService.inStorageOrderVerifier(text);
                // 驗證後才能執行工單
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType1))) {
                    asrsOrderService.asrsOrderInStorageBox(messageId);
                }
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType3))){
                    asrsOrderService.asrsOrderInStoragePart(messageId);
                }
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType5))){
                    asrsOrderService.asrsOrderInStorageBox(messageId);
                }
            }
            else if( woType.equals(CommonConstants.OrderType4) || woType.equals(CommonConstants.OrderType6)) {
                verifierResult = asrsOrderService.outStorageOrderVerifier(text);
                // 驗證後才能執行工單
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType4))){
                    asrsOrderService.asrsOrderOutStoragePart(messageId);
                }
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType6))){
                    asrsOrderService.asrsOrderOutStorageBox(messageId);
                }
            }
            else if( woType.equals(CommonConstants.OrderType2) || woType.equals(CommonConstants.OrderType7)) {
                verifierResult = asrsOrderService.checkOrderVerifier(text);
                // 驗證後才能執行工單
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType2))){
                    asrsOrderService.asrsOrderOutStoragePallet(messageId);
                }
                if((verifierResult)&&(woType.equals(CommonConstants.OrderType7))){
                    asrsOrderService.asrsOrderCheckInventory(messageId);
                }
            }
            else{
                alarmType = 2;
                alarmMsg = asrsOrderService.asrsOrderAlarmMessage(alarmType, "WO_TYPE");
            }
        } catch (Exception e) {
            logger.error("Error:", e);
            alarmType = 1;
            alarmMsg = asrsOrderService.asrsOrderAlarmMessage(alarmType, e.getMessage());
        }

        if(alarmType>0){
            JSONObject alarmJSON = new JSONObject();
            alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
            alarmJSON.put("CORRELATION_ID", messageId);
            alarmJSON.put("WO_SERIAL", woSerial);
            alarmJSON.put("VOUCHER_NO", "");
            alarmJSON.put("ALARM_TYPE", alarmType);
            alarmJSON.put("MSG", alarmMsg);
            alarmJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);
        }//end if(alarmType>0)

        JSONObject ackJSON = new JSONObject();
        ackJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAck);
        ackJSON.put("CORRELATION_ID", messageId);
        ackJSON.put("ACK_CODE", "0");
        ackJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        messageSendService.send(CommonConstants.ASRS_RequestAck, ackJSON);
    }

    /**
     * 更新棧板ID
     */
    @JmsListener(destination = "New.Pallet.ID")
    public void newPalletID(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[New.Pallet.ID]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("PALLET_ID");

            //確認棧板是否存在，存在則不給新增棧板
            boolean chkCarrier = carrierService.checkCarrier(carrier);
            if(chkCarrier){
                carrierService.insertCarrier(carrier, "CarrierType001");
            }
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * 收取RFID清單
     */
    @JmsListener(destination = "Get.RFID.Tags")
    public void getRFIDTags(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Get.RFID.Tags]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String voucherNo = obj.getString("VOUCHER_NO");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("PALLET");
            JSONArray dataList = obj.getJSONArray("DATA_LIST");

            if(dataList.size()>0){
                List<String> list = new ArrayList<>();
                for(int i=0; i<dataList.size();i++){
                    list.add(dataList.getString(i));
                }

                AsrsRfid asrsRfid = asrsFRIDService.findRFIDByID(dataList.getString(0));
                // RFID狀態：Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                if(asrsRfid.getCarrier()==null || "".equals(asrsRfid.getCarrier())){
                    //更新RFID狀態為OnPallet
                    asrsFRIDService.updateRFIDStatus(list, carrier, null, CommonConstants.STATUS_ON_PALLET);
                }
                else{
                    asrsFRIDService.updateRFIDStatus(list, carrier, voucherNo, CommonConstants.STATUS_WAIT_OUT_STATION);
                }
            }
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * 監聽WCS 發送完成棧板移動要求conveyor.trans要求
     */
    @JmsListener(destination = "conveyor.trans.Ack")
    public void conveyorTransAck(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[conveyor.trans.Ack]，接收到資訊: {}", text);
        JSONObject JsonTemp1 = new JSONObject();
        JsonTemp1.put("QUEUE", "conveyor.trans.Ack");
        JsonTemp1.put("MESSAGE_BODY",text);
        JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        messageSendService.send("MQ_LOG", JsonTemp1);

        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("CORRELATION_ID");
            String storageBin = obj.getString("STORAGE_BIN");
            String resource = obj.getString("RESOURCE");
            String carrier = obj.getString("PALLET_ID");
            String sendTime = obj.getString("SEND_TIME");

            // 查詢原任務
            CarrierTask carrierTask = carrierTaskService.findCarrierTaskByHandle(messageId);
            // 更新任務結果
            carrierTask.setStorageBin(storageBin);
            carrierTask.setStatus(CommonConstants.STATUS_END); // 任務狀態STATUS: NEW、START、END
            carrierTask.setResult("1");
            carrierTaskService.saveOrUpdate(carrierTask);

            // 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
            //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
            // 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // ● 任務6 (輸送帶移動conveyor.trans): CNV_TRANS

            // 透過原任務確認是否有其他要執行
            if("".equals(carrierTask.getRArmTaskId()) || carrierTask.getRArmTaskId()==null){
                boolean result = carrierTaskService.deployCarrierTask(null, carrierTask.getWoserial());
                if(result==false){// 任務執行完畢須告知WMS
                }
            }
            else{
                boolean result = carrierTaskService.deployCarrierTask(carrierTask.getRArmTaskId(), carrierTask.getWoserial());
                if(result==false){
                    result = roboticArmTaskService.deployRoboticArmTask(carrierTask.getWoserial());
                    if(result==false){// 任務執行完畢須告知WMS
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * 監聽WCS 發送完成棧板入庫Conveyor.To.Storage.Bin要求
     */
    @JmsListener(destination = "Conveyor.To.Storage.Bin.Ack")
    public void conveyorToStorageBinAck(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Conveyor.To.Storage.Bin.Ack]，接收到資訊: {}", text);
        JSONObject JsonTemp1 = new JSONObject();
        JsonTemp1.put("QUEUE", "Conveyor.To.Storage.Bin.Ack");
        JsonTemp1.put("MESSAGE_BODY",text);
        JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        messageSendService.send(CommonConstants.MQ_LOG, JsonTemp1);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("CORRELATION_ID");
            String storageBin = obj.getString("STORAGE_BIN");
            String resource = obj.getString("RESOURCE");
            String carrier = obj.getString("PALLET_ID");
            String sendTime = obj.getString("SEND_TIME");

            // 查詢原任務
            CarrierTask carrierTask = carrierTaskService.findCarrierTaskByHandle(messageId);

            // 更新任務結果
            carrierTask.setStorageBin(storageBin);
            carrierTask.setStatus(CommonConstants.STATUS_END); // 任務狀態STATUS: NEW、START、END
            carrierTask.setResult("1");
            carrierTaskService.saveOrUpdate(carrierTask);

            // 更新RFID狀態 STATUS: Processing、OnPallet、BindPallet、InStorage、OutStorage、UbeOutStorage、OutStation
            List<AsrsRfid> asrsRfids = asrsFRIDService.findRFIDByVoucherNoWithStatus(carrierTask.getVoucherNo(), CommonConstants.STATUS_BIND_PALLET);
            List<String> list = new ArrayList<>();
            for(int i=0; i<asrsRfids.size();i++){
                list.add(asrsRfids.get(i).getHandle());
            }
            asrsFRIDService.updateRFIDStatus(list, carrier, null, CommonConstants.STATUS_IN_STORAGE);

            // 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
            //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
            // 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // ● 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動conveyor.trans): CNV_TRANS

            /*
            // 找尋ASRS的任務ID
            List<AsrsOrder> asrsOrderList = asrsOrderService.findAsrsOrderByWoSerial(carrierTask.getWoserial());
            AsrsOrder asrsOrder = asrsOrderList.get(0);

            // 告知ASRS棧板入庫
            JSONObject reportASRSInStorage = new JSONObject();
            reportASRSInStorage.put("CORRELATION_ID", asrsOrder.getMessageId());
            reportASRSInStorage.put("MESSAGE_TYPE", "Info.Report.ASRS");
            reportASRSInStorage.put("RESOURCE", resource);
            reportASRSInStorage.put("CARRIER", carrier);
            reportASRSInStorage.put("STORAGE_BIN", storageBin);
            reportASRSInStorage.put("TYPE", "1"); //0: 儲位清空(棧板出庫)、1: 棧板入庫
            reportASRSInStorage.put("MSG", "");   //處理資訊
            reportASRSInStorage.put("SEND_TIME", System.currentTimeMillis());
            messageSendService.send("Info.Report.ASRS", reportASRSInStorage);
             */

            // 透過原任務確認是否有其他要執行
            if("".equals(carrierTask.getRArmTaskId()) || carrierTask.getRArmTaskId()==null){
                boolean result = carrierTaskService.deployCarrierTask(null, carrierTask.getWoserial());
                if(result==false){// 工單執行完畢
                    // 結束工單
                    asrsOrderService.completeASRSOrder(carrierTask.getWoserial());
                    reportASRSService.reportASRS(carrierTask.getWoserial());
                }
            }
            else{
                boolean result = carrierTaskService.deployCarrierTask(carrierTask.getRArmTaskId(), carrierTask.getWoserial());
                if(result==false){
                    result = roboticArmTaskService.deployRoboticArmTask(carrierTask.getWoserial());
                    if(result==false){// 工單執行完畢
                        // 結束工單
                        asrsOrderService.completeASRSOrder(carrierTask.getWoserial());
                        reportASRSService.reportASRS(carrierTask.getWoserial());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * 監聽WCS 發送完成棧板出庫要求Storage.Bin.To.Conveyor要求
     */
    @JmsListener(destination = "Storage.Bin.To.Conveyor.Ack")
    public void storageBinToConveyorAck(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Storage.Bin.To.Conveyor.Ack]，接收到資訊: {}", text);
        JSONObject JsonTemp1 = new JSONObject();
        JsonTemp1.put("QUEUE", "Storage.Bin.To.Conveyor.Ack");
        JsonTemp1.put("MESSAGE_BODY",text);
        JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        messageSendService.send(CommonConstants.MQ_LOG, JsonTemp1);

        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("CORRELATION_ID");
            String storageBin = obj.getString("STORAGE_BIN");
            String resource = obj.getString("RESOURCE");
            String carrier = obj.getString("PALLET_ID");
            String sendTime = obj.getString("SEND_TIME");

            // 查詢原任務
            CarrierTask carrierTask = carrierTaskService.findCarrierTaskByHandle(messageId);
            // 更新任務結果
            carrierTask.setStorageBin(storageBin);
            carrierTask.setStatus(CommonConstants.STATUS_END); // 任務狀態STATUS: NEW、START、END
            carrierTask.setResult("1");
            carrierTaskService.saveOrUpdate(carrierTask);

            // 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
            //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
            // ● 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動conveyor.trans): CNV_TRANS

            // 找尋ASRS的任務ID
            List<AsrsOrder> asrsOrderList = asrsOrderService.findAsrsOrderByWoSerial(carrierTask.getWoserial());
            AsrsOrder asrsOrder = asrsOrderList.get(0);
            /*
            // 告知ASRS棧板出庫
            JSONObject reportASRSInStorage = new JSONObject();
            reportASRSInStorage.put("CORRELATION_ID", asrsOrder.getMessageId());
            reportASRSInStorage.put("MESSAGE_TYPE", "Info.Report.ASRS");
            reportASRSInStorage.put("RESOURCE", resource);
            reportASRSInStorage.put("CARRIER", carrier);
            reportASRSInStorage.put("STORAGE_BIN", storageBin);
            reportASRSInStorage.put("TYPE", "0"); //0: 儲位清空(棧板出庫)、1: 棧板入庫
            reportASRSInStorage.put("MSG", "");   //處理資訊
            reportASRSInStorage.put("SEND_TIME", System.currentTimeMillis());
            messageSendService.send("Info.Report.ASRS", reportASRSInStorage);
             */

            // 透過原任務確認是否有其他要執行
            if("".equals(carrierTask.getRArmTaskId()) || carrierTask.getRArmTaskId()==null){
                boolean result = carrierTaskService.deployCarrierTask(null, carrierTask.getWoserial());
                if(result==false){// 任務執行完畢須告知WMS
                    // 結束工單
                    asrsOrderService.completeASRSOrder(carrierTask.getWoserial());
                    reportASRSService.reportASRS(carrierTask.getWoserial());
                }
            }
            else{
                boolean result = carrierTaskService.deployCarrierTask(carrierTask.getRArmTaskId(), carrierTask.getWoserial());
                if(result==false){
                    result = roboticArmTaskService.deployRoboticArmTask(carrierTask.getWoserial());
                    if(result==false){// 任務執行完畢須告知WMS
                        // 結束工單
                        asrsOrderService.completeASRSOrder(carrierTask.getWoserial());
                        reportASRSService.reportASRS(carrierTask.getWoserial());
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * 監聽WCS 發送完成按鈕任務要求Button.Task.Report要求
     */
    @JmsListener(destination = "Button.Task.Report")
    public void buttonTaskReport(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Button.Task.Report]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("CORRELATION_ID");
            String taskType = obj.getString("TYPE");
            String resultMsg = obj.getString("RESULT");
            String sendTime = obj.getString("SEND_TIME");

            // 查詢原任務
            CarrierTask carrierTask = carrierTaskService.findCarrierTaskByHandle(messageId);
            carrierTask.setStatus(CommonConstants.STATUS_END); // 任務狀態STATUS: NEW、START、END
            carrierTask.setResult("1");
            carrierTaskService.saveOrUpdate(carrierTask);

            // ● 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
            //                               BasketOutPallet、PutMaterialInBasket、CheckInventory
            // 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動conveyor.trans): CNV_TRANS

            if("OutStation".equals(taskType)){
                materialRequisitionService.outStation(CommonConstants.WCS_USER, carrierTask.getCarrier(), carrierTask.getRArmTaskId());
                // 查詢原機械手臂任務
                RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByHandle(carrierTask.getRArmTaskId());
                JSONObject JsonTemp = new JSONObject();
                JsonTemp.put("QUEUE", "Button.Task.Report 0");
                JsonTemp.put("MESSAGE_BODY", roboticArmTask.toString());
                JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                messageSendService.send(CommonConstants.MQ_LOG, JsonTemp);

                // 查詢 shelfOffLog 若 SPILT為Y則需要重新入庫棧板
                ShelfOffLog shelfOffLog = shelfOffLogService.findShelfOffLogByMessageId(carrierTask.getRArmTaskId());
                if(shelfOffLog==null  || "N".equals(shelfOffLog.getSplit()));
                else{

                    // RFID狀態 STATUS: Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                    List<AsrsRfid> asrsRfids = asrsFRIDService.findRFIDByHandlingIDWithStatus(roboticArmTask.getVoucherNo(), CommonConstants.STATUS_OUT_STATION_NO_REPORT);
                    String remainRFIDHandlingID = asrsFRIDService.findHandlingIDForRemainRFID(asrsRfids.get(0).getHandlingId());

                    JsonTemp.put("QUEUE", "Button.Task.Report 1");
                    JsonTemp.put("MESSAGE_BODY", "remainRFIDHandlingID: "+ remainRFIDHandlingID);
                    JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                    messageSendService.send(CommonConstants.MQ_LOG, JsonTemp);

                    List<AsrsRfid> asrsRfids_remain = asrsFRIDService.findRFIDByHandlingID(remainRFIDHandlingID);

                    JsonTemp.put("QUEUE", "Button.Task.Report 2");
                    JsonTemp.put("MESSAGE_BODY", "asrsRfids_remain.size(): "+ asrsRfids_remain.size());
                    JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                    messageSendService.send(CommonConstants.MQ_LOG, JsonTemp);

                    BigDecimal summaryQty = BigDecimal.valueOf(asrsRfids_remain.size());
                    String voucherNo = asrsRfids_remain.get(0).getVoucherNo();

                    JSONObject JsonTemp1 = new JSONObject();
                    JsonTemp1.put("QUEUE", "Button.Task.Report");
                    JsonTemp1.put("MESSAGE_BODY", "QTY: "+ asrsRfids_remain.size() + ", summaryQty:" +summaryQty);
                    JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                    messageSendService.send(CommonConstants.MQ_LOG, JsonTemp1);

                    // 綁定
                    CarrierBindDTO carrierBindDTO = new CarrierBindDTO();
                    carrierBindDTO.setBatchNo(voucherNo);
                    carrierBindDTO.setCarrier(roboticArmTask.getCarrier());
                    carrierBindDTO.setStatus("AVAILABLE");
                    carrierBindDTO.setQty(summaryQty);
                    handlingUnitService.carrierBind(CommonConstants.CREATE_USER, carrierBindDTO);

                    String handlingId = handlingUnitService.getHandlingId(roboticArmTask.getCarrier());

                    //更新RFID狀態為BindPallet
                    //狀態：Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                    List<String> list = new ArrayList<>();
                    for(int i=0; i<asrsRfids_remain.size();i++){
                        list.add(asrsRfids_remain.get(i).getHandle());
                    }
                    asrsFRIDService.updateRFIDStatus(list, roboticArmTask.getCarrier(), handlingId, CommonConstants.STATUS_BIND_PALLET);

                    // 要求建立 入庫要求的 CarrierTask "IN-CV3toBIN"
                    carrierTaskService.insertCarrierTask("", voucherNo, roboticArmTask.getWoserial()
                            , roboticArmTask.getHandle(), roboticArmTask.getCarrier(), roboticArmTask.getResource()
                            , "CV3", "CV3", roboticArmTask.getStorageBin(), "IN-CV3toBIN", "1");
                    boolean doCarrierTaskService = carrierTaskService.deployCarrierTask(roboticArmTask.getHandle(), roboticArmTask.getWoserial());
                }
            }

            // 透過原任務確認是否有其他要執行
            if("".equals(carrierTask.getRArmTaskId()) || carrierTask.getRArmTaskId()==null){
                boolean result = carrierTaskService.deployCarrierTask(null, carrierTask.getWoserial());
                if(result==false){// 任務執行完畢須告知WMS
                    // 結束工單
                    asrsOrderService.completeASRSOrder(carrierTask.getWoserial());
                    reportASRSService.reportASRS(carrierTask.getWoserial());
                }
            }
            else{
                boolean result = carrierTaskService.deployCarrierTask(carrierTask.getRArmTaskId(), carrierTask.getWoserial());
                if(result==false){
                    result = roboticArmTaskService.deployRoboticArmTask(carrierTask.getWoserial());
                    if(result==false){// 任務執行完畢須告知WMS
                        // 結束工單
                        asrsOrderService.completeASRSOrder(carrierTask.getWoserial());
                        reportASRSService.reportASRS(carrierTask.getWoserial());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }

    /**
     * 監聽WCS 發送完成機械手臂要求Robotic.Arm.Report.WMS要求
     */
    @JmsListener(destination = "Robotic.Arm.Report.WMS")
    public void roboticArmReportWMS(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[robotic.Arm.Report.WMS]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("CORRELATION_ID");
            JSONArray qty = obj.getJSONArray("QTY");
            String result = obj.getString("RESULT"); // 目前只有"專用儲籃裝滿但任務未完成"(1)與"任務完成"(0) 會回報
            String jsonMsg = obj.getString("MSG");
            String sendTime = obj.getString("SEND_TIME");

            // 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
            //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
            // 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // ● 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動conveyor.trans): CNV_TRANS

            // 查詢原任務
            RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByHandle(messageId);
            JSONObject JsonTemp = new JSONObject();

            if("0".equals(result)){
                roboticArmTask.setResult(result);
                roboticArmTask.setStatus(CommonConstants.STATUS_END);// 狀態有 NEW、START、END
                roboticArmTaskService.saveOrUpdate(roboticArmTask);
            }
            else if("1".equals(result)){
                // 更新做的數量
                roboticArmTask.setDoQty(qty.toString());
                roboticArmTask.setResult(result);
                roboticArmTask.setStatus(CommonConstants.STATUS_END);// 狀態有 NEW、START、END
                roboticArmTaskService.saveOrUpdate(roboticArmTask);

                // 更新TaskOrder
                String taskOrder = roboticArmTask.getTaskOrder();
                int taskOrderInt = Integer.parseInt(taskOrder);
                roboticArmTask.setTaskOrder(String.valueOf(taskOrderInt +1));

                // 確認數量
                int[] qtyOriginal= new int[qty.size()];
                int[] qtyResult = new int[qty.size()];
                int[] qtyNew = new int[qty.size()];
                String dataArray_DOQTY = "{\"DOQTY\":"+roboticArmTask.getDoQty()+"}";
                JSONObject JsonNewData = JSON.parseObject(dataArray_DOQTY);
                JSONArray originalQty = JsonNewData.getJSONArray("DOQTY");
                for(int i=0; i<qty.size();i++){
                    qtyOriginal[i] = Integer.parseInt(originalQty.getString(i));;
                    qtyResult[i] = Integer.parseInt(qty.getString(i));
                    qtyNew[i] = qtyOriginal[i] - qtyResult[i];
                }
                roboticArmTask.setDoQty(Arrays.toString(qtyNew));
                roboticArmTask.setHandle(DateUtil.getDateTimeWithRandomNum());//"WCS_"+ System.currentTimeMillis()
                roboticArmTaskService.insertRoboticArmTask(roboticArmTask);
            }

            // 在每次要執行機械手臂任務時，才發送RFID清單給WCS(TYPE: ADD)；機械手臂任務結束後，馬上刪除位於WCS的RFID清單(TYPE: DELETE)
            asrsFRIDService.sentRFIDDataToWCS(roboticArmTask.getVoucherNo(), CommonConstants.Operation_DELETE
                    , roboticArmTask.getType(), roboticArmTask.getCarrier());

            //預設RFID在機械手臂處理完畢後處理時間
            try{
                long waitingTime = CommonConstants.WAITING_Processing;
                Thread.sleep(new Long(waitingTime));
            }catch (Exception e){
                e.printStackTrace();
            }

            if(CommonConstants.Type_IN.equals(roboticArmTask.getType())){
                // RFID狀態 STATUS: Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                List<AsrsRfid> asrsRfids = asrsFRIDService.findRFIDByVoucherNoWithStatus(roboticArmTask.getVoucherNo(), CommonConstants.STATUS_ON_PALLET);

                // 確認數量達到目標數量，入庫是單一物料
                // 確認入庫數量與RFID一致
                int[] qtyResult= new int[qty.size()];
                int getQty = 0;
                boolean sameQty = true;
                for(int i=0; i<qty.size();i++){
                    qtyResult[i] = Integer.parseInt(qty.getString(i));
                    if(qtyResult[i]>0)   getQty = qtyResult[i];
                    if(qtyResult[i]>0 && qtyResult[i]!=asrsRfids.size())   sameQty = false;
                }

                BigDecimal summaryQty = BigDecimal.valueOf(getQty);
                String carrier = asrsRfids.get(0).getCarrier();// 從RFID清單中確認輸送帶CV3上的棧板ID

                // 綁定
                CarrierBindDTO carrierBindDTO = new CarrierBindDTO();
                carrierBindDTO.setBatchNo(roboticArmTask.getVoucherNo());
                carrierBindDTO.setCarrier(carrier);
                carrierBindDTO.setStatus("AVAILABLE");
                carrierBindDTO.setQty(summaryQty);
                handlingUnitService.carrierBind(CommonConstants.CREATE_USER, carrierBindDTO);

                String handlingId = handlingUnitService.getHandlingId(carrier);

                //更新RFID狀態為BindPallet
                //狀態：Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                List<String> list = new ArrayList<>();
                for(int i=0; i<asrsRfids.size();i++){
                    list.add(asrsRfids.get(i).getHandle());
                }
                asrsFRIDService.updateRFIDStatus(list, carrier, handlingId, CommonConstants.STATUS_BIND_PALLET);

                // 紀錄HU與carrier、Woserial之間的關係
                reportASRSService.saveReportASRS(carrier, roboticArmTask.getWoserial(), handlingId);

                // 要求建立 入庫要求的 CarrierTask "IN-CV3toBIN"
                carrierTaskService.insertCarrierTask("", roboticArmTask.getVoucherNo(), roboticArmTask.getWoserial()
                        , roboticArmTask.getHandle(), carrier, roboticArmTask.getResource()
                        , "CV3", "CV3", "", "IN-CV3toBIN", "1");
                boolean doCarrierTaskService = carrierTaskService.deployCarrierTask(roboticArmTask.getHandle(), roboticArmTask.getWoserial());
            }
            else if(CommonConstants.Type_OUT.equals(roboticArmTask.getType())){
                // RFID狀態 STATUS: Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                List<AsrsRfid> asrsRfids = asrsFRIDService.findRFIDByHandlingIDWithStatus(roboticArmTask.getVoucherNo(), CommonConstants.STATUS_WAIT_OUT_STATION);

                // 確認數量達到目標數量
                int[] qtyResult= new int[qty.size()];
                int getQty = 0;
                boolean sameQty = true;
                for(int i=0; i<qty.size();i++){
                    qtyResult[i] = Integer.parseInt(qty.getString(i));
                    if(qtyResult[i]>0)   getQty = qtyResult[i];
                    if(qtyResult[i]>0 && qtyResult[i]!=asrsRfids.size())   sameQty = false;
                }

                // 要求建立 入庫要求的 CarrierTask "OutStation"
                carrierTaskService.insertCarrierTask("", roboticArmTask.getVoucherNo(), roboticArmTask.getWoserial()
                        , roboticArmTask.getHandle(), roboticArmTask.getCarrier(), roboticArmTask.getResource()
                        , "", "", "", "OutStation", "1");
                boolean doCarrierTaskService = carrierTaskService.deployCarrierTask(roboticArmTask.getHandle(), roboticArmTask.getWoserial());
/*
                // 查詢 shelfOffLog 若 SPILT為Y則需要重新入庫棧板
                ShelfOffLog shelfOffLog = shelfOffLogService.findShelfOffLogByMessageId(roboticArmTask.getHandle());
                if(shelfOffLog==null  || "N".equals(shelfOffLog.getSplit())){
                    // 要求建立 入庫要求的 CarrierTask "OutStation"
                    carrierTaskService.insertCarrierTask("", roboticArmTask.getVoucherNo(), roboticArmTask.getWoserial()
                            , roboticArmTask.getHandle(), roboticArmTask.getCarrier(), roboticArmTask.getResource()
                            , "", "", "", "OutStation", "1");
                }
                else{
                    // RFID狀態 STATUS: Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                    String remainRFIDHandlingID = asrsFRIDService.findHandlingIDForRemainRFID(asrsRfids.get(0).getHandlingId());
                    List<AsrsRfid> asrsRfids_remain = asrsFRIDService.findRFIDByHandlingID(remainRFIDHandlingID);

                    BigDecimal summaryQty = BigDecimal.valueOf(asrsRfids_remain.size());
                    String voucherNo = asrsRfids_remain.get(0).getVoucherNo();

                    JSONObject JsonTemp1 = new JSONObject();
                    JsonTemp1.put("QUEUE", "roboticArmReportWMS");
                    JsonTemp1.put("MESSAGE_BODY", "QTY: "+ asrsRfids_remain.size() + ", summaryQty:" +summaryQty);
                    JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                    messageSendService.send(CommonConstants.MQ_LOG, JsonTemp1);

                    // 綁定
                    CarrierBindDTO carrierBindDTO = new CarrierBindDTO();
                    carrierBindDTO.setBatchNo(voucherNo);
                    carrierBindDTO.setCarrier(roboticArmTask.getCarrier());
                    carrierBindDTO.setStatus("AVAILABLE");
                    carrierBindDTO.setQty(summaryQty);
                    handlingUnitService.carrierBind(CommonConstants.CREATE_USER, carrierBindDTO);

                    String handlingId = handlingUnitService.getHandlingId(roboticArmTask.getCarrier());

                    //更新RFID狀態為BindPallet
                    //狀態：Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION_NO_REPORT、OUT_STATION
                    List<String> list = new ArrayList<>();
                    for(int i=0; i<asrsRfids_remain.size();i++){
                        list.add(asrsRfids_remain.get(i).getHandle());
                    }
                    asrsFRIDService.updateRFIDStatus(list, roboticArmTask.getCarrier(), handlingId, CommonConstants.STATUS_BIND_PALLET);

                    // 要求建立 入庫要求的 CarrierTask "IN-CV3toBIN"
                    carrierTaskService.insertCarrierTask("", voucherNo, roboticArmTask.getWoserial()
                            , roboticArmTask.getHandle(), roboticArmTask.getCarrier(), roboticArmTask.getResource()
                            , "CV3", "CV3", roboticArmTask.getStorageBin(), "IN-CV3toBIN", "1");
                    // 要求建立 入庫要求的 CarrierTask "OutStation"
                    carrierTaskService.insertCarrierTask("", voucherNo, roboticArmTask.getWoserial()
                            , roboticArmTask.getHandle(), roboticArmTask.getCarrier(), roboticArmTask.getResource()
                            , "", "", roboticArmTask.getStorageBin(), "OutStation", "2");
                }*/
            }
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }
/*
    //監聽ASRS 發送完成Info.Report.ASRS要求
    @JmsListener(destination = "Info.Report.ASRS.Ack")
    public void infoReportASRSAck(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Info.Report.ASRS.Ack]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String correlationId = obj.getString("CORRELATION_ID");
            String ack = obj.getString("ACK_CODE");
            String sendTime = obj.getString("SEND_TIME");
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }
*/
    /**
     * 監聽ASRS 發送完成WO.Result要求
     */
    @JmsListener(destination = "WO.Result.Ack")
    public void woResultAck(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[WO.Result.Ack]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String correlationId = obj.getString("CORRELATION_ID");
            String ack = obj.getString("ACK_CODE");
            String sendTime = obj.getString("SEND_TIME");
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }


    /**
     * 監聽WCS獲取處理單元規格資訊
     *
     * @param text
     */
    @JmsListener(destination = "handling.unit.info.process")
    public String handlingUnitInfoProcess(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[handling.unit.info.process]，接收到資訊: {}", text);
        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        resultJSONObject.put("MESSAGE", "OK");
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("CARRIER");
            String length = obj.getString("LENGTH");
            String width = obj.getString("WIDTH");
            String height = obj.getString("HEIGHT");
            String weight = obj.getString("WEIGHT");
            resultJSONObject.put("MESSAGE_ID", messageId);
            List<String> busyStorageLocationList = (List<String>) obj.get("BUSY_STORAGE_LOCATION_LIST");
            WcsHandlingUnitSpecDTO wcsHandlingUnitSpecDTO = new WcsHandlingUnitSpecDTO(carrier, new BigDecimal(width), new BigDecimal(length), new BigDecimal(height), new BigDecimal(weight), busyStorageLocationList);

            // TODO 發送貨格資訊給WCS, 未找到貨格如何處理？
            handlingUnitService.handlingUnitInfoProcess(CommonConstants.WCS_USER, wcsHandlingUnitSpecDTO);
        } catch (Exception e) {
            logger.error("Error:", e);
            resultJSONObject.put("RESULT", "2");
            resultJSONObject.put("MESSAGE", StrUtil.isBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            jmsTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
        return result;
    }

    /**
     * 監聽WCS入庫請求資訊
     *
     * @param text
     */
    @JmsListener(destination = "handling.unit.in.request")
    public String handlingUnitInRequest(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[handling.unit.in.request]，接收到資訊: {}", text);
        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        resultJSONObject.put("MESSAGE", "OK");
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("CARRIER");
            resultJSONObject.put("MESSAGE_ID", messageId);
            List<String> busyStorageLocationList = (List<String>) obj.get("BUSY_STORAGE_LOCATION_LIST");
            // 發送貨格資訊給WCS, 未找到貨格如何處理
            StorageBin storageBin = handlingUnitService.handlingUnitInRequest(carrier, busyStorageLocationList);
            resultJSONObject.put("STORAGE_BIN", storageBin == null ? "" : storageBin.getStorageBin());

            logger.info("OptimalStorageBin: {}", storageBin == null ? "" : storageBin.getStorageBin());
        } catch (Exception e) {
            logger.error("Error:", e);
            resultJSONObject.put("RESULT", "2");
            resultJSONObject.put("MESSAGE", StrUtil.isBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            jmsTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
        return result;
    }

    /**
     * 處理單元放入貨格WCS通知EWM資訊
     *
     * @param text
     */
    @JmsListener(destination = "handling.unit.in.storage")
    public String inStorage(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[handling.unit.in.storage]，接收到資訊: {}", text);
        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        resultJSONObject.put("MESSAGE", "OK");
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("CARRIER");
            String storageBin = obj.getString("STORAGE_BIN");
            resultJSONObject.put("MESSAGE_ID", messageId);
            handlingUnitService.inStorage(CommonConstants.WCS_USER, carrier, storageBin);
            logger.info("handling.unit.in.storage success: {} -> {}", carrier, storageBin);
        } catch (Exception e) {
            logger.error("Error:", e);
            resultJSONObject.put("RESULT", "2");
            resultJSONObject.put("MESSAGE", StrUtil.isBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            jmsTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
        return result;
    }

    /**
     * 處理單元出貨格WCS通知EWM資訊
     *
     * @param text
     */
    @JmsListener(destination = "handling.unit.out.storage")
    public String outStorage(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[handling.unit.out.storage]，接收到資訊: {}", text);
        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        resultJSONObject.put("MESSAGE", "OK");
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String correlationId = obj.getString("CORRELATION_ID");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("CARRIER");
            String storageBin = obj.getString("STORAGE_BIN");
            resultJSONObject.put("MESSAGE_ID", messageId);
            materialRequisitionService.outStorage(CommonConstants.WCS_USER, carrier, storageBin, correlationId);
            logger.info("handling.unit.out.storage success: {} -> {}", carrier, storageBin);
        } catch (Exception e) {
            logger.error("Error:", e);
            resultJSONObject.put("RESULT", "2");
            resultJSONObject.put("MESSAGE", StrUtil.isBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            jmsTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
        return result;
    }

    /**
     * 出接駁站監聽
     *
     * @param text
     */
    @JmsListener(destination = "handling.unit.out.station")
    public String outReceiveStation(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[handling.unit.out.station]，接收到資訊: {}", text);
        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        resultJSONObject.put("MESSAGE", "OK");
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String messageId = obj.getString("MESSAGE_ID");
            String action = obj.getString("ACTION");
            String correlationId = obj.getString("CORRELATION_ID");
            String sendTime = obj.getString("SEND_TIME");
            String carrier = obj.getString("CARRIER");

            resultJSONObject.put("MESSAGE_ID", messageId);
            if ("MR".equalsIgnoreCase(action)) {
                materialRequisitionService.outStation(CommonConstants.WCS_USER, carrier, correlationId);
            }
            logger.info("handling.unit.out.station success: {}", carrier);
            resultJSONObject.put("MESSAGE_BODY", text);
        } catch (Exception e) {
            logger.error("Error:", e);
            resultJSONObject.put("RESULT", "2");
            resultJSONObject.put("MESSAGE", StrUtil.isBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            jmsTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
        return result;
    }


    // 模擬ASRS
    @JmsListener(destination = "ASRS.Request.Ack")
    public void asrsRequestAck(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[ASRS.Request.Ack]，接收到資訊: {}", text);
    }
    // 模擬ASRS
    @JmsListener(destination = "Conveyor.Location.ASRS")
    public void conveyorLocationASRS(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Conveyor.Location.ASRS]，接收到資訊: {}", text);
    }

/*
    // 模擬ASRS
    @JmsListener(destination = "Info.Report.ASRS")
    public void infoReportASRS(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Info.Report.ASRS]，接收到資訊: {}", text);
        try {
            JSONObject obj = JSONObject.parseObject(text);
            String messageType = obj.getString("MESSAGE_TYPE");
            String correlationId = obj.getString("CORRELATION_ID");
            String resource = obj.getString("RESOURCE");
            String carrier = obj.getString("CARRIER");
            String storageBin = obj.getString("STORAGE_BIN");
            String type = obj.getString("TYPE");
            String msgInfo = obj.getString("MSG");
            String sendTime = obj.getString("SEND_TIME");

            JSONObject JSONAck = new JSONObject();
            JSONAck.put("MESSAGE_TYPE", "Info.Report.ASRS.Ack");
            JSONAck.put("CORRELATION_ID", correlationId);
            JSONAck.put("ACK_CODE", "0");
            JSONAck.put("SEND_TIME", System.currentTimeMillis());
            messageSendService.send("Info.Report.ASRS.Ack", JSONAck);
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }
*/

    // 模擬ASRS
    // Device.Status.ASRS
    @JmsListener(destination = "Device.Status.ASRS")
    public void deviceStatusASRS(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[Device.Status.ASRS]，接收到資訊: {}", text);
    }

    // 模擬ASRS
    // WO.Result
    @JmsListener(destination = "WO.Result")
    public void woResult(String text, Message msg, MessageHeaders headers) {
        logger.info("=======訊息佇列[WO.Result]，接收到資訊: {}", text);
    }

}