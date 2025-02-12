package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.mapper.ButtonTaskMapper;
import com.fw.wcs.sys.model.ButtonTask;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.model.RoboticArmTask;
import com.fw.wcs.sys.service.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PressButtonServiceImpl extends ServiceImpl<ButtonTaskMapper, ButtonTask> implements PressButtonService {

    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ButtonTaskMapper buttonTaskMapper;
    @Autowired
    private PLCDataService plcDataService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;


    // 輸送帶目標任務
    // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
    // ● 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
    // 任務4 (機械手臂): ROBOTIC_ARM
    // 任務5 (無人運輸車): AGV_TRANS
    // 任務6 (輸送帶移動): CNV_TRANS

    //ButtonTask TYPE類型：IN-CV1toCV2、IN-CV1toCV3、OUT、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
    //ButtonTask STATUS類型：NEW、START、END
    @Override
    public void  createPressButtonTask(String handle, String receiveStation, String type){
        ButtonTask buttonTask = new ButtonTask();
        buttonTask.setHandle(handle);
        buttonTask.setReceiveStation(receiveStation);
        buttonTask.setType(type);
        buttonTask.setStatus(CustomConstants.NEW);
        buttonTask.setCreateUser("ADMIN");
        buttonTask.setCreatedTime(new Date());
        buttonTaskMapper.insert(buttonTask);
        this.doPressButtonTask(handle, receiveStation, type, CustomConstants.START);
    }

    //找尋任務
    @Override
    public ButtonTask findButtonTask(String receiveStation){
        List<ButtonTask> list = buttonTaskMapper.findButtonTaskByReceiveStation(receiveStation);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    //透過HANDLE找尋任務
    @Override
    public ButtonTask findButtonTaskByID(String handle){
        return buttonTaskMapper.findButtonTaskByHandle(handle);
    }

    // 執行任務
    @Override
    public void doPressButtonTask(String handle, String receiveStation, String type, String status){

        //確認輸送帶的PLC的lenNum
        String lenNum = "";
        if("Conveyor1".equals(receiveStation))  lenNum = "1";
        else if("Conveyor2".equals(receiveStation))  lenNum = "2";
        else if("Conveyor3".equals(receiveStation))  lenNum = "3";
        else if("Conveyor4".equals(receiveStation))  lenNum = "4";
        else if("Conveyor5".equals(receiveStation))  lenNum = "5";

        // 更新狀態
        ButtonTask buttonTask = this.findButtonTask(receiveStation);
        buttonTask.setStatus(status);
        buttonTask.setUpdateUser("ADMIN");
        buttonTask.setUpdatedTime(new Date());
        Wrapper<ButtonTask> wrapper = new EntityWrapper<>();
        wrapper.eq(ButtonTask.HANDLE, buttonTask.getHandle());
        this.update(buttonTask, wrapper);

        //PLC資料只寫D3100~D3119的資料 共20個點位
        int[] plcWriteIntArray = new int[20];
        Arrays.fill(plcWriteIntArray, 0); // 陣列值初始化

        //ButtonTask TYPE類型：IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
        //ButtonTask STATUS類型：NEW、START、END
        if(CustomConstants.START.equals(status)){
            if("IN-CV1toCV2".equals(type))    plcWriteIntArray[12] = 2;
            else if("IN-CV1toCV3".equals(type))    plcWriteIntArray[12] = 2;
            else if("OutStation".equals(type))    plcWriteIntArray[12] = 2; //沒有亮燈，給個值使流程順暢
            else if("PutPallet".equals(type))    plcWriteIntArray[12] = 2;
            else if("EmptyPallet".equals(type))    plcWriteIntArray[12] = 2;
            else if("PutBasketOnPallet".equals(type))    plcWriteIntArray[12] = 2;
            else if("BasketOutPallet".equals(type))    plcWriteIntArray[12] = 2;
            else if("test".equals(type))    plcWriteIntArray[12] = 2;
            else if("WO01".equals(type))    plcWriteIntArray[12] = 2;
            else if("WO02".equals(type))    plcWriteIntArray[12] = 2;
            // 更新輸送帶目前任務
            ReceiveStation rs = receiveStationService.getReceiveStation(receiveStation);
            rs.setStatus(CustomConstants.WORKING);
            rs.setNowTask(type);
            receiveStationService.updateReceiveStation(rs);
            // 告知ASRS輸送帶狀態
            receiveStationService.reportASRS(receiveStation, CustomConstants.WORKING);
        }
        else if(CustomConstants.COMPLETE.equals(status)){

            plcWriteIntArray[12] = 0;
        }
        // 形成指定PLC資料，交由PLC執行
        plcDataService.writePLCData(lenNum, plcWriteIntArray);
        //plcDataService.SimulationPLCData(receiveStation, "PressButton", plcWriteIntArray);
    }

    // 結束任務
    @Override
    public void endPressButtonTask(String receiveStation){
        ButtonTask buttonTask = this.findButtonTask(receiveStation);
        String handle = buttonTask.getHandle();
        String type = buttonTask.getType();
        this.doPressButtonTask(handle, receiveStation, type, CustomConstants.COMPLETE);
        // 告知ASRS輸送帶狀態
        receiveStationService.reportASRS(receiveStation, CustomConstants.IDLE);

        ReceiveStation rs = receiveStationService.getReceiveStation(receiveStation);

        // 如果TYPE是"IN-CV1toCV2"需要再下移動任務(先將棧板移至CV2)，確認棧板位於CV2與CV3後執行機械手臂任務，
        if("IN-CV1toCV2".equals(type)){
            if("ROBOTIC_ARM".equals(rs.getTaskGoal())){
                RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(handle);
                handle = roboticArmTask.getMessageID();

                JSONObject JsonTemp = new JSONObject();
                JsonTemp.put("QUEUE", "IN-CV1toCV2 - ROBOTIC_ARM");
                JsonTemp.put("MESSAGE_BODY", roboticArmTask.toString());
                JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
            }
            try{
                receiveStationTaskService.createReceiveStationTask(handle, receiveStation, "", "Transfer"
                        , "CV1", "CV2", false);
            }catch (Exception e){
                JSONObject JsonE = new JSONObject();
                JsonE.put("QUEUE", "endPressButtonTask -e");
                JsonE.put("MESSAGE_BODY", e.getMessage());
                JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                activeMqSendService.sendMsgNoResponse4Wms("MQ_LOG", JsonE.toJSONString());
            }

            /*receiveStationTaskService.createReceiveStationTask(handle, receiveStation, "", "Transfer"
                    , "CV1", "CV2", false);

             */
        }
        // 如果TYPE是"IN-CV1toCV3"需要再下移動任務(先將棧板移至CV3)，完成須告知WMS
        else if("IN-CV1toCV3".equals(type)){
            if("ROBOTIC_ARM".equals(rs.getTaskGoal())){
                RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(handle);
                handle = roboticArmTask.getMessageID();
            }
            receiveStationTaskService.createReceiveStationTask(handle, receiveStation, "", "Transfer"
                                , "CV1", "CV3", false);
        }
        // 如果TYPE是"PutPallet" 需要建立RFID任務，完成須告知WMS
        else if("PutPallet".equals(type)){
            // 建立RFID任務
            // handle會存到rfidReaderTask的voucherNo
            rfidReaderTaskService.createRFIDReaderTask(buttonTask.getHandle(), buttonTask.getReceiveStation()
                                                       , "CV1", "GetPalletID");
            //完成須告知WMS
            this.reportButtonTaskResult(handle);
        }
        // 如果TYPE是EmptyPallet、PutBasketOnPallet、BasketOutPallet需須告知WMS
        else if("EmptyPallet".equals(type) || "PutBasketOnPallet".equals(type) || "BasketOutPallet".equals(type)){
            this.reportButtonTaskResult(handle);
        }
        // 如果TYPE是OutStation需須告知WMS
        else if("OutStation".equals(type)){
            this.reportButtonTaskResult(handle);
        }
        else if("test".equals(type)){
            JSONObject requestPLC = new JSONObject();
            requestPLC.put("MESSAGE_ID", DateUtil.getDateTimemessageId());
            requestPLC.put("MESSAGE_TYPE", "conveyor.trans.4");   //MX 編號
            requestPLC.put("RESOURCE", "Conveyor4");        //點位區域
            requestPLC.put("PALLET_ID", "ASRS_PALLET_00010");    //起始點位
            requestPLC.put("START_STATION", "CV3");     //資料長度
            requestPLC.put("END_STATION", "CV1");  //設備編號
            requestPLC.put("SEND_TIME", DateUtil.getDateTime());
            String mqName = "conveyor.trans.4";
            String plcResponse = activeMqSendService.sendMsgNeedResponse4Wms(mqName, requestPLC.toJSONString());  
        }
        else if("WO01".equals(type)){
            JSONObject requestPLC = new JSONObject();
            requestPLC.put("MESSAGE_ID", DateUtil.getDateTimemessageId());
            requestPLC.put("MESSAGE_TYPE", "conveyor.trans.4");   //MX 編號
            requestPLC.put("RESOURCE", "Conveyor4");        //點位區域
            requestPLC.put("PALLET_ID", "ASRS_PALLET_00010");    //起始點位
            requestPLC.put("START_STATION", "CV1");     //資料長度
            requestPLC.put("END_STATION", "CV3");  //設備編號
            requestPLC.put("SEND_TIME", DateUtil.getDateTime());
            String mqName = "conveyor.trans.4";
            String plcResponse = activeMqSendService.sendMsgNeedResponse4Wms(mqName, requestPLC.toJSONString());  
        }
        else if("WO02".equals(type)){
            JSONObject requestPLC = new JSONObject();
            requestPLC.put("MESSAGE_ID", DateUtil.getDateTimemessageId());
            requestPLC.put("MESSAGE_TYPE", "conveyor.trans.4");   //MX 編號
            requestPLC.put("RESOURCE", "Conveyor4");        //點位區域
            requestPLC.put("PALLET_ID", "ASRS_PALLET_00010");    //起始點位
            requestPLC.put("START_STATION", "CV3");     //資料長度
            requestPLC.put("END_STATION", "CV1");  //設備編號
            requestPLC.put("SEND_TIME", DateUtil.getDateTime());
            String mqName = "conveyor.trans.4";
            String plcResponse = activeMqSendService.sendMsgNeedResponse4Wms(mqName, requestPLC.toJSONString());  
        }

    }

    // 告知WMS完成
    @Override
    public void reportButtonTaskResult(String handle){
        ButtonTask buttonTask = this.findButtonTaskByID(handle);
        // 更新輸送帶任務類型
        ReceiveStation rs = receiveStationService.getReceiveStation(buttonTask.getReceiveStation());
        rs.setStatus(CustomConstants.IDLE);
        rs.setTaskGoal("");
        rs.setNowTask("");
        receiveStationService.updateReceiveStation(rs);

        // 回報WMS
        JSONObject JsonReport = new JSONObject();
        JsonReport.put("CORRELATION_ID", buttonTask.getHandle());
        JsonReport.put("MESSAGE_TYPE", "Button.Task.Report");
        JsonReport.put("TYPE", buttonTask.getType());
        JsonReport.put("RESULT", CustomConstants.COMPLETE); // 完成
        JsonReport.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Button.Task.Report", JsonReport.toJSONString());
    }
}
