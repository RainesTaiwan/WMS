package com.fw.wcs.sys.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.dto.CommonResponse;
import com.fw.wcs.sys.model.*;
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
import java.util.Arrays;
import com.fw.wcs.core.utils.DateUtil;

import java.util.Date;
import java.time.LocalDateTime;

/**
 * @author Glanz
 * 監聽 機械手臂 互動事件
 */
@Component
public class RoboticArmConsumer {
    private static Logger logger = LoggerFactory.getLogger(RoboticArmConsumer.class);

    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private RoboticArmService roboticArmService;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private RFIDReaderService rfidReaderService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;
    @Autowired
    private ReceiveStationService receiveStationService;

    @JmsListener(destination = "Request.Robotic.Arm.Ack", containerFactory = "wmsFactory")
    public void roboticArmRequestAck(MessageHeaders headers, String text) {
        logger.info("Get Request.Robotic.Arm.Ack Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.RequestRobotArmACK);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        String correlationId = "";
        String ackCode = "";
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            correlationId = jsonObject.getString("CORRELATION_ID");
            ackCode = jsonObject.getString("ACK_CODE");
            String sendTime = jsonObject.getString("SEND_TIME");

            // 更新機械手臂指定任務狀態為START
            roboticArmTaskService.updateRoboticArmTask(correlationId, CustomConstants.START, "");

        } catch (Exception e) {
            logger.error("Request.Robotic.Arm.Ack failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Request.Robotic.Arm.Ack -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }

    @JmsListener(destination = "Robotic.Arm.Report.WCS", containerFactory = "wmsFactory")
    public void roboticArmReport(MessageHeaders headers, String text) {
        logger.info("Get Robotic.Arm.Report.WCS Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.RobotArmReportWCS);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        String correlationId = "";
        String ackCode = "";
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            correlationId = jsonObject.getString("CORRELATION_ID");
            JSONArray qty = jsonObject.getJSONArray("QTY"); // 處理量
            String result = jsonObject.getString("RESULT");
            String sendTime = jsonObject.getString("SEND_TIME");

            // 找到機械手臂指定任務
            RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(correlationId);
            // 若任務 RESULT 0: 任務完成、1: 專用儲籃裝滿但任務未完成、2: 物料拿取完畢但任務未完成、
            //              3: 發生異常(斷線、抓取物料失敗、機械手臂視覺故障、沒有感測到專用儲籃、任務錯誤、其他)
            roboticArmTask.setResult("0");
            roboticArmTaskService.updateRoboticArmTask(roboticArmTask);

            // 告知ASRS RoboticArm狀態
            roboticArmService.reportASRS(roboticArmTask.getRoboticArm(), result);

            if("0".equals(result));
            else if("1".equals(result)) {
                // 由於散件不是確定數量，所以需要想辦法紀錄 已抓完數量 告知WMS更換儲藍
                roboticArmTask.setDoQty(qty.toString());
                roboticArmTaskService.updateRoboticArmTask(roboticArmTask);
            }
            else if(CustomConstants.TYPE_IN.equals(roboticArmTask.getType()) && "2".equals(result)){
                // 更新roboticArmTask，並新增機械手臂任務
                // 用於更新的
                int[] qtyUpdate = new int[qty.size()];
                // 確認結果數量
                int[] qtyResult = new int[qty.size()];
                for (int i = 0; i < qty.size(); i++){
                    qtyResult[i] = Integer.parseInt(qty.getString(i));
                }
                // 確認原任務要求數量
                int pointer = 0; //用於確認是哪個值要處理
                int[] qtyOriginal = new int[qty.size()];
                String dataArray_DOQTY = "{\"QTY\":"+roboticArmTask.getDoQty()+"}";
                JSONObject JsonNewData = JSON.parseObject(dataArray_DOQTY);
                JSONArray data = JsonNewData.getJSONArray("QTY");
                for (int i = 0; i < qty.size(); i++){
                    qtyOriginal[i] = Integer.parseInt(data.getString(i));
                    qtyUpdate[i] = qtyOriginal[i] - qtyResult[i];
                }
                // 更新目標棧板的數量
                int[] toQtyOriginal = new int[qty.size()];
                String dataArray_TOQTY = "{\"QTY\":"+roboticArmTask.getToPalletQty()+"}";
                JsonNewData = JSON.parseObject(dataArray_TOQTY);
                data = JsonNewData.getJSONArray("QTY");
                for (int i = 0; i < qty.size(); i++){
                    toQtyOriginal[i] = Integer.parseInt(data.getString(i));
                    toQtyOriginal[i] = toQtyOriginal[i] + qtyResult[i];
                }
                roboticArmTaskService.createRoboticArmTask(roboticArmTask.getMessageID(), roboticArmTask.getVoucherNo()
                        , roboticArmTask.getWoSerial(), roboticArmTask.getWoQty(), Arrays.toString(qtyUpdate)
                        , roboticArmTask.getFromPalletQty(), Arrays.toString(toQtyOriginal)
                        , roboticArmTask.getResource(), roboticArmTask.getType());
            }
            else{
                JSONObject JsonStatus = new JSONObject();
                JsonStatus.put("MESSAGE_TYPE", "Device.Status.ASRS");
                JsonStatus.put("RESOURCE", roboticArmTask.getRoboticArm());
                JsonStatus.put("WO_SERIAL", "");
                JsonStatus.put("VOUCHER_NO", "");
                JsonStatus.put("CAPACITY", "");
                JsonStatus.put("STATUS", "2"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
                JsonStatus.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms("Device.Status.ASRS", JsonStatus.toString());

                JSONObject JsonE = new JSONObject();
                JsonE.put("QUEUE", "Device.Status.ASRS - RoboticArm");
                JsonE.put("MESSAGE_BODY", JsonStatus.toString());
                JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toString());
            }

            //將棧板從輸送帶撤回CV1
            String handle = DateUtil.getDateTimeWithRandomNum();
            receiveStationTaskService.createReceiveStationTask(handle, roboticArmTask.getResource(), "", "Transfer"
                    , "CV2", "CV1", false);
        } catch (Exception e) {
            ackCode = "1";
            logger.error("Robotic.Arm.Report.WCS failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Robotic.Arm.Report.WCS -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }

        //訊息回覆
        CommonResponse response = new CommonResponse(correlationId, CustomConstants.RobotArmReportWCSACK);
        response.setAckCode(ackCode);
        response.setSendTime(LocalDateTime.now().toString()); //DateUtil.getDateTimemessageId()
        logger.info("Return RoboticArmReport Text>>> {}", response.toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.RobotArmReportWCSACK, JSON.toJSONString(response));
    }

    @JmsListener(destination = "Robotic.Arm.Status.WCS", containerFactory = "wmsFactory")
    public void roboticArmStatusWCS(MessageHeaders headers, String text) {
        logger.info("Get Robotic.Arm.Status.WCS Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.RoboticArmStatusWCS);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String resource = jsonObject.getString("RESOURCE");
            String mode = jsonObject.getString("MODE");
            String taskId = jsonObject.getString("TASK_ID");
            String taskType = jsonObject.getString("TASK_TYPE");
            String taskStartTime = jsonObject.getString("TASK_STARTTIME");
            String status = jsonObject.getString("STATUS");
            String workingTime = jsonObject.getString("WORKING_TIME");
            String msg = jsonObject.getString("MSG");
            String sendTime = jsonObject.getString("SEND_TIME");
        } catch (Exception e) {
            logger.error("Robotic.Arm.Status.WCS failed: {}", e.getMessage());

            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "Robotic.Arm.Status.WCS -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }
/*
    //模擬RoboticArm
    @JmsListener(destination = "Robotic.Arm.Report.WCS.Ack", containerFactory = "wmsFactory")
    public void roboticArmReportAck(MessageHeaders headers, String text) {
        logger.info("Get RoboticArmRequestAck Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.RobotArmReportWCSACK);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
    }
    //模擬RoboticArm
    @JmsListener(destination = "Request.Robotic.Arm", containerFactory = "wmsFactory")
    public void roboticArmRequest(MessageHeaders headers, String text) {
        logger.info("Get Request.Robotic.Arm Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.RequestRobotArm);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        String messageId = "";
        String ackCode = "";
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            messageId = jsonObject.getString("MESSAGE_ID");
            String woQty = jsonObject.getString("WO_QTY");
            String doQty = jsonObject.getString("DO_QTY");
            String fromPalletQty = jsonObject.getString("FROM_PALLET_QTY");
            String toPalletQty = jsonObject.getString("TO_PALLET_QTY");
            String resource = jsonObject.getString("RESOURCE");
            String upperResource = jsonObject.getString("UPPER_RESOURCE");
            String type = jsonObject.getString("TYPE");
            String sendTime = jsonObject.getString("SEND_TIME");
        } catch (Exception e) {
            ackCode = "1";
            logger.error("Request.Robotic.Arm failed: {}", e.getMessage());

            JsonTemp.put("QUEUE", "Request.Robotic.Arm -e");
            JsonTemp.put("MESSAGE_BODY", e.getMessage());
            JsonTemp.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
        }

        //訊息回覆
        CommonResponse response = new CommonResponse(messageId, CustomConstants.RequestRobotArmACK);
        response.setAckCode(ackCode);
        response.setSendTime(DateUtil.getDateGMT8Time());
        logger.info("Return RoboticArmRequest Text>>> {}", response.toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.RequestRobotArmACK, JSON.toJSONString(response));

        //預設處理時間
        try{
            long waitingTime = CustomConstants.WAITING_Processing;
            Thread.sleep(new Long(waitingTime));
        }catch (Exception e){
            e.printStackTrace();
        }

        //模擬呼叫 Robotic.Arm.Report.WCS
        // type有"ALL"、"PART"表示回傳數量要全部或部分
        roboticArmTaskService.reportTasktoWCS(messageId, "ALL");
    }
*/
}
