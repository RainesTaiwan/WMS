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
import com.fw.wcs.sys.mapper.ReceiveStationTaskMapper;
import com.fw.wcs.sys.model.ReceiveStationTask;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.model.PLCData;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.PLCDataService;
import com.fw.wcs.sys.service.ReceiveStationTaskService;
import com.fw.wcs.sys.service.ReceiveStationService;
import com.fw.wcs.sys.service.PressButtonService;
import com.fw.wcs.sys.service.RFIDReaderTaskService;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiveStationTaskServiceImpl extends ServiceImpl<ReceiveStationTaskMapper, ReceiveStationTask> implements ReceiveStationTaskService{

    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationTaskMapper receiveStationTaskMapper;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;
    @Autowired
    private PLCDataService plcDataService;
    @Autowired
    private PressButtonService pressButtonService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;

    // 刪除指定TASK
    @Override
    public void deleteTask(String handle){
        receiveStationTaskMapper.deleteTask(handle);
    }

    //透過輸送帶ID+狀態找尋任務
    @Override
    public ReceiveStationTask findReceiveStationTaskForStatus(String receiveStation, String status){
        List<ReceiveStationTask> list = receiveStationTaskMapper.findReceiveStationTaskByReceiveStationForStatus(receiveStation, "NEW");
        if(list==null || list.size()==0)  return null;
        else{
            ReceiveStationTask receiveStationTask = list.get(0);
            for(int i=0; i<list.size();i++){
                receiveStationTask = list.get(i);
                if(CustomConstants.NEW.equals(list.get(i).getStatus()) && CustomConstants.NEW.equals(receiveStationTask.getStatus())
                        && receiveStationTask.getCreatedTime().after(list.get(i).getCreatedTime())){
                    receiveStationTask = list.get(i);
                }
            }
            return receiveStationTask;
        }
    }

    //透過輸送帶ID+狀態+起始位置找尋任務
    @Override
    public ReceiveStationTask findReceiveStationTaskForStatusAndStartStation(String receiveStation, String status, String startStation){
        List<ReceiveStationTask> list =
                receiveStationTaskMapper.findReceiveStationTaskByReceiveStationForStatusAndStartStation(receiveStation, status, startStation);
        if(list==null || list.size()==0)  return null;
        else{
            return list.get(0);
        }
    }
    //透過輸送帶ID+狀態+起始位置找尋任務
    @Override
    public ReceiveStationTask findReceiveStationTaskForStatusAndEndStation(String receiveStation, String status, String endStation){
        List<ReceiveStationTask> list =
                receiveStationTaskMapper.findReceiveStationTaskByReceiveStationForStatusAndEndStation(receiveStation, status, endStation);
        if(list==null || list.size()==0)  return null;
        else{
            return list.get(0);
        }
    }

    //透過輸送帶ID找尋任務
    @Override
    public List<ReceiveStationTask> findReceiveStationTask(String receiveStation){
        return receiveStationTaskMapper.findReceiveStationTaskByReceiveStation(receiveStation);
    }

    //透過輸送帶ID找尋任務狀態為START
    @Override
    public ReceiveStationTask findReceiveStationTaskForStart(String receiveStation){
        List<ReceiveStationTask> list = receiveStationTaskMapper.findReceiveStationTaskByReceiveStation(receiveStation);
        if(list==null || list.size()==0)  return null;
        else{
            ReceiveStationTask receiveStationTask = new ReceiveStationTask();
            for(int i=0; i<list.size();i++){
                if(CustomConstants.START.equals(list.get(i).getStatus())){
                    receiveStationTask = list.get(i);
                }
            }
            return receiveStationTask;
        }
    }

    //透過MessageId找尋任務
    @Override
    public List<ReceiveStationTask> findReceiveStationTaskByMessageId(String messageId){
        return receiveStationTaskMapper.findReceiveStationTaskByMessageId(messageId);
    }

    // 建立/分割 任務
    // type任務類型：Transfer(輸送帶移動棧板)、GetPalletID(取得指定輸送帶位置的棧板ID)
    // 狀態: NEW、START、COMPLETE、ERROR_END
    @Override
    public void createReceiveStationTask(String messageId, String receiveStation, String pallet, String type
            , String startStation, String endStation, boolean needPalletInfoCheck){

        //任務的基本設定
        ReceiveStationTask receiveStationTask = new ReceiveStationTask();
        receiveStationTask.setMessageId(messageId);
        receiveStationTask.setReceiveStation(receiveStation);
        receiveStationTask.setType(type);
        receiveStationTask.setStatus("NEW");
        receiveStationTask.setCreateUser("ADMIN");
        receiveStationTask.setPallet(pallet);
        if(needPalletInfoCheck)   receiveStationTask.setPalletInfoCheck("Y");
        else    receiveStationTask.setPalletInfoCheck("N");

        // 棧板移動任務，只有在CV1->CV3或CV3->CV1才需要分割任務
        String handle = "RST_" + DateUtil.getDateTimeWithRandomNum();
        if("Transfer".equals(type)){
            if((("CV1".equals(startStation))&&("CV3".equals(endStation)))
               ||(("CV3".equals(startStation))&&("CV1".equals(endStation)))){
                receiveStationTask.setHandle(handle);
                receiveStationTask.setStartStation(startStation);
                receiveStationTask.setEndStation("CV2");
                receiveStationTask.setCreatedTime(new Date());
                // 將任務存到資料庫
                receiveStationTaskMapper.insert(receiveStationTask);
                handle = "RST_" + DateUtil.getDateTimeWithRandomNum();
                receiveStationTask.setHandle(handle);
                receiveStationTask.setStartStation("CV2");
                receiveStationTask.setEndStation(endStation);
                receiveStationTask.setCreatedTime(new Date());
                // 將任務存到資料庫
                receiveStationTaskMapper.insert(receiveStationTask);
            }
            else{
                receiveStationTask.setHandle(handle);
                receiveStationTask.setStartStation(startStation);
                receiveStationTask.setEndStation(endStation);
                receiveStationTask.setCreatedTime(new Date());
                // 將任務存到資料庫
                receiveStationTaskMapper.insert(receiveStationTask);
            }
        }
        else{
            receiveStationTask.setHandle(handle);
            receiveStationTask.setStartStation(startStation);
            receiveStationTask.setEndStation(endStation);
            receiveStationTask.setCreatedTime(new Date());
            // 將任務存到資料庫
            receiveStationTaskMapper.insert(receiveStationTask);
        }
        boolean result = this.checkReceiveStationTask(messageId, receiveStation);
    }

    // 檢查是否需執行任務 需要則回傳 true，不須則回傳 false
    // type任務類型：Transfer(輸送帶移動棧板)、GetPalletID(取得指定輸送帶位置的棧板ID)
    // 狀態: NEW、START、COMPLETE
    @Override
    public Boolean checkReceiveStationTask(String messageId, String receiveStation){
        boolean result = false;
        List<ReceiveStationTask> list = receiveStationTaskMapper.findReceiveStationTaskByReceiveStation(receiveStation);
        if(list!=null || list.size()>0){
            ReceiveStationTask receiveStationTaskConsiderMessageId = new ReceiveStationTask();
            ReceiveStationTask receiveStationTask = new ReceiveStationTask();
            // 確認沒有其他正在執行的任務
            for(int i=0; i<list.size();i++){
                if(CustomConstants.START.equals(list.get(i).getStatus())) return false;
                else if(CustomConstants.NEW.equals(list.get(i).getStatus())){
                    // 確認下一個需要執行的任務
                    if(receiveStationTask.getCreatedTime()==null)  receiveStationTask = list.get(i);
                    else if( receiveStationTask.getCreatedTime()!=null && list.get(i).getCreatedTime()!=null &&
                            receiveStationTask.getCreatedTime().after(list.get(i).getCreatedTime())){
                        receiveStationTask = list.get(i);
                    }

                    // 確認指定MessageId任務
                    if(messageId.equals(list.get(i).getMessageId()) && CustomConstants.NEW.equals(list.get(i).getStatus())){
                        if(receiveStationTaskConsiderMessageId.getCreatedTime()==null)    receiveStationTaskConsiderMessageId = list.get(i);
                        else if(receiveStationTaskConsiderMessageId.getCreatedTime()!=null && list.get(i).getCreatedTime()!=null &&
                                receiveStationTaskConsiderMessageId.getCreatedTime().after(list.get(i).getCreatedTime())) {
                            receiveStationTaskConsiderMessageId = list.get(i);
                        }
                    }
                }
            }
            // 若指定MessageId有任務未執行完畢
            if(receiveStationTaskConsiderMessageId.getCreatedTime()!=null){
                // 如果需要needPalletInfoCheck
                if(("Y".equals(receiveStationTaskConsiderMessageId.getPalletInfoCheck()))&&
                        ("CV2".equals(receiveStationTaskConsiderMessageId.getStartStation()))){
                    //感測棧板的長寬高重
                    result = receiveStationService.palletInfoCheck
                            (receiveStationTaskConsiderMessageId.getPallet(), receiveStationTaskConsiderMessageId.getReceiveStation());
                }
                receiveStationTaskConsiderMessageId.setStatus(CustomConstants.START);
                this.updateReceiveStationTaskByHandle(receiveStationTaskConsiderMessageId);
                if("Transfer".equals(receiveStationTaskConsiderMessageId.getType())){
                    // 起始終點一樣則不需做
                    if(receiveStationTaskConsiderMessageId.getStartStation().equals( receiveStationTaskConsiderMessageId.getEndStation())){
                        // 更新輸送帶任務狀態
                        //this.updateReceiveStationTask(receiveStationTaskConsiderMessageId.getHandle(), "COMPLETE");
                        // 刪除receiveStationTask
                        receiveStationTaskService.deleteTask(receiveStationTaskConsiderMessageId.getHandle());

                        // 若目標任務為入庫，則需要流向CV3
                        ReceiveStation rs = receiveStationService.getReceiveStation(receiveStationTaskConsiderMessageId.getReceiveStation());
                        if("IN-CV2toBIN".equals(rs.getTaskGoal())){
                            receiveStationTaskService.createReceiveStationTask(receiveStationTaskConsiderMessageId.getMessageId()
                                    , receiveStationTaskConsiderMessageId.getReceiveStation(), receiveStationTaskConsiderMessageId.getPallet()
                                    ,"Transfer","CV2", "CV3", false);
                        }
                    }
                    else{
                        // 更新輸送帶任務狀態
                        this.updateReceiveStationTask(receiveStationTaskConsiderMessageId.getHandle(), CustomConstants.START);
                        // 執行移動任務
                        this.doReceiveStationTaskTransfer(receiveStationTaskConsiderMessageId.getReceiveStation()
                                , receiveStationTaskConsiderMessageId.getStartStation(), receiveStationTaskConsiderMessageId.getEndStation());
                        // 更新輸送帶任務類型
                        ReceiveStation rs = receiveStationService.getReceiveStation(receiveStationTaskConsiderMessageId.getReceiveStation());
                        rs.setStatus(CustomConstants.WORKING);
                        rs.setNowTask("Transfer");
                        receiveStationService.updateReceiveStation(rs);
                    }
                }
                else if("GetPalletID".equals(receiveStationTaskConsiderMessageId.getType())){
                    // 建立RFID任務
                    // 會MessageId存到rfidReaderTask的voucherNo
                    // 透過 receiveStationTaskService 的 GetPalletID 才需要回傳WMS
                    rfidReaderTaskService.createRFIDReaderTask(receiveStationTaskConsiderMessageId.getMessageId()
                            , receiveStationTaskConsiderMessageId.getReceiveStation(), receiveStationTaskConsiderMessageId.getStartStation()
                            , receiveStationTaskConsiderMessageId.getType());
                }
                return true;
            }
            else if(receiveStationTask.getCreatedTime()!=null){
                // 如果需要needPalletInfoCheck
                if(("Y".equals(receiveStationTask.getPalletInfoCheck()))&&
                        ("CV2".equals(receiveStationTask.getStartStation()))){
                    //感測棧板的長寬高重
                    result = receiveStationService.palletInfoCheck(receiveStationTask.getPallet(), receiveStationTask.getReceiveStation());
                }
                receiveStationTask.setStatus(CustomConstants.START);
                this.updateReceiveStationTaskByHandle(receiveStationTask);
                if("Transfer".equals(receiveStationTask.getType())){
                    // 起始終點一樣則不需做
                    if(receiveStationTask.getStartStation().equals( receiveStationTask.getEndStation())){
                        // 更新輸送帶任務狀態
                        //this.updateReceiveStationTask(receiveStationTask.getHandle(), "COMPLETE");
                        // 刪除receiveStationTask
                        receiveStationTaskService.deleteTask(receiveStationTask.getHandle());

                        // 若目標任務為入庫，則需要流向CV3
                        ReceiveStation rs = receiveStationService.getReceiveStation(receiveStationTask.getReceiveStation());
                        if("IN-CV2toBIN".equals(rs.getTaskGoal())){
                            receiveStationTaskService.createReceiveStationTask(receiveStationTask.getMessageId()
                                    , receiveStationTask.getReceiveStation(), receiveStationTask.getPallet()
                                    ,"Transfer","CV2", "CV3", false);
                        }
                    }
                    else{
                        // 更新輸送帶任務狀態
                        this.updateReceiveStationTask(receiveStationTask.getHandle(), "START");
                        // 執行移動任務
                        this.doReceiveStationTaskTransfer(receiveStationTask.getReceiveStation()
                                , receiveStationTask.getStartStation(), receiveStationTask.getEndStation());
                        // 更新輸送帶任務類型
                        ReceiveStation rs = receiveStationService.getReceiveStation(receiveStationTask.getReceiveStation());
                        rs.setStatus(CustomConstants.WORKING);
                        rs.setNowTask("Transfer");
                        receiveStationService.updateReceiveStation(rs);
                    }
                }
                else if("GetPalletID".equals(receiveStationTask.getType())){
                    // 建立RFID任務
                    // 會MessageId存到rfidReaderTask的voucherNo
                    // 透過 receiveStationTaskService 的 GetPalletID 才需要回傳WMS
                    rfidReaderTaskService.createRFIDReaderTask(receiveStationTask.getMessageId()
                            , receiveStationTask.getReceiveStation(), receiveStationTask.getStartStation()
                            , receiveStationTask.getType());
                }
                return true;
            }
            else{
                return false;
            }
        }
        else   return false;
    }

    // 執行棧板移動任務
    @Override
    public void doReceiveStationTaskTransfer(String receiveStation, String startStation, String endStation){
        //確認輸送帶的PLC的lenNum
        String lenNum = "";
        if("Conveyor1".equals(receiveStation))  lenNum = "1";
        else if("Conveyor2".equals(receiveStation))  lenNum = "2";
        else if("Conveyor3".equals(receiveStation))  lenNum = "3";
        else if("Conveyor4".equals(receiveStation))  lenNum = "4";
        else if("Conveyor5".equals(receiveStation))  lenNum = "5";

        // 取出PLC資料
        PLCData plcData = plcDataService.findPLCDataByConveyor(receiveStation);
        int[] plcDataIntList = new int[100];
        if(plcData==null){
            for (int i = 0; i < 100; i++){
                plcDataIntList[i] = 0;
            }
        }
        else{
            // 取出PLC資料，轉成需要的格式
            String plcDataArray = "{\"DATA\":"+plcData.getData()+"}";
            JSONObject JsonNewplcData = JSON.parseObject(plcDataArray);
            JSONArray data = JsonNewplcData.getJSONArray("DATA");

            //int[] plcDataIntList = new int[data.size()];
            for (int i = 0; i < data.size(); i++){
                plcDataIntList[i] = Integer.parseInt(data.getString(i));
            }
        }

        //資料只寫D3100~D3119的資料 共20個點位
        int[] plcWriteIntArray = new int[20];
        Arrays.fill(plcWriteIntArray, 0); // 陣列值初始化

        // D3070-3081 對應CV1的 D3100~D3111的動作
        /* (for write)CV1的狀態
           D3100    DIRECTION(運輸方向)  1:CV1-->CV2(入庫)
           D3101	HOLD(設備工作遏制)    當HOLD值=0時，設備照常運作
                                        當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新
           D3102	MOVE(驅動棧板移動)
                    MOVE值=1時，設備依照DIRECTION值方向設定，驅動帶動電機工作，進行搬運動作；
                    運輸方向載口感測到棧板標識，停止帶動電機工作，自動恢復MOVE值=0；
        */
        /* (for write)CV2的狀態
           D3104	DIRECTION(運輸方向)   1:CV2-->CV3、2:CV2-->CV1
           D3105	HOLD(設備工作遏制)    當HOLD值=0時，設備照常運作
                                        當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新
           D3106	MOVE(驅動棧板移動)
                    MOVE值=1時，設備依照DIRECTION值方向設定，驅動帶動電機工作，進行搬運動作；
                    運輸方向載口感測到棧板標識，停止帶動電機工作，自動恢復MOVE值=0；
        */
        /* (for write)CV3的狀態
           D3108    DIRECTION(運輸方向) 1:CV3-->CV2
           D3109	HOLD(設備工作遏制)    當HOLD值=0時，設備照常運作
                                        當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新
           D3110	MOVE(驅動棧板移動)
                    MOVE值=1時，設備依照DIRECTION值方向設定，驅動帶動電機工作，進行搬運動作；
                    運輸方向載口感測到棧板標識，停止帶動電機工作，自動恢復MOVE值=0；
        */
        if(("CV1".equals(startStation))&&("CV2".equals(endStation))){
            plcWriteIntArray[0] = 1;
            plcWriteIntArray[1] = 0;
            plcWriteIntArray[2] = 1;
            plcWriteIntArray[3] = plcDataIntList[73]+1;
        }
        else if(("CV2".equals(startStation))&&("CV3".equals(endStation))){
            plcWriteIntArray[4] = 1;
            plcWriteIntArray[5] = 0;
            plcWriteIntArray[6] = 1;
            plcWriteIntArray[7] = plcDataIntList[77]+1;
        }
        else if(("CV2".equals(startStation))&&("CV1".equals(endStation))){
            plcWriteIntArray[4] = 2;
            plcWriteIntArray[5] = 0;
            plcWriteIntArray[6] = 1;
            plcWriteIntArray[7] = plcDataIntList[77]+1;
        }
        else if(("CV3".equals(startStation))&&("CV2".equals(endStation))){
            plcWriteIntArray[8] = 1;
            plcWriteIntArray[9] = 0;
            plcWriteIntArray[10] = 1;
            plcWriteIntArray[11] = plcDataIntList[81]+1;
        }
        // 在原有條件判斷結構中新增以下區塊
        else if(("CV3".equals(startStation))&&("CV3".equals(endStation))){
        // CV3控制寄存器地址對應陣列索引8-11
            plcWriteIntArray[8] = 1;    // D3108 DIRECTION (保持原有方向設定)
            plcWriteIntArray[9] = 0;    // D3109 HOLD 解除設備遏制
            plcWriteIntArray[10] = 1;   // D3110 MOVE 觸發短距離移動
            plcWriteIntArray[11] = plcDataIntList[81]+1; // 更新操作計數器
        }


        // 形成指定PLC資料，交由PLC執行
        plcDataService.writePLCData(lenNum, plcWriteIntArray);
        //plcDataService.SimulationPLCData(receiveStation, "Transfer", plcWriteIntArray);
    }

    // 更新任務狀態
    // 狀態: NEW、START、COMPLETE、ERROR_END
    @Override
    public void updateReceiveStationTask(String handle, String state){
        ReceiveStationTask receiveStationTask = new ReceiveStationTask();
        receiveStationTask.setHandle(handle);
        receiveStationTask.setStatus(state);
        receiveStationTask.setUpdateUser("ADMIN");
        receiveStationTask.setUpdatedTime(new Date());

        Wrapper<ReceiveStationTask> wrapper = new EntityWrapper<>();
        wrapper.eq(ReceiveStationTask.HANDLE, handle);
        this.update(receiveStationTask, wrapper);
    }

    // 更新任務
    @Override
    public void updateReceiveStationTaskByHandle(ReceiveStationTask receiveStationTask){
        receiveStationTask.setUpdateUser("ADMIN");
        receiveStationTask.setUpdatedTime(new Date());

        Wrapper<ReceiveStationTask> wrapper = new EntityWrapper<>();
        wrapper.eq(ReceiveStationTask.HANDLE, receiveStationTask.getHandle());
        this.update(receiveStationTask, wrapper);
    }

    // 輸送帶相關任務回報WMS
    @Override
    public void reportWMS(String mqName, String messageId, String storageBin, String resource, String palletId){
        // 回報WMS
        JSONObject JsonReport = new JSONObject();
        JsonReport.put("CORRELATION_ID", messageId);
        JsonReport.put("MESSAGE_TYPE", mqName);
        JsonReport.put("RESOURCE", resource);
        JsonReport.put("STORAGE_BIN", storageBin);
        JsonReport.put("PALLET_ID", palletId);
        JsonReport.put("RESULT", "1"); // 1表示完成任務
        JsonReport.put("SEND_TIME", LocalDateTime.now().toString()); //DateUtil.getDateTime()
        activeMqSendService.sendMsgNoResponse4Wms(mqName, JsonReport.toJSONString());
    }

}
