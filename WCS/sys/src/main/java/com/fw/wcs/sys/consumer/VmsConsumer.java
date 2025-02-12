package com.fw.wcs.sys.consumer;

import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.dto.VmsResponse;
import com.fw.wcs.sys.model.Agv;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.AgvAlarmService;
import com.fw.wcs.sys.service.AgvService;
import com.fw.wcs.sys.service.VmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.time.LocalDateTime;

import javax.jms.Queue;
import com.fw.wcs.core.utils.DateUtil;

/**
 * @author Leon
 *
 *
 * 監聽AGV系統-VMS 互動事件
 */
@Component
public class VmsConsumer {

    private static Logger logger = LoggerFactory.getLogger(VmsConsumer.class);

    @Autowired
    private AgvService agvService;
    @Autowired
    private VmsService vmsService;
    @Autowired
    private AgvAlarmService agvAlarmService;
    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Autowired
    @Qualifier("wmsActivemqTemplate")
    private JmsMessagingTemplate wmsActivemqTemplate;


    //AGV上報運輸任務狀態 //原TransportStateReport
    //@JmsListener(destination = "WCS-AGV-4", containerFactory = "wmrFactory")
    @JmsListener(destination = "AGV.Report.WCS", containerFactory = "wmsFactory")
    public void transportStateReport(MessageHeaders headers, String text) {
        logger.info("Get AGVReportWCS Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.AGVReportWCS);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("MQ_LOG", JsonTemp.toJSONString());

        String vehicleId = "";
        String messageId = "";
        String ackCode = "0";
        String reason = "";
        try {

            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            messageId = jsonObject.getString("MESSAGE_ID");
            String correlationId = jsonObject.getString("CORRELATION_ID");
            vehicleId = jsonObject.getString("VEHICLE_ID");
            String carrier = jsonObject.getString("CARRIER_ID");
            String taskType = jsonObject.getString("TASK_TYPE");
            String taskStatus = jsonObject.getString("TASK_STATUS");
            reason = jsonObject.getString("MSG");
            String sendTime = jsonObject.getString("SEND_TIME");

            if(taskStatus == "END") {
                JSONObject JsonTemp2 = new JSONObject();
                JsonTemp2.put("MESSAGE_ID", DateUtil.getDateTime());
                JsonTemp2.put("MESSAGE_TYPE", "conveyor.trans");
                JsonTemp2.put("RESOURCE", "Conveyor4");
                JsonTemp2.put("PALLET_ID", "ASRS_PALLET_00010");
                JsonTemp2.put("START_STATION", "CV3");
                JsonTemp2.put("END_STATION", "CV2");
                JsonTemp2.put("SEND_TIME", LocalDateTime.now().toString());
                activeMqSendService.sendMsgNoResponse4Wms("conveyor.trans", JsonTemp2.toJSONString());
                JSONObject JsonTemp3 = new JSONObject();
                JsonTemp3.put("MESSAGE_ID", DateUtil.getDateTime());
                JsonTemp3.put("MESSAGE_TYPE", "conveyor.trans");
                JsonTemp3.put("RESOURCE", "Conveyor4");
                JsonTemp3.put("PALLET_ID", "ASRS_PALLET_00010");
                JsonTemp3.put("START_STATION", "CV2");
                JsonTemp3.put("END_STATION", "CV3");
                JsonTemp3.put("SEND_TIME", LocalDateTime.now().toString());
                activeMqSendService.sendMsgNoResponse4Wms("conveyor.trans", JsonTemp3.toJSONString());
                JSONObject JsonTemp4 = new JSONObject();
                JsonTemp4.put("MESSAGE_TYPE", "Storage.Bin.To.Conveyor.Ack");
                JsonTemp4.put("CORRELATION_ID", messageId);
                JsonTemp4.put("STORAGE_BIN", "C01R07L4");
                JsonTemp4.put("RESOURCE", "Conveyor4");
                JsonTemp4.put("PALLET_ID", "ASRS_PALLET_00596");
                JsonTemp3.put("SEND_TIME", LocalDateTime.now().toString());
                activeMqSendService.sendMsgNoResponse4Wms("Storage.Bin.To.Conveyor.Ack", JsonTemp4.toJSONString());
            }
            //運輸任務狀態記錄
            //vmsService.agvTransportState(vehicleId, taskStatus, carrier);

            //運輸任務狀態記錄
            vmsService.agvTransportStateWithTaskID(correlationId, vehicleId, taskStatus, carrier);
        } catch (Exception e) {
            ackCode = "1";
            reason = e.getMessage();
        }

        //訊息回覆
        VmsResponse vmsResponse = new VmsResponse(messageId, CustomConstants.AGVReportWCSAck);
        vmsResponse.setAckCode(ackCode);
        vmsResponse.setSendTime(LocalDateTime.now().toString()); //DateUtil.getDateTimemessageId()
        logger.info("Return AGVReportWCS Text>>> {}", vmsResponse.toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.AGVReportWCSAck, JSON.toJSONString(vmsResponse));

        /*
        VmsResponse vmsResponse = new VmsResponse(MessageId);
        vmsResponse.setAckCode(ackCode);
        vmsResponse.setReason(reason);
        vmsResponse.setVehicleId(vehicleId);
        logger.info("Return AGVReportWCS Text>>> {}", vmsResponse.toString());
        wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), JSON.toJSONString(vmsResponse));
        */
    }

    //AGV 報警資訊上報
    @JmsListener(destination = "AGV.Alarm.WCS" , containerFactory = "wmsFactory")
    public void alarmReport(MessageHeaders headers, String text) {
        logger.info("Get AlarmReport Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.AGVAlarmWCS);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        String messageId = "";
        String vehicleId = "";
        String severity = "";
        String state = "true";
        String ackCode = "0";
        String reason = "";
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            messageId = jsonObject.getString("MESSAGE_ID");
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            vehicleId = jsonObject.getString("RESOURCE");
            severity = jsonObject.getString("SEVERITY");
            String alarmCode = jsonObject.getString("ALARM_TYPE");
            String description = jsonObject.getString("MSG");

            //裝置報警處理
            agvAlarmService.agvAlarm(vehicleId, severity, alarmCode, description, state);
        } catch (Exception e) {
            ackCode = "1";
            reason = e.getMessage();
            JsonTemp.put("QUEUE", "AGV.Report.WCS -e");
            JsonTemp.put("MESSAGE_BODY", reason);
            JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
        }
        //訊息回覆
        VmsResponse vmsResponse = new VmsResponse(messageId, CustomConstants.AGVAlarmWCSAck);
        vmsResponse.setAckCode(ackCode);
        vmsResponse.setSendTime(LocalDateTime.now().toString()); //DateUtil.getDateTimemessageId()
        logger.info("Return AGVAlarmWCS Text>>> {}", vmsResponse.toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.AGVAlarmWCSAck, JSON.toJSONString(vmsResponse));
        //訊息回覆
        /*
        VmsResponse vmsResponse = new VmsResponse(commandId);
        vmsResponse.setAckCode(ackCode);
        vmsResponse.setReason(reason);
        vmsResponse.setVehicleId(vehicleId);
        wmsActivemqTemplate.convertAndSend((Queue) headers.get("jms_replyTo"), JSON.toJSONString(vmsResponse));
        */
    }

    @JmsListener(destination = "Request.AGV.Ack", containerFactory = "wmsFactory")
    public void requestAGVAck(MessageHeaders headers, String text) {
        logger.info("Get RequestAGVAck Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.RequestAGVAck);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
    }

    @JmsListener(destination = "AGV.Status.WCS", containerFactory = "wmsFactory")
    public void agvStatusWCS(MessageHeaders headers, String text) {
        logger.info("Get AGVStatusToWCS Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.AGVStatusWCS);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        try {
            JSONObject jsonObject = JSON.parseObject(text);
            String messageType = jsonObject.getString("MESSAGE_TYPE");
            String agvNo = jsonObject.getString("RESOURCE");
            String mode = jsonObject.getString("MODE");
            String position = jsonObject.getString("POSITION");
            String taskId = jsonObject.getString("TASK_ID");
            String taskType = jsonObject.getString("TASK_TYPE");
            String taskStartTime = jsonObject.getString("TASK_STARTTIME");
            String capacity = jsonObject.getString("CAPACITY");
            //String temperature = jsonObject.getString("TEMPERATURE");
            String status = jsonObject.getString("STATUS");
            //String speed = jsonObject.getString("SPEED");
            //String maxLoad = jsonObject.getString("MAX_LOAD");
            String workingTime = jsonObject.getString("WORKING_TIME");
            //String msg = jsonObject.getString("MSG");
            String sendTime = jsonObject.getString("SEND_TIME");

            boolean changeStatus = false;
            String statusToAsrs = "IDLE";
            Agv agv = agvService.findAGV(agvNo);
            agv.setMode(mode);
            agv.setPosition(position);

            agv.setTaskId(taskId);
            if(taskId.length()>0)   statusToAsrs = "WORKING";

            agv.setTaskType(taskType);

            if(capacity.equals(agv.getCapacity())==false) {
                changeStatus = true;
                agv.setCapacity(capacity);
            }

            try {
                if(taskStartTime==null){
                    agv.setTaskStartTime(null);
                }else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = format.parse(taskStartTime);
                    agv.setTaskStartTime(date);
                }
            }catch (Exception e){
                JSONObject JsonE = new JSONObject();
                JsonE.put("QUEUE", "AGV.Status.WCS - date parse -e");
                JsonE.put("MESSAGE_BODY", e.getMessage());
                JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
            }

            agv.setStatus(status);
            agv.setWorkingTime(workingTime);
            agvService.updateByAgvNo(agvNo, agv);

            if(changeStatus){
                // 告知ASRS AGV狀態為WORKING
                agvService.reportASRS(agvNo, statusToAsrs);
            }

            /*

            Agv agv = new Agv();
            agv.setMode(mode);
            if("".equals(position)==false)  agv.setPosition(position);
            agv.setTaskId(taskId);
            agv.setTaskType(taskType);
            try {
                if(taskStartTime==null){
                    agv.setTaskStartTime(null);
                }else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = format.parse(taskStartTime);
                    agv.setTaskStartTime(date);
                }
            }catch (Exception e){
                JSONObject JsonE = new JSONObject();
                JsonE.put("QUEUE", "AGV.Status.WCS - date parse -e");
                JsonE.put("MESSAGE_BODY", e.getMessage());
                JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
            }
            if("".equals(capacity)==false)  agv.setCapacity(capacity);
            agv.setStatus(status);
            agv.setWorkingTime(workingTime);
            agvService.updateByAgvNo(agvNo, agv);

            */

        } catch (Exception e) {
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "AGV.Status.WCS -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
        }
    }
/*
    //模擬VMS //原TransportCommand
    @JmsListener(destination = "WCS-AGV-2", containerFactory = "wmsFactory")
    //@JmsListener(destination = "Request.AGV", containerFactory = "wmsFactory")
    public void requestAGV(MessageHeaders headers, String text) {
        logger.info("Get RequestAGV Text>>> {}", text);

        JSONObject jsonObject = JSON.parseObject(text);
        vmsService.agvTransportCommand(jsonObject);
    }
    //模擬VMS
    @JmsListener(destination = "AGV.Report.WCS.Ack", containerFactory = "wmsFactory")
    public void agvReportWCSAck(MessageHeaders headers, String text) {
        logger.info("Get AGVReportWCSAck Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.AGVReportWCSAck);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
    }
    //模擬VMS
    @JmsListener(destination = "AGV.Alarm.WCS.Ack", containerFactory = "wmsFactory")
    public void alarmReportaAck(MessageHeaders headers, String text) {
        logger.info("Get alarmReportaAck Text>>> {}", text);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", CustomConstants.AGVAlarmWCSAck);
        JsonTemp.put("MESSAGE_BODY", text);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
    }
*/
}
