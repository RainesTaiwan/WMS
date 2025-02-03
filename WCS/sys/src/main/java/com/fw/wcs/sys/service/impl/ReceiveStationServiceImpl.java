package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.core.utils.StringUtils;
import com.fw.wcs.sys.dto.CarrierInfoDto;
import com.fw.wcs.sys.mapper.ReceiveStationMapper;
import com.fw.wcs.sys.model.*;
import com.fw.wcs.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiveStationServiceImpl extends ServiceImpl<ReceiveStationMapper, ReceiveStation> implements ReceiveStationService {

    @Autowired
    private ReceiveStationMapper receiveStationMapper;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private PressButtonService pressButtonService;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private CarrierTaskService carrierTaskService;

    @Override
    public ReceiveStation getOutboundReceiveStation() {
        List<ReceiveStation> list = receiveStationMapper.selectOutboundReceiveStation();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public ReceiveStation getReceiveStation(String receiveStation) {
        return receiveStationMapper.selectReceiveStation(receiveStation);
    }

    //感測棧板在指定輸送帶的指定工位上，有回傳1，沒有回傳0
    //代號【CHK_SP_CNV】感測棧板
    /*
    因為在席sensor只有在4個位置有值，一個棧板在一個工位上，最多A到2個位置，可以表示棧板就定位的時候是棧板在2+4的位置
       =  =  =  =
       1, 2, 4, 8
    */
    @Override
    public boolean palletOnReceiveStation(String receiveStation, String station){
        boolean checkResult = false;
        ReceiveStation receiveStationSave = receiveStationService.getReceiveStation(receiveStation);
        if("CV1".equals(station)){
            if("6".equals(receiveStationSave.getCv1PalletSensor())) checkResult = true;
            if("3".equals(receiveStationSave.getCv1PalletSensor())) checkResult = true;
        }
        else if("CV2".equals(station)){
            if("6".equals(receiveStationSave.getCv2PalletSensor())) checkResult = true;
        }else if("CV3".equals(station)){
            if("6".equals(receiveStationSave.getCv3PalletSensor())) checkResult = true;
            if("12".equals(receiveStationSave.getCv3PalletSensor())) checkResult = true;
        }
        return checkResult;
    }

    //感測棧板的長寬高重在指定輸送帶上，若棧板資訊合法，則回傳1，反之則回傳0
    //代號【CHK_SP_INFO】確認棧板信息
    @Override
    public boolean palletInfoCheck(String palletId, String receiveStation){
        boolean checkResult = false;
        ReceiveStation receiveStationSave = receiveStationService.getReceiveStation(receiveStation);

        //發送給WMS檢測數據
        CarrierInfoDto carrierInfoDto = new CarrierInfoDto();
        carrierInfoDto.setMESSAGE_TYPE(CustomConstants.HANDLING_UNIT_INFO);
        carrierInfoDto.setMESSAGE_ID(DateUtil.getDateTimeWithRandomNum());
        carrierInfoDto.setCARRIER(palletId);
        carrierInfoDto.setHEIGHT(receiveStationSave.getHeight());
        carrierInfoDto.setWIDTH(receiveStationSave.getWidth());
        carrierInfoDto.setLENGTH(receiveStationSave.getLength());
        carrierInfoDto.setWEIGHT(receiveStationSave.getWeight());
        carrierInfoDto.setSEND_TIME(DateUtil.getDateTime());

        carrierInfoDto.setMESSAGE_TYPE(CustomConstants.HANDLING_UNIT_INFO);
        carrierInfoDto.setSEND_TIME(DateUtil.getDateTime());
        String infoResponse = activeMqSendService.sendMsgNeedResponse4Wms(CustomConstants.HANDLING_UNIT_INFO, JSON.toJSONString(carrierInfoDto));
        if (StringUtils.isBlank(infoResponse)) {
            throw new BusinessException("【Pallet Info(handling.unit.info.process)】未迴應");
        }
        JSONObject resultJSONObject = JSON.parseObject(infoResponse);
        String result = resultJSONObject.getString("RESULT");
        if("1".equals(result)){
            checkResult = true;
        }

        JSONObject JsonTemp1 = new JSONObject();
        JsonTemp1.put("QUEUE", "palletInfoCheck -1");
        JsonTemp1.put("MESSAGE_BODY", "resultJSONObject:"+ resultJSONObject.toString());
        JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp1.toJSONString());

        //之後須刪除
        checkResult = true;

        return checkResult;
    }

    @Override
    public void updateReceiveStation(ReceiveStation receiveStation){
        receiveStation.setUpdateUser(CustomConstants.UPDATE_USER);
        receiveStation.setUpdatedTime(new Date());

        Wrapper<ReceiveStation> wrapper = new EntityWrapper<>();
        wrapper.eq(ReceiveStation.HANDLE, receiveStation.getHandle());
        this.update(receiveStation, wrapper);
    }

    // 由PLC發起的檢查，確認任務是否執行完畢
    // 任務1 (使用按鈕): IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
    // 任務2 (使用出庫棧板): OUT-BINtoCV1、OUT-BINtoCV2、OUT-BINtoCV3
    // 任務3 (使用入庫棧板): IN-CV1toBIN、IN-CV2toBIN、IN-CV3toBIN
    // 任務4 (機械手臂): ROBOTIC_ARM
    // 任務5 (無人運輸車): AGV_TRANS
    // 任務6 (輸送帶移動): CNV_TRANS
    // sub任務: Transfer
    @Override
    public void plcCheckReceiveStation(String receiveStation, String station){
        // 取出輸送帶資訊
        ReceiveStation rs = receiveStationService.getReceiveStation(receiveStation);
        // 找到執行任務
        ReceiveStationTask receiveStationTask = receiveStationTaskService.findReceiveStationTaskForStatusAndEndStation(receiveStation, "START", station);
        //ReceiveStationTask receiveStationTask = receiveStationTaskService.findReceiveStationTaskForStart(receiveStation);

        // 若是任務為Transfer，則需要設定任務狀態，並且確認是否下一個任務，並處理下一個任務
        if((receiveStationTask!=null)&&("Transfer".equals(receiveStationTask.getType()))){
            // 找到最新任務的群組
            List<ReceiveStationTask> rstList = receiveStationTaskService.findReceiveStationTaskByMessageId(receiveStationTask.getMessageId());
            // 更新任務狀態
            if(rstList.size()>0){
                // 確認任務是哪一個
                for(int i=0; i<rstList.size();i++){
                    if(CustomConstants.START.equals(rstList.get(i).getStatus())){
                        receiveStationTask = rstList.get(i);
                        receiveStationTaskService.updateReceiveStationTask(receiveStationTask.getHandle(), CustomConstants.COMPLETE);
                        // 刪除receiveStationTask
                        // receiveStationTaskService.deleteTask(receiveStationTask.getHandle());
                        break;
                    }
                }

                boolean checkOtherTaskInSameMessageId = true;
                // 若任務仍有未完成的，則呼叫check
                for(int i=0; i<rstList.size();i++){
                    if(CustomConstants.NEW.equals(rstList.get(i).getStatus())){
                        receiveStationTaskService.checkReceiveStationTask(rstList.get(i).getMessageId(), rstList.get(i).getReceiveStation());
                        checkOtherTaskInSameMessageId = false;
                        break;
                    }
                }

                // 若沒有在相同MessageId的其他任務 根據EndStation確認棧板動作
                if(checkOtherTaskInSameMessageId){
                    // CV1
                    if("CV1".equals(station) && station.equals(receiveStationTask.getEndStation())){
                        // 更新輸送帶任務類型
                        rs.setStatus(CustomConstants.IDLE);
                        rs.setNowTask("");
                        if("OUT-BINtoCV1".equals(rs.getTaskGoal())){
                            rs.setTaskGoal("");
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            //完成須告知WMS
                            receiveStationTaskService.reportWMS("Storage.Bin.To.Conveyor.Ack", receiveStationTask.getMessageId()
                                    , "", receiveStationTask.getReceiveStation(), receiveStationTask.getPallet());
                        }
                        else if("CNV_TRANS".equals(rs.getTaskGoal())){
                            rs.setTaskGoal("");
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            //完成須告知WMS
                            receiveStationTaskService.reportWMS("conveyor.trans.Ack", receiveStationTask.getMessageId()
                                    , "", receiveStationTask.getReceiveStation(), receiveStationTask.getPallet());
                        }
                        else if("ROBOTIC_ARM".equals(rs.getTaskGoal())){
                            try{
                                // 結束輸送帶任務
                                roboticArmTaskService.endRoboticArmTask(receiveStation);
                            }catch (Exception e){
                                JSONObject JsonE = new JSONObject();
                                JsonE.put("QUEUE", "plcCheckReceiveStation - CV1 - ROBOTIC_ARM");
                                JsonE.put("MESSAGE_BODY", e.getMessage());
                                JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());
                            }
                        }

                        // 執行receiveStationTask
                        boolean result = receiveStationTaskService.checkReceiveStationTask
                                (receiveStationTask.getMessageId(), receiveStationTask.getReceiveStation());
                    }
                    // CV2
                    else if(CustomConstants.START.equals(receiveStationTask.getStatus()) && "CV2".equals(station)
                            && station.equals(receiveStationTask.getEndStation())){

                        // 更新輸送帶任務類型
                        rs.setStatus(CustomConstants.IDLE);
                        rs.setNowTask("");
                        if("OUT-BINtoCV2".equals(rs.getTaskGoal())){
                            // 更新輸送帶任務類型
                            rs.setTaskGoal("");
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            //完成須告知WMS
                            receiveStationTaskService.reportWMS("Storage.Bin.To.Conveyor.Ack", receiveStationTask.getMessageId()
                                    , "", receiveStationTask.getReceiveStation(), receiveStationTask.getPallet());
                        }
                        else if("IN-CV1toCV2".equals(rs.getTaskGoal())){
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            ButtonTask buttonTask = pressButtonService.findButtonTask(receiveStationTask.getReceiveStation());
                            if(buttonTask!=null){
                                //完成須告知WMS
                                pressButtonService.reportButtonTaskResult(buttonTask.getHandle());
                            }
                        }
                        else if("ROBOTIC_ARM".equals(rs.getTaskGoal())){
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            ButtonTask buttonTask = pressButtonService.findButtonTask(receiveStationTask.getReceiveStation());
                            if(buttonTask!=null){
                                // 2個棧板就定位即告知機械手臂
                                if(("7".equals(rs.getCv2PalletSensor()) || "14".equals(rs.getCv2PalletSensor()) || "15".equals(rs.getCv2PalletSensor())) &&
                                   ("6".equals(rs.getCv3PalletSensor()) || "12".equals(rs.getCv3PalletSensor()))){
                                    // 找尋機械手臂任務 並執行
                                    RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(buttonTask.getHandle());
                                    // 透過MQ發送給機械手臂
                                    roboticArmTaskService.sendRequestRoboticArm(roboticArmTask);
                                }
                            }
                        }
                        else if("CNV_TRANS".equals(rs.getTaskGoal())){
                            rs.setTaskGoal("");
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            // 完成須告知WMS
                            receiveStationTaskService.reportWMS("conveyor.trans.Ack", receiveStationTask.getMessageId()
                                    , "", receiveStationTask.getReceiveStation(), receiveStationTask.getPallet());
                        }
                        else if("IN-CV1toBIN".equals(rs.getTaskGoal()) || "IN-CV2toBIN".equals(rs.getTaskGoal()) ||
                                "IN-CV3toBIN".equals(rs.getTaskGoal())){
                            //預設處理時間
                            try{
                                long waitingTime = CustomConstants.WAITING_Conveyor_CarrierInfo;
                                Thread.sleep(new Long(waitingTime));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            // 告知棧板長寬高重
                            carrierTaskService.carrierInfoReport(receiveStationTask.getReceiveStation(), "CV2", receiveStationTask.getPallet());

                            // 棧板移到CV3
                            receiveStationTaskService.createReceiveStationTask(receiveStationTask.getMessageId()
                                    , receiveStationTask.getReceiveStation(), receiveStationTask.getPallet()
                                    ,"Transfer",station, "CV3", false);
                        }
                    }
                    // CV3
                    else if("START".equals(receiveStationTask.getStatus()) && "CV3".equals(station)
                            && station.equals(receiveStationTask.getEndStation())){
                        // 更新輸送帶任務類型
                        rs.setStatus(CustomConstants.IDLE);
                        rs.setNowTask("");

                        if("OUT-BINtoCV3".equals(rs.getTaskGoal())){
                            // 更新輸送帶任務類型
                            rs.setTaskGoal("");
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            //完成須告知WMS
                            receiveStationTaskService.reportWMS("Storage.Bin.To.Conveyor.Ack", receiveStationTask.getMessageId()
                                    , "", receiveStationTask.getReceiveStation(), receiveStationTask.getPallet());
                        }
                        if("IN-CV1toBIN".equals(rs.getTaskGoal()) || "IN-CV2toBIN".equals(rs.getTaskGoal()) ||
                                "IN-CV3toBIN".equals(rs.getTaskGoal())){
                            //發送WMS入庫請求
                            JSONObject wmsJson = new JSONObject();
                            wmsJson.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimemessageId());
                            wmsJson.put("MESSAGE_TYPE", CustomConstants.HANDLING_UNIT_IN_REQUEST);
                            wmsJson.put("CARRIER", receiveStationTask.getPallet());
                            wmsJson.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                            String wmsResponse = activeMqSendService.sendMsgNeedResponse4Wms(CustomConstants.HANDLING_UNIT_IN_REQUEST, wmsJson.toJSONString());
                            if (StringUtils.isBlank(wmsResponse)) {
                                throw new BusinessException("載具【" + receiveStationTask.getPallet() + "】入庫請求WMS未迴應");
                            }

                            JSONObject responseJson = JSON.parseObject(wmsResponse);
                            String result = responseJson.getString("RESULT");
                            if (!"1".equals(result)) {
                                throw new BusinessException(responseJson.getString("MESSAGE"));
                            }
                            String storageBin = responseJson.getString("STORAGE_BIN");

                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            //建立AGV入庫要求
                            carrierTaskService.createCarrierTask(receiveStationTask.getPallet(), CustomConstants.TYPE_IN
                                    , receiveStationTask.getReceiveStation(), storageBin, receiveStationTask.getMessageId());
                        }

                        ButtonTask buttonTask = pressButtonService.findButtonTask(receiveStationTask.getReceiveStation());
                        if(buttonTask!=null && "IN-CV1toCV3".equals(buttonTask.getType())){
                            // 更新輸送帶任務類型
                            rs.setTaskGoal("");
                            //更新資料
                            receiveStationService.updateReceiveStation(rs);
                            //完成須告知WMS
                            pressButtonService.reportButtonTaskResult(buttonTask.getHandle());
                            /*
                            // 2個棧板就定位即告知機械手臂
                            if("15".equals(rs.getCv2PalletSensor())){
                                // 找尋機械手臂任務 並執行
                                RoboticArmTask roboticArmTask = roboticArmTaskService.findRoboticArmTaskByID(buttonTask.getHandle());
                                // 透過MQ發送給機械手臂
                                roboticArmTaskService.sendRequestRoboticArm(roboticArmTask);
                            }*/
                        }
                    }

                    // 執行確認其他receiveStationTask
                    boolean result = receiveStationTaskService.checkReceiveStationTask
                            (receiveStationTask.getMessageId(), receiveStationTask.getReceiveStation());
                }
            }//End if(rstList.size()>0)
        }
        //更新資料
        //receiveStationService.updateReceiveStation(rs);
    }

    // 輸送帶狀態報告ASRS (先告知WMS  由WMS告知ASRS)
    // 因為WCS不會知道WO_SERIAL與VOUCHER_NO，這個要WMS才知道
    @Override
    public void reportASRS(String receiveStation, String type){

        JSONObject JsonStatus = new JSONObject();
        JsonStatus.put("MESSAGE_TYPE", "Device.Status.ASRS");
        JsonStatus.put("RESOURCE", receiveStation);
        JsonStatus.put("WO_SERIAL", "");
        JsonStatus.put("VOUCHER_NO", "");
        JsonStatus.put("CAPACITY", "");

        if(CustomConstants.IDLE.equals(type)){
            JsonStatus.put("STATUS", "0"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if(CustomConstants.WORKING.equals(type)){

            JsonStatus.put("STATUS", "3"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if(CustomConstants.ALARM.equals(type)){
            JsonStatus.put("STATUS", "2"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if(CustomConstants.ALARM_DISCONNECT.equals(type)){
            JsonStatus.put("STATUS", "1"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }

        JsonStatus.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Device.Status.ASRS", JsonStatus.toString());
    }
}