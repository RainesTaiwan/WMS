package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.mapper.RoboticArmTaskMapper;
import com.fw.wcs.sys.model.RFIDReaderTask;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.model.RoboticArmTask;
import com.fw.wcs.sys.model.RoboticArm;
import com.fw.wcs.sys.service.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoboticArmTaskServiceImpl extends ServiceImpl<RoboticArmTaskMapper, RoboticArmTask> implements RoboticArmTaskService{

    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private RoboticArmTaskMapper roboticArmTaskMapper;
    @Autowired
    private RoboticArmService roboticArmService;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private PressButtonService pressButtonService;
    @Autowired
    private RFIDReaderService rfidReaderService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;
    @Autowired
    private RFIDReaderMaskService rfidReaderMaskService;
    @Autowired
    private ReceiveStationService receiveStationService;

    // 建立任務
    @Override
    public void createRoboticArmTask(String messageId, String voucherNo, String woSerial, String woQty, String doQty
            , String fromPalletQty, String toPalletQty, String conveyor, String type){

        //先確認機械手臂有空，不然就等待
        RoboticArm roboticArm = new RoboticArm();
        if(("Conveyor1".equals(conveyor))||("Conveyor2".equals(conveyor))){
            roboticArm = roboticArmService.findRoboticArmByName("RArm1");
        }
        else if("Conveyor4".equals(conveyor)){
            roboticArm = roboticArmService.findRoboticArmByName("RArm2");
        }
        else if("Conveyor5".equals(conveyor)){
            roboticArm = roboticArmService.findRoboticArmByName("RArm3");
        }

        //任務的設定
        Date time = new Date();
        String handle = DateUtil.getDateTimemessageId();
        RoboticArmTask roboticArmTask = new RoboticArmTask();
        roboticArmTask.setHandle(handle);
        roboticArmTask.setMessageID(messageId);
        roboticArmTask.setVoucherNo(voucherNo);
        roboticArmTask.setWoSerial(woSerial);
        roboticArmTask.setWoQty(woQty);
        roboticArmTask.setDoQty(doQty);
        roboticArmTask.setFromPalletQty(fromPalletQty);
        roboticArmTask.setToPalletQty(toPalletQty);
        roboticArmTask.setRoboticArm(roboticArm.getRoboticArm());
        roboticArmTask.setResource(conveyor);
        roboticArmTask.setType(type);
        roboticArmTask.setStatus(CustomConstants.NEW);
        roboticArmTask.setCreateUser("ADMIN");
        roboticArmTask.setCreatedTime(time);

        // 將任務存到資料庫
        roboticArmTaskMapper.insert(roboticArmTask);
        // 先確認機械手臂有空，不然就等待 等到機械手臂任務結束 再次偵測
        this.checkDoRoboticArmTask(conveyor);
    }

    // 更新任務狀態
    // 設定機械手臂的任務狀態 NEW, START, COMPLETE, UNCOMPLETE
    @Override
    public void updateRoboticArmTask(String handle, String status, String result){
        RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(handle);
        roboticArmTask.setStatus(status);
        roboticArmTask.setResult(result);
        roboticArmTask.setUpdateUser("ADMIN");
        roboticArmTask.setUpdatedTime(new Date());

        Wrapper<RoboticArmTask> wrapper = new EntityWrapper<>();
        wrapper.eq(RoboticArmTask.HANDLE, handle);
        this.update(roboticArmTask, wrapper);
    }
    @Override
    public void updateRoboticArmTask(RoboticArmTask roboticArmTask){
        roboticArmTask.setUpdateUser("ADMIN");
        roboticArmTask.setUpdatedTime(new Date());

        Wrapper<RoboticArmTask> wrapper = new EntityWrapper<>();
        wrapper.eq(RoboticArmTask.HANDLE, roboticArmTask.getHandle());
        this.update(roboticArmTask, wrapper);
    }

    // 發送任務 - Request.Robotic.Arm
@Override
public void sendRequestRoboticArm(RoboticArmTask roboticArmTask) {
    // 透過MQ發送給機械手臂
    // 等待 3 秒
    try {
        Thread.sleep(3000); // 單位為毫秒，3000 毫秒等於 3 秒
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // 恢復中斷狀態
        throw new RuntimeException("Thread was interrupted", e);
    }
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("MESSAGE_TYPE", CustomConstants.RequestRobotArm);
    jsonObject.put("MESSAGE_ID", roboticArmTask.getHandle());
    jsonObject.put("WO_QTY", roboticArmTask.getWoQty());
    jsonObject.put("DO_QTY", roboticArmTask.getDoQty());
    jsonObject.put("FROM_PALLET_QTY", roboticArmTask.getFromPalletQty());
    jsonObject.put("TO_PALLET_QTY", roboticArmTask.getToPalletQty());
    jsonObject.put("RESOURCE", roboticArmTask.getRoboticArm());
    jsonObject.put("UPPER_RESOURCE", roboticArmTask.getResource());
    jsonObject.put("TYPE", roboticArmTask.getType());
    jsonObject.put("SEND_TIME", LocalDateTime.now().toString()); // System.currentTimeMillis()
    activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.RequestRobotArm, jsonObject.toJSONString());



    // 更新輸送帶狀態
    // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
    // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
    // ● 任務4 (機械手臂): ROBOTIC_ARM
    // 任務5 (無人運輸車): AGV_TRANS
    ReceiveStation receiveStation = receiveStationService.getReceiveStation(roboticArmTask.getResource());
    receiveStation.setNowTask("ROBOTIC_ARM");
    receiveStationService.updateReceiveStation(receiveStation);

    JSONObject JsonTemp = new JSONObject();
    JsonTemp.put("QUEUE", CustomConstants.RequestRobotArm);
    JsonTemp.put("MESSAGE_BODY", jsonObject.toJSONString());
    JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); // System.currentTimeMillis()
    activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

    // 告知ASRS RoboticArm狀態為WORKING
    roboticArmService.reportASRS(roboticArmTask.getRoboticArm(), CustomConstants.WORKING);
}


    // 用MQID找尋任務
    @Override
    public RoboticArmTask findRoboticArmTaskByMessageID(String messageId){
        List<RoboticArmTask> list = roboticArmTaskMapper.findRoboticArmTaskByMessageID(messageId);
        if(list.size()>0)   return list.get(0);
        return null;
    }

    // 用輸送帶找尋"新"任務
    @Override
    public RoboticArmTask findRoboticArmTaskByResource(String resource){
        List<RoboticArmTask> list = roboticArmTaskMapper.findRoboticArmTaskByResource(resource);
        for(int i=0; i<list.size();i++){
            if(CustomConstants.NEW.equals(list.get(i).getStatus())){
                return list.get(0);
            }
        }
        return null;
    }

    // 用Handle找尋任務
    @Override
    public RoboticArmTask findRoboticArmTaskByID(String handle) {
        return roboticArmTaskMapper.findRoboticArmTaskByHandle(handle);
    }

    // 用VoucherNo找尋任務
    @Override
    public List<RoboticArmTask> findRoboticArmTaskByVoucherNo(String voucherNo){
        return roboticArmTaskMapper.findRoboticArmTaskByVoucherNo(voucherNo);
    }

    // 檢查指定輸送帶，若有任務待處理，則執行
    // 由於是創建任務後檢查，所以roboticArmTask一定有資料 ==> 不一定  因為執行完畢也會檢查
    @Override
    public void checkDoRoboticArmTask(String resource){
        // 透過輸送帶找到需要執行的任務與輸送帶狀態
        RoboticArmTask roboticArmTask = this.findRoboticArmTaskByResource(resource);

        if(roboticArmTask!=null){
            RoboticArm roboticArm = roboticArmService.findRoboticArmByName(roboticArmTask.getRoboticArm());

            // 若輸送帶IDLE且任務狀態為NEW，則發送機械手臂任務要求
            if((roboticArm!=null) && (CustomConstants.IDLE.equals(roboticArm.getStatus())) && (roboticArmTask!=null)){
                // 設定機械手臂的狀態
                roboticArmService.updateRoboticArmStatus(roboticArmTask.getRoboticArm(), roboticArmTask.getResource(), CustomConstants.WORKING);
                //更新輸送帶狀態
                ReceiveStation receiveStation = receiveStationService.getReceiveStation(roboticArmTask.getResource());
                receiveStation.setTaskGoal("ROBOTIC_ARM");
                receiveStation.setStatus(CustomConstants.WORKING);
                receiveStationService.updateReceiveStation(receiveStation);

                if("IN".equals(roboticArmTask.getType())){
                    // 按鈕任務 理貨完畢才會按鈕，按鈕後將棧板移動(CV1->CV2)，完畢才發送機械手臂任務
                    pressButtonService.createPressButtonTask(roboticArmTask.getHandle(), roboticArmTask.getResource(), "IN-CV1toCV2");
                    /*
                    JSONObject JsonTemp = new JSONObject();
                    JsonTemp.put("QUEUE", "IN-CV1toCV2 - ROBOTIC_ARM Create");
                    JsonTemp.put("MESSAGE_BODY", "Handle:"+ roboticArmTask.getHandle());
                    JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                    activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
                    */
                    //要求建立RFID任務
                    rfidReaderTaskService.createRFIDReaderTask(roboticArmTask.getVoucherNo(), resource, "CV3", "RoboticArm");
                }
                else if("OUT".equals(roboticArmTask.getType())){
                    // 透過MQ發送給機械手臂指定任務
                    this.sendRequestRoboticArm(roboticArmTask);
                    //要求建立RFID任務
                    rfidReaderTaskService.createRFIDReaderTask(roboticArmTask.getVoucherNo(), resource, "CV2", "RoboticArm");
                }
            }
        } // End if(roboticArmTask!=null)
    }

    // 結束機械手臂任務
    @Override
    public void endRoboticArmTask(String resource){
        // 找到指定機械手臂任務
        // 機械手臂的狀態 NEW, START, COMPLETE, UNCOMPLETE

        List<RoboticArmTask> list = roboticArmTaskMapper.findRoboticArmTaskByResource(resource);
        RoboticArmTask roboticArmTask = new RoboticArmTask();
        for(int i=0; i<list.size();i++){
            if(CustomConstants.START.equals(list.get(i).getStatus())){
                roboticArmTask = list.get(i);
                break;
            }
        }

        // 要求取得RFID - 先找到指定的RFID Reader 再找此RFID Reader的任務
        RFIDReaderTask rfidReaderTask = new RFIDReaderTask();
        if("IN".equals(roboticArmTask.getType())){
            //要求建立RFID任務
            //rfidReaderTaskService.createRFIDReaderTask(roboticArmTask.getVoucherNo(), resource, "CV3", "RoboticArm");
            String rfidID = rfidReaderService.getRFIDReaderID(resource, "CV3");
            rfidReaderTask = rfidReaderTaskService.findTaskByRFID(rfidID);
        }else if("OUT".equals(roboticArmTask.getType())){
            //要求建立RFID任務
            //rfidReaderTaskService.createRFIDReaderTask(roboticArmTask.getVoucherNo(), resource, "CV1", "RoboticArm");
            String rfidID = rfidReaderService.getRFIDReaderID(resource, "CV2");
            rfidReaderTask = rfidReaderTaskService.findTaskByRFID(rfidID);
        }
        //等待RFID讀取完畢
        //try{
        //    Thread.sleep(new Long(21000));//隨意設置可更動 預設RFID讀取時間+1秒(1000毫秒)
        //    //Thread.sleep(new Long(CustomConstants.WAITINGRFID_RoboticArm));
        //}catch (Exception e){
        //    e.printStackTrace();
        //}

        //呼叫要求傳送讀取RFID清單
        JSONObject jsonObject_request = new JSONObject();
        jsonObject_request.put("MessageType", "Conveyor.Tags.Request");
        jsonObject_request.put("MESSAGE_ID", rfidReaderTask.getMessageID());
        jsonObject_request.put("RESOURCE", rfidReaderTask.getReaderID()); //RFID READER的序號
        jsonObject_request.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Tags.Request", jsonObject_request.toJSONString());

        //將機械手臂重設為IDLE
        roboticArmService.updateRoboticArmStatus(roboticArmTask.getRoboticArm(), "", CustomConstants.IDLE);

        // 完成須告知WMS，沒完成需要重新創建任務
        // 若任務 RESULT 0: 任務完成、1: 專用儲籃裝滿但任務未完成、2: 物料拿取完畢但任務未完成、
        //              3: 發生異常(斷線、抓取物料失敗、機械手臂視覺故障、沒有感測到專用儲籃、任務錯誤、其他)
        String resultMsg = "";
        if("0".equals(roboticArmTask.getResult())){
            resultMsg = "任務完成";
            roboticArmTaskService.updateRoboticArmTask(roboticArmTask.getHandle(), CustomConstants.COMPLETE, resultMsg);
            // 告知WMS機械手臂任務完成
            roboticArmTaskService.reportWMSRoboticArmTask(roboticArmTask.getHandle(), "0");
        }
        else if("1".equals(roboticArmTask.getResult())){
            resultMsg = "專用儲籃裝滿但任務未完成";
            roboticArmTaskService.updateRoboticArmTask(roboticArmTask.getHandle(), CustomConstants.UNCOMPLETE, resultMsg);
            // 告知WMS 更換儲藍
            roboticArmTaskService.reportWMSRoboticArmTask(roboticArmTask.getHandle(), "1");
        }
        else if("2".equals(roboticArmTask.getResult())){
            resultMsg = "物料拿取完畢但任務未完成";
            roboticArmTaskService.updateRoboticArmTask(roboticArmTask.getHandle(), CustomConstants.UNCOMPLETE, resultMsg);
        }
        else if("3".equals(roboticArmTask.getResult())){
            resultMsg = "發生異常";
            roboticArmTaskService.updateRoboticArmTask(roboticArmTask.getHandle(), CustomConstants.UNCOMPLETE, resultMsg);
        }

        // 檢查機械手臂任務
        this.checkDoRoboticArmTask(resource);
    }

    // 告知WMS任務完成
    @Override
    public void reportWMSRoboticArmTask(String handle, String result){
        //預設RFID更新時間
        try{
            long waitingTime = CustomConstants.WAITING_RFID_Update;
            Thread.sleep(new Long(waitingTime));
        }catch (Exception e){
            e.printStackTrace();
        }
        RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(handle);
        // 告知WMS RFID清單
        rfidReaderMaskService.reportReadRFIDList(roboticArmTask.getVoucherNo());
        //預設RFID更新時間
        try{
            long waitingTime = CustomConstants.WAITING_RFID_Update;
            Thread.sleep(new Long(waitingTime));
        }catch (Exception e){
            e.printStackTrace();
        }

        // 發送任務 - Robotic.Arm.Report.WMS
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE", "Robotic.Arm.Report.WMS");
        jsonObject.put("CORRELATION_ID", roboticArmTask.getMessageID());

        // 取出DO_QTY資料，轉成JSONArray格式
        String dataArray_DOQTY = "{\"QTY\":"+roboticArmTask.getDoQty()+"}";
        JSONObject JsonNewData = JSON.parseObject(dataArray_DOQTY);
        JSONArray data = JsonNewData.getJSONArray("QTY");
        jsonObject.put("QTY", data);

        jsonObject.put("RESULT", result); // 目前只有"專用儲籃裝滿但任務未完成"(1)與"任務完成"(0) 會回報
        jsonObject.put("MSG", roboticArmTask.getResult());
        jsonObject.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Robotic.Arm.Report.WMS", jsonObject.toJSONString());

        //更新輸送帶狀態
        ReceiveStation receiveStation = receiveStationService.getReceiveStation(roboticArmTask.getResource());
        receiveStation.setStatus(CustomConstants.IDLE);
        receiveStation.setTaskGoal("");
        receiveStation.setNowTask("");
        receiveStationService.updateReceiveStation(receiveStation);
    }

    // 模擬呼叫 Robotic.Arm.Report.WCS
    // type有"ALL"、"PART"表示回傳數量要全部或部分
    @Override
    public void reportTasktoWCS(String handle, String type){
        // 找尋指定任務
        RoboticArmTask roboticArmTask = this.findRoboticArmTaskByID(handle);

        // QTY    抓取數量，依紙箱類型 [PB1, PB2, PB3, PB4, XXX]
        // RESULT 0: 任務完成、1: 專用儲籃裝滿但任務未完成、2: 物料拿取完畢但任務未完成、
        //        3: 發生異常(斷線、抓取物料失敗、機械手臂視覺故障、沒有感測到專用儲籃、任務錯誤、其他)

        // 取出DO_QTY資料，轉成JSONArray格式
        String dataArray_DOQTY = "{\"QTY\":"+roboticArmTask.getDoQty()+"}";
        JSONObject JsonNewData = JSON.parseObject(dataArray_DOQTY);
        JSONArray data = JsonNewData.getJSONArray("QTY");
        int[] qtyDo = new int[data.size()];
        int[] qtyWo = new int[data.size()];
        int[] qtyFinal = new int[data.size()];
        int pointer = 0; //用於確認是哪個值要處理

        for (int i = 0; i < data.size(); i++){
            qtyDo[i] = Integer.parseInt(data.getString(i));
            qtyFinal[i] = qtyDo[i];
            if(qtyDo[i]>0)  pointer = i;
        }
        // 取出WO_QTY資料，轉成JSONArray格式
        String dataArray_WOQTY = "{\"QTY\":"+roboticArmTask.getDoQty()+"}";
        JsonNewData = JSON.parseObject(dataArray_WOQTY);
        data = JsonNewData.getJSONArray("QTY");
        for (int i = 0; i < data.size(); i++){
            qtyWo[i] = Integer.parseInt(data.getString(i));
        }

        String result = ""; // 結果RESULT 設置
        if("ALL".equals(type)){
            result = "0";
        }
        else if("PART".equals(type)){
            // 選擇抓取數量
            int randomGet = (int)(Math.random()*(qtyWo[pointer]-1))+1; //產生1~目標量的數值
            if(randomGet>=qtyDo[pointer]){
                qtyFinal[pointer] = qtyDo[pointer];
                result = "0";
            }
            else{
                qtyFinal[pointer] = randomGet;
                result = "2";
            }
        }

        // 發送任務 - Robotic.Arm.Report.WCS
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE", "Robotic.Arm.Report.WCS");
        jsonObject.put("CORRELATION_ID", roboticArmTask.getHandle());

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < data.size(); i++){
            jsonArray.add(qtyFinal[i]);
        }
        jsonObject.put("QTY", jsonArray);

        jsonObject.put("RESULT", result);
        jsonObject.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Robotic.Arm.Report.WCS", jsonObject.toJSONString());

    }
}
