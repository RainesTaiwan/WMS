package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.core.utils.StringUtils;
import com.fw.wcs.sys.dto.TransportCommandDto;
import com.fw.wcs.sys.model.*;
import com.fw.wcs.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class VmsServiceImpl implements VmsService {

    @Autowired
    private WmsService wmsService;
    @Autowired
    private AgvService agvService;
    @Autowired
    private AgvTaskService agvTaskService;
    @Autowired
    private MonitoringTaskService monitoringTaskService;
    @Autowired
    private CarrierTaskService carrierTaskService;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationBindService receiveStationBindService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;

    @Override
    public void sendTransportTaskWithTaskID(String taskID, String carrier, String taskType, String startPosition, String targetPosition){
        TransportCommandDto transportCommandDto = new TransportCommandDto();
        transportCommandDto.setMESSAGE_ID(taskID);
        transportCommandDto.setVehicleId("");
        transportCommandDto.setCarrierId(carrier);
        transportCommandDto.setTaskType(taskType);
        transportCommandDto.setFromNodeNo(startPosition);
        transportCommandDto.setToNodeNo(targetPosition);
        activeMqSendService.sendMsgNoResponse4Res(CustomConstants.RequestAGV, JSON.toJSONString(transportCommandDto));

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE",  CustomConstants.RequestAGV);
        JsonTemp.put("MESSAGE_BODY", JSON.toJSONString(transportCommandDto));
        JsonTemp.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
        //TODO 回覆異常/無回覆情況
    }

    @Override
    public void agvTransportStateWithTaskID(String taskID, String agvNo, String status, String carrier) {
        //查詢AGV目前任務
        AgvTask agvTask = agvTaskService.getAgvTaskByTaskID(taskID);
        if (agvTask == null) {
            throw new BusinessException("【" +taskID+ "】的任務不存在");
        }
        String agvCarrier = agvTask.getCarrier();
        if (StringUtils.notBlank(carrier) && !agvCarrier.equals(carrier)) {
            throw new BusinessException("AGV上傳載具【"+carrier+"】與繫結任務載具【"+agvCarrier+"】不一致");
        }

        //AGV狀態變更
        Agv agv = new Agv();
        agv.setStatus(status);
        agvService.updateByAgvNo(agvNo, agv);
        //更新agvTask狀態
        agvTask.setStatus(status);
        agvTask.setUpdateUser(CustomConstants.UPDATE_USER);
        agvTask.setUpdatedTime(new Date());
        agvTaskService.updateById(agvTask);
        switch (status) {
            //任務開始
            case CustomConstants.AGV_START:
                this.agvTransportStart(agvNo, agvTask);
                break;
            //到達取貨位置
            case CustomConstants.AGV_FROM_ARRIVED:
                break;
            //離開取貨位置
            case CustomConstants.AGV_FROM_LEFT:
                this.agvTransportFromLeft(agvNo, agvTask);
                break;
            //到達卸貨站點
            case CustomConstants.AGV_TO_ARRIVED:
                break;
            //離開卸貨站點
            case CustomConstants.AGV_TO_LEFT:
                this.agvTransportToLeft(agvNo, agvTask);
                break;
            //任務結束
            case CustomConstants.AGV_END:
                this.agvTransportEnd(agvNo, agvTask);
                break;
            default: break;
        }

    }

    //AGV任務開始
    void agvTransportStart(String agvNo, AgvTask agvTask) {
        // 更新輸送帶NowTask為AGV_TRANS}
        if(CustomConstants.TYPE_IN.equals(agvTask.getCategory())){
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(agvTask.getStartPosition());
            receiveStation.setNowTask("AGV_TRANS");
            receiveStationService.updateReceiveStation(receiveStation);
        }
        else if(CustomConstants.TYPE_OUT.equals(agvTask.getCategory())){
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(agvTask.getTargetPosition());
            receiveStation.setNowTask("AGV_TRANS");
            receiveStationService.updateReceiveStation(receiveStation);
        }

        //更新agvTask的車子訊息
        agvTask.setAgvNo(agvNo);
        agvTask.setUpdateUser(CustomConstants.UPDATE_USER);
        agvTask.setUpdatedTime(new Date());
        agvTaskService.updateById(agvTask);

        // 取得agvNo，更新CarrierTask
        carrierTaskService.updateCarrierTaskAgvInfo(agvTask.getCarrier(), agvNo);

        // 告知ASRS AGV狀態為WORKING
        agvService.reportASRS(agvTask.getAgvNo(), CustomConstants.WORKING);

        // 建立啟動監控任務
        String taskID = DateUtil.getDateTimeWithRandomNum();
        String type = "0"; //0: 啟動、1: 關閉
        String resource, storageBin;
        if(CustomConstants.TYPE_IN.equals(agvTask.getCategory())){
            resource = agvTask.getStartPosition();
            storageBin = agvTask.getTargetPosition();
        }else{
            resource = agvTask.getTargetPosition();
            storageBin = agvTask.getStartPosition();
        }
        monitoringTaskService.createMonitoringTask(taskID, type, resource, storageBin);
    }

    /**
     * AGV上報離開取貨地點
     *  入庫：接駁站繫結釋放
     *  出庫：告知WMS貨格釋放
     *  &載具任務完成
     */
    void agvTransportFromLeft(String agvNo, AgvTask agvTask) {
        String startPosition = null;
        String carrier = agvTask.getCarrier();

        //任務型別
        String category = agvTask.getCategory();
        if (CustomConstants.TYPE_IN.equals(category)) {
            //入庫
            String receiveStation = agvTask.getStartPosition();
            startPosition = receiveStation;
            receiveStationBindService.receiveStationUnBind(receiveStation, carrier);
        } else {
            //出庫
            String storageBin = agvTask.getStartPosition();
            startPosition = storageBin;
            wmsService.carrierOutStorage(carrier, storageBin);
        }
        //------------------------------------------------------------------------
        carrierTaskService.updateCarrierTaskStatus(carrier, category, startPosition, CustomConstants.START);
        //------------------------------------------------------------------------
        // 建立關閉監控任務
        String taskID = DateUtil.getDateTimemessageId();
        String type = "1"; //0: 啟動、1: 關閉
        String resource, storageBin;
        if(CustomConstants.TYPE_IN.equals(agvTask.getCategory())){
            resource = agvTask.getStartPosition();
            storageBin = agvTask.getTargetPosition();
        }else{
            resource = agvTask.getTargetPosition();
            storageBin = agvTask.getStartPosition();
        }
        monitoringTaskService.createMonitoringTask(taskID, type, resource, storageBin);
    }

    /**
     * AGV上報離開卸貨站點
     *  入庫：告知WMS貨格已使用
     *  出庫：等待接駁站上報載具資訊
     */
    void agvTransportToLeft(String agvNo, AgvTask agvTask) {
        String startPosition = null;
        String carrier = agvTask.getCarrier();

        //任務型別
        String category = agvTask.getCategory();
        if (CustomConstants.TYPE_IN.equals(category)) {
            startPosition = agvTask.getStartPosition();
            String storageBin = agvTask.getTargetPosition();
            wmsService.carrierInStorage(carrier, storageBin);
            carrierTaskService.updateCarrierTaskStatus(carrier, category, startPosition, CustomConstants.COMPLETE);
        }
    }

    /**
     * AGV上報任務完成
     *  目前任務完成
     *  指派下一任務
     */
    void agvTransportEnd(String agvNo, AgvTask agvTask) {

        //任務完成
        agvTaskService.completeAgvTask(agvTask);

        //任務型別
        String category = agvTask.getCategory();
        if (CustomConstants.TYPE_OUT.equals(category)) {//出庫
            // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
            // ● 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動): CNV_TRANS

            // 找尋輸送帶   更新輸送帶TaskGoal、NowTask為空值
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(agvTask.getTargetPosition());
            if("OUT-BINtoCV3".equals(receiveStation.getTaskGoal())){
                // 回報WMS
                receiveStationTaskService.reportWMS("Storage.Bin.To.Conveyor.Ack", agvTask.getHandle()
                        , agvTask.getStartPosition(), agvTask.getTargetPosition(), agvTask.getCarrier());
                // 更新狀態
                receiveStation.setTaskGoal("");
                receiveStation.setNowTask("");
                //接駁站繫結載具
                receiveStationBindService.receiveStationBind(receiveStation.getReceiveStation(), "CV3", agvTask.getCarrier());
            }
            else if("OUT-BINtoCV2".equals(receiveStation.getTaskGoal())){
                // 建立移動任務
                receiveStationTaskService.createReceiveStationTask(agvTask.getHandle(), agvTask.getTargetPosition()
                        , agvTask.getCarrier(),"Transfer", "CV3", "CV2", false);
                // 更新狀態
                receiveStation.setNowTask("");
                //接駁站繫結載具
                receiveStationBindService.receiveStationBind(receiveStation.getReceiveStation(), "CV2", agvTask.getCarrier());
            }
            else if("OUT-BINtoCV1".equals(receiveStation.getTaskGoal())){
                // 建立移動任務
                receiveStationTaskService.createReceiveStationTask(agvTask.getHandle(), agvTask.getTargetPosition()
                        , agvTask.getCarrier(),"Transfer", "CV3", "CV1", false);
                // 更新狀態
                receiveStation.setNowTask("");
                //接駁站繫結載具
                receiveStationBindService.receiveStationBind(receiveStation.getReceiveStation(), "CV1", agvTask.getCarrier());
            }
            // 更新狀態
            receiveStationService.updateReceiveStation(receiveStation);
        }else if(CustomConstants.TYPE_IN.equals(category)) {//入庫
            // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
            // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
            // ● 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
            // 任務4 (機械手臂): ROBOTIC_ARM
            // 任務5 (無人運輸車): AGV_TRANS
            // 任務6 (輸送帶移動): CNV_TRANS
            // 找尋輸送帶   更新輸送帶TaskGoal、NowTask為空值
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(agvTask.getStartPosition());
            if("IN-CV1toBIN".equals(receiveStation.getTaskGoal()) || "IN-CV2toBIN".equals(receiveStation.getTaskGoal()) ||
               "IN-CV3toBIN".equals(receiveStation.getTaskGoal())){
                // 回報WMS
                receiveStationTaskService.reportWMS("Conveyor.To.Storage.Bin.Ack", agvTask.getHandle()
                        , agvTask.getTargetPosition(), agvTask.getStartPosition(), agvTask.getCarrier());
                // 更新狀態
                receiveStation.setTaskGoal("");
                receiveStation.setNowTask("");
                receiveStationService.updateReceiveStation(receiveStation);
            }
        }
        /*
        //------------------------------------------------------------------------
        List<CarrierTask> list = carrierTaskService.findCarrierTask(null, CustomConstants.NEW);
        if (list == null || list.size() <= 0) {
            return;
        }

        CarrierTask carrierTaskModel = list.get(0);
        String carrier = carrierTaskModel.getCarrier();
        String startPosition = carrierTaskModel.getStartPosition();
        String targetPosition = carrierTaskModel.getTargetPosition();
        //載具任務狀態修改
        carrierTaskModel.setAgvNo(agvNo);
        carrierTaskModel.setStatus(CustomConstants.START);
        carrierTaskModel.setUpdatedTime(new Date());
        carrierTaskService.updateById(carrierTaskModel);

        //AGV 任務建立
        agvTaskService.createAgvTransportTask(agvNo, carrierTaskModel.getCategory(), carrier, startPosition, targetPosition);
        */
    }

     //模擬VMS
    @Override
    public void agvTransportCommand(JSONObject jsonObject) {
        String MessageId = jsonObject.getString("MESSAGE_ID");
        String agvNo = "AGV0"+ (int)(Math.random()*2+1);  //產生1-3的數值
        //String agvNo = jsonObject.getString("VEHICLE_ID");
        String carrier = jsonObject.getString("CARRIER_ID");
        String fromNodeNo = jsonObject.getString("FROM_NODE_NO");
        String ToNodeNo = jsonObject.getString("TO_NODE_NO");
        String TaskType = jsonObject.getString("TASK_TYPE");

        //START
        JSONObject requestJson = new JSONObject();
        requestJson.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        requestJson.put("CORRELATION_ID", MessageId);
        requestJson.put("VEHICLE_ID", agvNo);
        requestJson.put("CARRIER_ID", carrier);
        requestJson.put("TASK_TYPE", TaskType);
        requestJson.put("TASK_STATUS", CustomConstants.AGV_START);
        requestJson.put("MSG", "");
        requestJson.put("SEND_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Res(CustomConstants.AGVReportWCS, JSON.toJSONString(requestJson));

        // 入庫更新輸送帶輸送帶CV3為空值
        AgvTask agvTask = agvTaskService.getAgvTaskByTaskID(MessageId);
        if(CustomConstants.TYPE_IN.equals(agvTask.getCategory())){
            try {
                long waitingTime = CustomConstants.WAITING_Processing;
                Thread.sleep(new Long(waitingTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String resource = agvTask.getStartPosition();
            ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
            receiveStation.setCv3PalletSensor("0");
            receiveStationService.updateReceiveStation(receiveStation);
        }

        try {
            long waitingTime = CustomConstants.WAITINGAGV_Simulation;
            Thread.sleep(new Long(waitingTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //FROM_LEFT
        requestJson.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        requestJson.put("TASK_STATUS", CustomConstants.AGV_FROM_LEFT);
        activeMqSendService.sendMsgNoResponse4Res(CustomConstants.AGVReportWCS, JSON.toJSONString(requestJson));

        try {
            long waitingTime = CustomConstants.WAITINGAGV_Simulation;
            Thread.sleep(new Long(waitingTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TO_LEFT
        requestJson.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        requestJson.put("TASK_STATUS", CustomConstants.AGV_TO_LEFT);
        activeMqSendService.sendMsgNoResponse4Res(CustomConstants.AGVReportWCS, JSON.toJSONString(requestJson));

        try {
            long waitingTime = CustomConstants.WAITINGAGV_Simulation;
            Thread.sleep(new Long(waitingTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //END
        requestJson.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        requestJson.put("TASK_STATUS", CustomConstants.AGV_END);
        activeMqSendService.sendMsgNoResponse4Res(CustomConstants.AGVReportWCS, JSON.toJSONString(requestJson));
    }

}
