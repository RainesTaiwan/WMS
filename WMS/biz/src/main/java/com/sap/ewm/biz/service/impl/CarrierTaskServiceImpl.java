package com.sap.ewm.biz.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.mapper.CarrierTaskMapper;
import com.sap.ewm.biz.model.AsrsOrder;
import com.sap.ewm.biz.model.CarrierTask;
import com.sap.ewm.biz.model.RoboticArmTask;
import com.sap.ewm.biz.service.ASRSOrderService;
import com.sap.ewm.biz.service.CarrierTaskService;
import com.sap.ewm.biz.service.ReportASRSService;
import com.sap.ewm.biz.service.RoboticArmTaskService;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import com.sap.ewm.core.utils.DateUtil;

/**
 * 棧板任務主數據 服務實現類
 *
 * @author Glanz
 * @since 2020-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarrierTaskServiceImpl  extends ServiceImpl<CarrierTaskMapper, CarrierTask> implements CarrierTaskService {
    @Autowired
    MessageSendService messageSendService;
    @Autowired
    CarrierTaskMapper carrierTaskMapper;
    @Autowired
    RoboticArmTaskService roboticArmTaskService;
    @Autowired
    ASRSOrderService asrsOrderService;
    @Autowired
    CarrierTaskService carrierTaskService;
    @Autowired
    ReportASRSService reportASRSService;

    // 依照RoboticArmTask任務類型對輸送帶檢查棧板位置 (需透過woSerial找RoboticArmTask)
    // 若沒有需要新增CarrierTask

    // 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
    //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
    // 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    // 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
    // 任務4 (機械手臂): ROBOTIC_ARM
    // 任務5 (無人運輸車): AGV_TRANS
    // 任務6 (輸送帶移動conveyor.trans): CNV_TRANS
    @Override
    public CarrierTask checkCarrierForRoboticArmTask(String rArmTaskId){
        CarrierTask carrierTaskForReturn = new CarrierTask();
        // 找到要檢查的機械手臂任務
        RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByHandle(rArmTaskId);
        List<AsrsOrder> asrsOrderList = asrsOrderService.findAsrsOrderByWoSerial(roboticArmTask.getWoserial());

        // 向WCS詢問輸送帶的在席SENSOR
        JSONObject JsonTask = new JSONObject();
        JsonTask.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());//System.currentTimeMillis());
        JsonTask.put("MESSAGE_TYPE", CommonConstants.Request_Conveyor_Status);
        JsonTask.put("RESOURCE", roboticArmTask.getResource());
        JsonTask.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        JSONObject resultJson = messageSendService.sendAndReceive(CommonConstants.Request_Conveyor_Status, JsonTask);

        // 棧板在表示Y，不在表示N
        String sensorCV1 = resultJson.getString("CV1_SENSOR");
        String sensorCV2 = resultJson.getString("CV2_SENSOR");
        String sensorCV3 = resultJson.getString("CV3_SENSOR");

        JSONObject JsonT = new JSONObject();
        JsonT.put("QUEUE", "checkCarrierForRoboticArmTask");
        JsonT.put("MESSAGE_BODY", "sensorCV1: "+sensorCV1+", sensorCV2: "+sensorCV2+", sensorCV3: "+sensorCV3);
        JsonT.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        messageSendService.send(CommonConstants.MQ_LOG, JsonT);

        // 機械手臂任務TYPE為IN需要CV1、CV3有棧板；機械手臂任務TYPE為OUT需要CV2、CV3有棧板
        // 查詢空棧板是否足夠2個，不足則使用場域外捕
        String woType = asrsOrderList.get(0).getWoType();
        String voucherNo = roboticArmTask.getVoucherNo();
        String woSerial = roboticArmTask.getWoserial();
        String resource = roboticArmTask.getResource();

        if("IN".equals(roboticArmTask.getType())){
            // 查詢空棧板數量

            // 確認需要數量
            if("N".equals(sensorCV1) && "N".equals(sensorCV2) && "N".equals(sensorCV3)){
                // 空棧板足夠

                // 空棧板不足
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "PutPallet", "1");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "IN-CV1toCV3", "2");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "PutPallet", "3");
            }
            else if("Y".equals(sensorCV1) && "N".equals(sensorCV2) && "N".equals(sensorCV3)){
                // 空棧板足夠

                // 空棧板不足
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "EmptyPallet", "1");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "IN-CV1toCV3", "2");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "PutPallet", "3");
            }
            else if("N".equals(sensorCV1) && "N".equals(sensorCV2) && "Y".equals(sensorCV3)){
                // 空棧板足夠

                // 空棧板不足
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "PutPallet", "1");
            }
            else  return  null;
        }
        else if("OUT".equals(roboticArmTask.getType())){
            // 紀錄carrier、Woserial之間的關係
            reportASRSService.saveReportASRS(roboticArmTask.getCarrier(), roboticArmTask.getWoserial(), roboticArmTask.getVoucherNo());

            // 查詢空棧板數量

            // 確認需要數量
            if("N".equals(sensorCV1) && "N".equals(sensorCV2) && "N".equals(sensorCV3)){
                // 空棧板足夠

                // 空棧板不足
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, roboticArmTask.getCarrier(), resource
                        , "CV3", "CV3", roboticArmTask.getStorageBin(), "OUT-BINtoCV3", "1");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "PutPallet", "2");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "CV1", "CV2", "", "IN-CV1toCV2", "3");
            }
            else if("Y".equals(sensorCV1) && "N".equals(sensorCV2) && "N".equals(sensorCV3)){
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, roboticArmTask.getCarrier(), resource
                        , "CV3", "CV3", roboticArmTask.getStorageBin(), "OUT-BINtoCV3", "1");
                if(CommonConstants.OrderType4.equals(woType)){
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "EmptyPallet", "2");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "PutBasketOnPallet", "3");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV1", "CV2", "", "IN-CV1toCV2", "4");
                }
                else if(CommonConstants.OrderType6.equals(woType)){
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "EmptyPallet", "2");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV1", "CV2", "", "IN-CV1toCV2", "3");
                }
            }
            else if("N".equals(sensorCV1) && "Y".equals(sensorCV2) && "N".equals(sensorCV3)){
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, roboticArmTask.getCarrier(), resource
                        , "CV3", "CV3", roboticArmTask.getStorageBin(), "OUT-BINtoCV3", "1");
                if(CommonConstants.OrderType4.equals(woType)){
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV2", "CV1", "", "IN-CV2toCV1", "2");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "EmptyPallet", "3");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "PutBasketOnPallet", "4");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV1", "CV2", "", "IN-CV1toCV2", "5");
                }
                else if(CommonConstants.OrderType6.equals(woType)){
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV2", "CV1", "", "IN-CV2toCV1", "2");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "EmptyPallet", "3");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV1", "CV2", "", "IN-CV1toCV2", "4");
                }
            }
            else if("N".equals(sensorCV1) && "N".equals(sensorCV2) && "Y".equals(sensorCV3)){
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "CV3", "CV1", "", "IN-CV3toCV1", "1");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                        , "", "", "", "EmptyPallet", "2");
                this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, roboticArmTask.getCarrier(), resource
                        , "CV3", "CV3", roboticArmTask.getStorageBin(), "OUT-BINtoCV3", "3");
                if(CommonConstants.OrderType4.equals(woType)){
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "", "", "", "PutBasketOnPallet", "4");
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV1", "CV2", "", "IN-CV1toCV2", "5");
                }
                else if(CommonConstants.OrderType6.equals(woType)){
                    this.insertCarrierTask(woType, voucherNo, woSerial, rArmTaskId, "", resource
                            , "CV1", "CV2", "", "IN-CV1toCV2", "4");
                }
            }
            else  return  null;
        }
        carrierTaskForReturn = this.findCarrierTaskByRArmTaskId(rArmTaskId);
        return carrierTaskForReturn;
    }

    // 透過機械手臂任務ID找CarrierTask
    @Override
    public CarrierTask findCarrierTaskByRArmTaskId(String rArmTaskId){
        List<CarrierTask> list = carrierTaskMapper.findCarrierTaskByRArmTaskId(rArmTaskId);
        CarrierTask carrierTask = new CarrierTask();
        CarrierTask carrierTaskCheck = new CarrierTask();

        if(list.size()==0 || list==null)    return null;
        else{
            // 找到序號最小且狀態為NEW的CarrierTask
            int nowTaskOrder = 0;
            int returnTaskOrder = list.size()+1;
            int selectTask = list.size()+1;
            for(int i=0; i<list.size();i++){
                carrierTaskCheck = list.get(i);
                nowTaskOrder = Integer.parseInt(carrierTaskCheck.getTaskOrder());
                if((nowTaskOrder<returnTaskOrder)&&(CommonConstants.STATUS_NEW.equals(carrierTaskCheck.getStatus()))){
                    returnTaskOrder = nowTaskOrder;
                    selectTask = i;
                }
            }
            if(returnTaskOrder != list.size()+1){
                return list.get(selectTask);
            }
            else    return null;
        }
    }

    // 透過woSerial找CarrierTask
    @Override
    public CarrierTask findCarrierTaskByWoSerial(String woSerial){
        List<CarrierTask> list = carrierTaskMapper.findCarrierTaskByWoSerial(woSerial);
        CarrierTask carrierTask = new CarrierTask();
        CarrierTask carrierTaskCheck = new CarrierTask();
        if(list.size()==0 || list==null)    return null;
        else{
            // 先找到已完成的最大TaskOrder  TaskOrder最小為1 若皆沒完成 則直接回傳序號為1的任務
            int maxTaskOrder = 0;
            int nowTaskOrder = 0;
            for(int i=0; i<list.size();i++){
                carrierTaskCheck = list.get(i);
                nowTaskOrder = Integer.parseInt(carrierTaskCheck.getTaskOrder());
                if((nowTaskOrder>maxTaskOrder)&&(CommonConstants.STATUS_END.equals(carrierTaskCheck.getStatus()))){
                    maxTaskOrder = nowTaskOrder;
                }
            }
            if(maxTaskOrder==list.size())return null;
            else{
                // 回傳沒完成的中最小的TaskOrder
                String nextTaskOrder = String.valueOf(maxTaskOrder+1);
                for(int i=0; i<list.size();i++){
                    carrierTaskCheck = list.get(i);
                    if(nextTaskOrder.equals(carrierTaskCheck.getTaskOrder())){
                        carrierTask = list.get(i);
                        break;
                    }
                }
                return carrierTask;
            }
        }
    }

    // 透過任務ID找CarrierTask
    @Override
    public CarrierTask findCarrierTaskByHandle(String handle){
        return carrierTaskMapper.findCarrierTaskById(handle);
    }

    // 發布機械手臂相關任務，成功回傳1，失敗回傳0
    // 任務1 (使用按鈕Button.Task): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、
    //                            BasketOutPallet、PutMaterialInBasket、CheckInventory
    // 任務2 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    // 任務3 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
    // 任務4 (機械手臂): ROBOTIC_ARM
    // 任務5 (無人運輸車): AGV_TRANS
    // 任務6 (輸送帶移動conveyor.trans): CNV_TRANS
    @Override
    public boolean deployCarrierTask(String rArmTaskId, String woSerial){
        boolean result = true;
        CarrierTask carrierTask = new CarrierTask();
        if(rArmTaskId==null){
            carrierTask = this.findCarrierTaskByWoSerial(woSerial);
        }
        else{
            // 檢查有沒有針對指定機械手臂任務沒執行的carrierTask
            carrierTask = this.findCarrierTaskByRArmTaskId(rArmTaskId);
            List<CarrierTask> list = carrierTaskMapper.findCarrierTaskByRArmTaskId(rArmTaskId);

            // 若不存在carrierTask，且機械手臂任務沒有執行，則檢查是否需要新增carrierTask，並回傳carrierTask
            RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByHandle(rArmTaskId);
            if(carrierTask==null && CommonConstants.STATUS_NEW.equals(roboticArmTask.getStatus()) && (list.size()==0 || list==null)){
                carrierTask = this.checkCarrierForRoboticArmTask(rArmTaskId);
            }
        }

        if(carrierTask==null){
            result = false;
        }else{
            // 發布棧板任務
            JSONObject JsonTask = new JSONObject();
            JsonTask.put("MESSAGE_ID", carrierTask.getHandle());
            JsonTask.put("SEND_TIME", LocalDateTime.now().toString());//System.currentTimeMillis()
            JsonTask.put("RESOURCE", carrierTask.getResource());

            if("IN-CV1toCV2".equals(carrierTask.getTaskType()) || "IN-CV1toCV3".equals(carrierTask.getTaskType()) ||
               "OutStation".equals(carrierTask.getTaskType()) || "PutPallet".equals(carrierTask.getTaskType()) ||
               "EmptyPallet".equals(carrierTask.getTaskType())||"PutBasketOnPallet".equals(carrierTask.getTaskType())||
               "BasketOutPallet".equals(carrierTask.getTaskType())||"PutMaterialInBasket".equals(carrierTask.getTaskType())||
               "CheckInventory".equals(carrierTask.getTaskType())){
                JsonTask.put("MESSAGE_TYPE", "Button.Task");
                JsonTask.put("TYPE", carrierTask.getTaskType());
                messageSendService.send("Button.Task", JsonTask);
            }
            else if("OUT-BINtoCV1".equals(carrierTask.getTaskType()) || "OUT-BINtoCV2".equals(carrierTask.getTaskType()) ||
                    "OUT-BINtoCV3".equals(carrierTask.getTaskType())){
                JsonTask.put("MESSAGE_TYPE", "Storage.Bin.To.Conveyor");
                JsonTask.put("STATION", carrierTask.getEndStation());
                JsonTask.put("PALLET_ID", carrierTask.getCarrier());
                JsonTask.put("STORAGE_BIN", carrierTask.getStorageBin());
                messageSendService.send(CommonConstants.StorageBin_To_Conveyor, JsonTask);
            }
            else if("IN-CV1toBIN".equals(carrierTask.getTaskType()) || "IN-CV2toBIN".equals(carrierTask.getTaskType()) ||
                    "IN-CV3toBIN".equals(carrierTask.getTaskType())){

                // 如果棧板值為空，需要確認
                if("".equals(carrierTask.getCarrier()) || carrierTask.getCarrier()==null){
                    String conveyorLocation = "";
                    if("IN-CV1toBIN".equals(carrierTask.getTaskType()))  conveyorLocation = "CV1";
                    else if("IN-CV2toBIN".equals(carrierTask.getTaskType()))  conveyorLocation = "CV2";
                    else if("IN-CV3toBIN".equals(carrierTask.getTaskType()))  conveyorLocation = "CV3";

                    // 確認輸送帶上的棧板ID
                    JSONObject askPalletId = new JSONObject();
                    askPalletId.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum()); //"WCS_" + System.currentTimeMillis()
                    askPalletId.put("MESSAGE_TYPE", CommonConstants.Get_Pallet_ID);
                    askPalletId.put("RESOURCE", carrierTask.getResource());
                    askPalletId.put("STATION", conveyorLocation);
                    askPalletId.put("SEND_TIME", LocalDateTime.now().toString());//System.currentTimeMillis()
                    JSONObject resultJson =  messageSendService.sendAndReceive(CommonConstants.Get_Pallet_ID, askPalletId);

                    String palletId = resultJson.getString("PALLET_ID");
                    carrierTask.setCarrier(palletId);
                }

                JsonTask.put("MESSAGE_TYPE", CommonConstants.Conveyor_To_StorageBin);
                JsonTask.put("STATION", carrierTask.getEndStation());
                JsonTask.put("PALLET_ID", carrierTask.getCarrier());
                JsonTask.put("STORAGE_BIN", carrierTask.getStorageBin());
                messageSendService.send(CommonConstants.Conveyor_To_StorageBin, JsonTask);
            }
            else if("CNV_TRANS".equals(carrierTask.getTaskType())){
                // 如果棧板值為空，需要確認
                if("".equals(carrierTask.getCarrier()) || carrierTask.getCarrier()==null){
                    // 確認輸送帶上的棧板ID
                    JSONObject askPalletId = new JSONObject();
                    askPalletId.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum()); //"WCS_" + System.currentTimeMillis()
                    askPalletId.put("MESSAGE_TYPE", CommonConstants.Get_Pallet_ID);
                    askPalletId.put("RESOURCE", carrierTask.getResource());
                    askPalletId.put("STATION", carrierTask.getStartStation());
                    askPalletId.put("SEND_TIME", LocalDateTime.now().toString());//System.currentTimeMillis()
                    JSONObject resultJson =  messageSendService.sendAndReceive(CommonConstants.Get_Pallet_ID, askPalletId);

                    String palletId = resultJson.getString("PALLET_ID");
                    carrierTask.setCarrier(palletId);
                }

                String conveyorNo = "";
                if("Conveyor1".equals(carrierTask.getResource())) conveyorNo = "1";
                else if("Conveyor2".equals(carrierTask.getResource())) conveyorNo = "2";
                else if("Conveyor3".equals(carrierTask.getResource())) conveyorNo = "3";
                else if("Conveyor4".equals(carrierTask.getResource())) conveyorNo = "4";
                else if("Conveyor5".equals(carrierTask.getResource())) conveyorNo = "5";

                JsonTask.put("MESSAGE_TYPE", "conveyor.trans."+conveyorNo);
                JsonTask.put("RESOURCE", carrierTask.getResource());
                JsonTask.put("PALLET_ID", carrierTask.getCarrier());
                JsonTask.put("START_STATION", carrierTask.getStartStation());
                JsonTask.put("END_STATION", carrierTask.getEndStation());
                messageSendService.send("conveyor.trans."+conveyorNo, JsonTask);
            }
            // 更新任務的狀態為START
            carrierTask.setStatus("START");// 狀態有 NEW、START、END
            carrierTaskService.saveOrUpdate(carrierTask);
            //this.updateCarrierTask(carrierTask);
        }
        return result;
    }

    // 更新任務
    // 任務狀態STATUS: NEW、START、END
    @Override
    public void updateCarrierTask(CarrierTask carrierTask){
        carrierTask.setUpdater("ADMIN");
        carrierTask.setUpdateTime(LocalDateTime.now());

        QueryWrapper<CarrierTask> wrapper = new QueryWrapper<>();
        wrapper.eq(CarrierTask.HANDLE, carrierTask.getHandle());
        this.update(carrierTask, wrapper);
    }
    // 新增任務
    @Override
    public void insertCarrierTask(CarrierTask carrierTask){
        carrierTaskMapper.insert(carrierTask);
    }

    // 給定指定任務內容 新增
    @Override
    public void insertCarrierTask(String woType, String voucherNo, String woSerial, String rArmTaskId
            , String carrier, String resource, String startStation, String endStation
            , String storageBin, String taskType, String taskOrder){
        CarrierTask carrierTask = new CarrierTask();
        carrierTask.setHandle(DateUtil.getDateTimeWithRandomNum());
        carrierTask.setWoType(woType);
        carrierTask.setVoucherNo(voucherNo);
        carrierTask.setWoserial(woSerial);
        carrierTask.setRArmTaskId(rArmTaskId);
        carrierTask.setCarrier(carrier);
        carrierTask.setResource(resource);
        carrierTask.setStartStation(startStation);
        carrierTask.setEndStation(endStation);
        carrierTask.setStorageBin(storageBin);
        carrierTask.setTaskType(taskType);
        carrierTask.setTaskOrder(taskOrder);
        carrierTask.setStatus(CommonConstants.STATUS_NEW);
        carrierTask.setResult("");
        carrierTask.setCreator(CommonConstants.CREATE_USER);
        carrierTask.setCreateTime(LocalDateTime.now());
        carrierTask.setUpdater(CommonConstants.CREATE_USER);
        carrierTask.setUpdateTime(LocalDateTime.now());
        carrierTaskMapper.insert(carrierTask);
    }
}
