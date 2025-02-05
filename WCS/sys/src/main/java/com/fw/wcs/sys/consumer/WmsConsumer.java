package com.fw.wcs.sys.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.model.ReceiveStationTask;
import com.fw.wcs.sys.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.List;
import java.time.LocalDateTime;

/**
 * @author Leon
 *
 *
 * WMS下發事件監聽
 */
@Component
public class WmsConsumer {

    private static Logger logger = LoggerFactory.getLogger(WmsConsumer.class);

    @Autowired
    @Qualifier("wmsActivemqTemplate")
    private JmsMessagingTemplate wmsActivemqTemplate;
    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Autowired
    private RFIDReaderMaskService rfidReaderMaskService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;
    @Autowired
    private AgvTaskService agvTaskService;
    @Autowired
    private PressButtonService pressButtonService;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;
    @Autowired
    private CarrierTaskService carrierTaskService;
    @Autowired
    private ReceiveStationBindService receiveStationBindService;
    @Autowired
    private WmsService wmsService;
    @Autowired
    private VmsService vmsService;

    //載具出庫通知
    @JmsListener(destination = "carrier.outbound.notice.process", containerFactory="wmsFactory")
    public void carrierOutboundProcess(MessageHeaders headers, String text) {
        logger.info("Get carrier.outbound.notice.process Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        try {
            String carrier = jsonObject.getString("CARRIER");
            String storageBin = jsonObject.getString("STORAGE_BIN");
            String correlationId = jsonObject.getString("CORRELATION_ID");

            wmsService.carrierOutboundTask(carrier, storageBin, correlationId);
        } catch (Exception e) {
            logger.error("carrier outbound failed: {}", e.getMessage());
        }
        //wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), text);
    }

    //載具調倉通知
    @JmsListener(destination = "carrier.switch.notice.process", containerFactory="wmsFactory")
    public void carrierSwitchProcess(MessageHeaders headers, String text) {
        logger.info("Get carrier.outbound.notice.process Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        try {
            String carrier = jsonObject.getString("CARRIER");
            String storageBin = jsonObject.getString("STORAGE_BIN");
            String correlationId = jsonObject.getString("CORRELATION_ID");

            wmsService.carrierOutboundTask(carrier, storageBin, correlationId);
        } catch (Exception e) {
            logger.error("carrier outbound failed: {}", e.getMessage());
        }

        //wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), text);
    }

    //通知RFID清單
    @JmsListener(destination = "Provide.RFID.Tags", containerFactory="wmsFactory")
    public void ProvideRFIDTags(MessageHeaders headers, String text) {
        logger.info("Get Provide.RFID.Tags Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String voucherNo = jsonObject.getString("VOUCHER_NO"); // 憑單號
            JSONArray dataList = jsonObject.getJSONArray("DATA_LIST");  //RFID Tags清單
            String SendTime = jsonObject.getString("SEND_TIME");

            //WOSerial不能為空
            if("".equals(voucherNo)==false){
                //update RFID Tags
                rfidReaderMaskService.insertRFIDTags(voucherNo, dataList);
                /*
                //模擬呼叫建立、開啟RFID 為與RFID對皆測試 實際運行不會接收清單馬上開啟
                //須給定輸送帶、工位
                int n1 = 3;//(int)(Math.random()*5)+1; //產生1~5的數值
                //int n2 = 3;//(int)(Math.random()*3)+1; //產生1~3的數值
                String receiveStation = "Conveyor" + Integer.toString(n1);
                //String station = Integer.toString(n2);
                String type = "NotRArm";
                rfidReaderTaskService.createRFIDReaderTask(voucherNo, receiveStation, "1", type);
                //Thread.sleep(new Long(30000));
                //rfidReaderTaskService.createRFIDReaderTask(WOSerial, receiveStation, "3", type);
                 */
            }
            else{
                throw new BusinessException("MQ-Provide.RFID.Tags【" + messageId + "】的憑單號VOUCHER_NO為空");
            }

        } catch (Exception e) {
            logger.error("WMS Provide WCS With RFID Tags failed: {}", e.getMessage());
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Provide.RFID.Tags -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    //要求告知輸送帶在席情況
    @JmsListener(destination = "WMS.Request.Conveyor.Status", containerFactory="wmsFactory")
    public void wmsRequestConveyorStatus(MessageHeaders headers, String text) {
        logger.info("Get WMS.Request.Conveyor.Status Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        resultJSONObject.put("RESULT", "1");
        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String receiveStation = jsonObject.getString("RESOURCE");
            String SendTime = jsonObject.getString("SEND_TIME");

            ReceiveStation rs = receiveStationService.getReceiveStation(receiveStation);
            resultJSONObject.put("STATUS", rs.getStatus());
            if ("3".equals(rs.getCv1PalletSensor()) || "6".equals(rs.getCv1PalletSensor())){
                resultJSONObject.put("CV1_SENSOR", "Y");
            }else{
                resultJSONObject.put("CV1_SENSOR", "N");
            }
            if ("15".equals(rs.getCv2PalletSensor())){
                resultJSONObject.put("CV2_SENSOR", "Y");
            }else{
                resultJSONObject.put("CV2_SENSOR", "N");
            }
            if ("6".equals(rs.getCv3PalletSensor()) || "12".equals(rs.getCv3PalletSensor())){
                resultJSONObject.put("CV3_SENSOR", "Y");
            }else{
                resultJSONObject.put("CV3_SENSOR", "N");
            }
        } catch (Exception e) {
            logger.error("WMS.Request.Conveyor.Status failed: {}", e.getMessage());
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "WMS.Request.Conveyor.Status -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
    }

    //要求機械手臂工作
    @JmsListener(destination = "WMS.Request.Robotic.Arm", containerFactory="wmsFactory")
public void wmsRequestRoboticArmTask(MessageHeaders headers, String text) {
    long startTime = System.currentTimeMillis();
    String methodName = "wmsRequestRoboticArmTask";
    
    // 記錄開始執行的時間和收到的數據
    JSONObject logStart = new JSONObject();
    logStart.put("QUEUE", "WMS.Request.Robotic.Arm");
    logStart.put("METHOD", methodName);
    logStart.put("STATUS", "START");
    logStart.put("MESSAGE_BODY", text);
    logStart.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
    activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, logStart.toJSONString());
    
    logger.info("Get WMS.Request.Robotic.Arm Data >>> {}", text);
    
    try {
        // 解析 JSON
        JSONObject jsonObject = JSON.parseObject(text);
        
        // 記錄 JSON 解析時間
        long parseTime = System.currentTimeMillis();
        JSONObject logParse = new JSONObject();
        logParse.put("QUEUE", "WMS.Request.Robotic.Arm");
        logParse.put("METHOD", methodName);
        logParse.put("STEP", "JSON_PARSE");
        logParse.put("DURATION", parseTime - startTime);
        logParse.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, logParse.toJSONString());

        // 提取所需數據
        String messageType = jsonObject.getString("MESSAGE_TYPE");
        String messageId = jsonObject.getString("MESSAGE_ID");
        String voucherNo = jsonObject.getString("VOUCHER_NO");
        String woSerial = jsonObject.getString("WO_SERIAL");
            JSONArray woQty = jsonObject.getJSONArray("WO_QTY"); // 目標處理量
            JSONArray doQty = jsonObject.getJSONArray("DO_QTY"); // 要求處理量
            JSONArray fromPalletQty = jsonObject.getJSONArray("FROM_PALLET_QTY"); //來料棧板上數量(出庫用)
            JSONArray toPalletQty = jsonObject.getJSONArray("TO_PALLET_QTY"); //機械手臂放置物料的棧板，目前的數量
            String type = jsonObject.getString("TYPE"); //出入庫類型
            String resource = jsonObject.getString("RESOURCE"); //輸送帶ID
        String SendTime = jsonObject.getString("SEND_TIME");

        // 更新輸送帶狀態前記錄時間
        long beforeUpdateStation = System.currentTimeMillis();
        
        // 更新輸送帶目標任務
            // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
            // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // ● 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
        ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            receiveStation.setTaskGoal("ROBOTIC_ARM");
            receiveStationService.updateReceiveStation(receiveStation);
        

        // 記錄更新輸送帶狀態的時間
        long afterUpdateStation = System.currentTimeMillis();
        JSONObject logStation = new JSONObject();
        logStation.put("QUEUE", "WMS.Request.Robotic.Arm");
        logStation.put("METHOD", methodName);
        logStation.put("STEP", "UPDATE_STATION");
        logStation.put("DURATION", afterUpdateStation - beforeUpdateStation);
        logStation.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, logStation.toJSONString());

        // 建立任務前記錄時間
        long beforeCreateTask = System.currentTimeMillis();

        // 建立任務
             roboticArmTaskService.createRoboticArmTask(messageId, voucherNo, woSerial, woQty.toString(), doQty.toString()
                    , fromPalletQty.toString(), toPalletQty.toString(), resource, type);
        

        // 記錄完成時間和總執行時間
        long endTime = System.currentTimeMillis();
        JSONObject logEnd = new JSONObject();
        logEnd.put("QUEUE", "WMS.Request.Robotic.Arm");
        logEnd.put("METHOD", methodName);
        logEnd.put("STATUS", "COMPLETE");
        logEnd.put("TOTAL_DURATION", endTime - startTime);
        logEnd.put("CREATE_TASK_DURATION", endTime - beforeCreateTask);
        logEnd.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, logEnd.toJSONString());

    } catch (Exception e) {
        logger.error("WMS Provide WCS With RFID Tags failed: {}", e.getMessage());
        
        // 記錄錯誤信息
        JSONObject JsonE = new JSONObject();
        JsonE.put("QUEUE", "WMS.Request.Robotic.Arm");
        JsonE.put("METHOD", methodName);
        JsonE.put("STATUS", "ERROR");
        JsonE.put("ERROR_MESSAGE", e.getMessage());
        JsonE.put("DURATION", System.currentTimeMillis() - startTime);
        JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
        //wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), text);
}

    //要求輸送帶按鈕協助的任務
    @JmsListener(destination = "Button.Task", containerFactory="wmsFactory")
    public void askPressButton(MessageHeaders headers, String text) {
        logger.info("Get Button.Task Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String resource = jsonObject.getString("RESOURCE");
            String type = jsonObject.getString("TYPE");

            // 更新輸送帶目標任務
            // ● 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
            //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
            // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動): CNV_TRANS
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            receiveStation.setTaskGoal(type);
            receiveStationService.updateReceiveStation(receiveStation);

            // 建立並執行任務
            pressButtonService.createPressButtonTask(messageId, resource, type);

        } catch (Exception e) {
            logger.error("Button.Task failed: {}", e.getMessage());
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Button.Task -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    //要求pallet從儲位到輸送帶 【GI_SP_CNV】出庫棧板至輸送帶
    @JmsListener(destination = "Storage.Bin.To.Conveyor", containerFactory="wmsFactory")
    public void palletFromStorageBinToConveyor(MessageHeaders headers, String text) {
        logger.info("Get Storage.Bin.To.Conveyor Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", "Storage.Bin.To.Conveyor");
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String resource = jsonObject.getString("RESOURCE");  //輸送帶 ID
            String station = jsonObject.getString("STATION");  //輸送帶工位
            String pallet = jsonObject.getString("PALLET_ID"); //棧板ID
            String storageBin = jsonObject.getString("STORAGE_BIN");  //立庫庫位

            // 更新輸送帶目標任務
            // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
            // ● 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動): CNV_TRANS
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            String type = "";
            if("CV1".equals(station)) type = "OUT-BINtoCV1";
            else if("CV2".equals(station)) type = "OUT-BINtoCV2";
            else if("CV3".equals(station)) type = "OUT-BINtoCV3";
            receiveStation.setTaskGoal(type);
            receiveStationService.updateReceiveStation(receiveStation);

            //接駁站繫結載具
            receiveStationBindService.receiveStationBind(resource, station, pallet);
            //建立載具出庫任務、同時發派任務給AGV，
            carrierTaskService.createCarrierTask(pallet, CustomConstants.TYPE_OUT, storageBin, resource, messageId);
            // AGV執行完畢會直接檢查ReceiveStationBind的station，因為此任務會透過BIND程式將棧板與輸送帶結合
            // 執行完畢後要透過ACK告知WMS已完成此任務

        } catch (Exception e) {
            logger.error("Storage.Bin.To.Conveyor failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Storage.Bin.To.Conveyor -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    //要求pallet從輸送帶到儲位 【GR_SP_CNV】從輸送帶入庫棧板
    @JmsListener(destination = "Conveyor.To.Storage.Bin", containerFactory="wmsFactory")
    public void palletFromConveyorToStorageBin(MessageHeaders headers, String text) {
        logger.info("Get Conveyor.To.Storage.Bin Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);
        /*
        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", "Conveyor.To.Storage.Bin");
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", System.currentTimeMillis());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
         */
        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String resource = jsonObject.getString("RESOURCE");  //輸送帶 ID
            String station = jsonObject.getString("STATION");  //輸送帶工位
            String pallet = jsonObject.getString("PALLET_ID"); //棧板ID
            String storageBin = jsonObject.getString("STORAGE_BIN");  //立庫庫位

            // 建立並執行任務
            String type = "";
            if("CV1".equals(station)){
                type = "IN-CV1toBIN";
            }
            else if("CV2".equals(station)){
                type = "IN-CV2toBIN";
            }
            else if("CV3".equals(station)){
                type = "IN-CV3toBIN";
            }

            // 更新輸送帶目標任務
            // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
            // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // ● 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動): CNV_TRANS
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            receiveStation.setTaskGoal(type);
            receiveStationService.updateReceiveStation(receiveStation);

            // 建立並執行任務
            if("CV1".equals(station)){
                receiveStationTaskService.createReceiveStationTask(messageId, resource, pallet
                        ,"Transfer",station, "CV2", true);
            }
            else if("CV2".equals(station)){
                receiveStationTaskService.createReceiveStationTask(messageId, resource, pallet
                        ,"Transfer",station, "CV2", true);
            }
            else if("CV3".equals(station)){
                receiveStationTaskService.createReceiveStationTask(messageId, resource, pallet
                        ,"Transfer",station, "CV2", true);
            }
        } catch (Exception e) {
            logger.error("Conveyor.To.Storage.Bin failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Conveyor.To.Storage.Bin -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", System.currentTimeMillis());
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    // 要求取得指定輸送帶位置的棧板ID
    @JmsListener(destination = "Get.Pallet.ID", containerFactory="wmsFactory")
    public void getPalletID(MessageHeaders headers, String text) {
        logger.info("Get Get.Pallet.ID Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        String result = "";
        JSONObject resultJSONObject = new JSONObject();
        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String conveyor = jsonObject.getString("RESOURCE");
            String station = jsonObject.getString("STATION");

            // 建立並執行任務
            receiveStationTaskService.createReceiveStationTask(messageId, conveyor, ""
                    ,"GetPalletID",station, station, false);

            // 等待讀取棧板指定毫秒 + 500毫秒的操作時間
            try {
                long waitingTime = CustomConstants.WAITINGRFID_GetPalletID+ 500;
                Thread.sleep(new Long(waitingTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 讀取ReceiveStationTask
            List<ReceiveStationTask> list = receiveStationTaskService.findReceiveStationTaskByMessageId(messageId);
            if(list.size()>0)  resultJSONObject.put("PALLET_ID", list.get(0).getPallet());
            else  resultJSONObject.put("PALLET_ID", "");
            resultJSONObject.put("RESULT", "1");

        } catch (Exception e) {
            logger.error("Get.Pallet.ID failed: {}", e.getMessage());
            resultJSONObject.put("RESULT", "2");

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Get.Pallet.ID -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
        if (headers.get("jms_replyTo") != null) {
            result = resultJSONObject.toString();
            wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), result);
        }
    }


    /*
    @JmsListener(destination = "conveyor.trans", containerFactory="wmsFactory", concurrency = "5-10")
    public void conveyorTrans(MessageHeaders headers, String text) {
        logger.info("Get conveyor.trans Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String resource = jsonObject.getString("RESOURCE");
            String pallet = jsonObject.getString("PALLET_ID");
            String startStation = jsonObject.getString("START_STATION");
            String endStation = jsonObject.getString("END_STATION");

            ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            receiveStation.setTaskGoal("CNV_TRANS");
            receiveStationService.updateReceiveStation(receiveStation);

            // 建立並執行任務
            receiveStationTaskService.createReceiveStationTask(messageId, resource, pallet
                    ,"Transfer",startStation, endStation, false);

        } catch (Exception e) {
            logger.error("conveyor.trans failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "conveyor.trans -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }*/

    // 更新輸送帶目標任務
    // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
    // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
    // 任務4 (機械手臂): ROBOTIC_ARM
    // 任務5 (無人運輸車): AGV_TRANS
    // ● 任務6 (輸送帶移動): CNV_TRANS (不須按鈕)
    //要求pallet從輸送帶1轉移
    @JmsListener(destination = "conveyor.trans.1", containerFactory="wmsFactory", concurrency = "5-10")
    @JmsListener(destination = "conveyor.trans.2", containerFactory="wmsFactory", concurrency = "5-10")
    @JmsListener(destination = "conveyor.trans.3", containerFactory="wmsFactory", concurrency = "5-10")
    @JmsListener(destination = "conveyor.trans.4", containerFactory="wmsFactory", concurrency = "5-10")
    @JmsListener(destination = "conveyor.trans.5", containerFactory="wmsFactory", concurrency = "5-10")
    public void conveyorTrans(MessageHeaders headers, String text) {
        logger.info("Get conveyor.trans Data >>> {}", text);
        JSONObject jsonObject = JSON.parseObject(text);

        try {
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String messageId = jsonObject.getString("MESSAGE_ID");
            String resource = jsonObject.getString("RESOURCE");
            String pallet = jsonObject.getString("PALLET_ID");
            String startStation = jsonObject.getString("START_STATION");
            String endStation = jsonObject.getString("END_STATION");

            ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            receiveStation.setTaskGoal("CNV_TRANS");
            receiveStationService.updateReceiveStation(receiveStation);

            // 建立並執行任務
            receiveStationTaskService.createReceiveStationTask(messageId, resource, pallet
                    ,"Transfer",startStation, endStation, false);

        } catch (Exception e) {
            logger.error("conveyor.trans failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "conveyor.trans -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

}
