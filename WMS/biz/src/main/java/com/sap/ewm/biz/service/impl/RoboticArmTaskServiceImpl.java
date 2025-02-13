package com.sap.ewm.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.MaterialRequisitionDTO;
import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.sap.ewm.biz.mapper.RoboticArmTaskMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import com.sap.ewm.core.utils.DateUtil;

import java.math.BigDecimal;


/**
 * 機械手臂任務主數據 服務實現類
 *
 * @author Glanz
 * @since 2020-04-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoboticArmTaskServiceImpl extends ServiceImpl<RoboticArmTaskMapper, RoboticArmTask> implements RoboticArmTaskService{
    @Autowired
    RoboticArmTaskMapper roboticArmTaskMapper;
    @Autowired
    RoboticArmTaskService roboticArmTaskService;
    @Autowired
    MessageSendService messageSendService;
    @Autowired
    ASRSOrderService asrsOrderService;
    @Autowired
    ASRSFRIDService asrsFRIDService;
    @Autowired
    MaterialRequisitionService materialRequisitionService;
    @Autowired
    CarrierTaskService carrierTaskService;
    @Autowired
    ShelfOffLogService shelfOffLogService;
    @Autowired
    ConveyorSerivce conveyorSerivce;

    // 更新任務
    @Override
    public void updateRoboticArmTask(RoboticArmTask roboticArmTask){
        roboticArmTask.setUpdater(CommonConstants.UPDATE_USER);
        roboticArmTask.setUpdateTime(LocalDateTime.now());

        QueryWrapper<RoboticArmTask> wrapper = new QueryWrapper<>();
        wrapper.eq(RoboticArmTask.HANDLE, roboticArmTask.getHandle());
        this.update(roboticArmTask, wrapper);
    }

    // 發布任務，成功回傳1，失敗回傳0
    @Override
    public boolean deployRoboticArmTask(String woSerial){
        boolean result = true;
        RoboticArmTask roboticArmTask = this.findRoboticArmTaskByWoSerial(woSerial);
        if(roboticArmTask==null){
            result = false;
        }
        else{ //要求WCS做任務
            // 發布之前要先確認輸送帶上棧板狀況，確認是否要增加CarrierTask，若不需要才可執行發布
            // 機械手臂任務TYPE為IN需要CV1、CV3有棧板；機械手臂任務TYPE為OUT需要CV2、CV3有棧板

            boolean carrierTaskCheck =
                    carrierTaskService.deployCarrierTask(roboticArmTask.getHandle(), null);
            if(carrierTaskCheck==false){
                // 發送ASRS RFID清單
                // 在每次要執行機械手臂任務時，才發送RFID清單給WCS(TYPE: ADD)；機械手臂任務結束後，馬上刪除位於WCS的RFID清單(TYPE: DELETE)
                boolean doSentRFIDDataToWCS =
                asrsFRIDService.sentRFIDDataToWCS(roboticArmTask.getVoucherNo(), CommonConstants.Operation_ADD
                        , roboticArmTask.getType(), roboticArmTask.getCarrier());

                if(doSentRFIDDataToWCS){
                    //發布任務
                    JSONObject JsonTask = new JSONObject();
                    JsonTask.put("MESSAGE_ID", roboticArmTask.getHandle());
                    JsonTask.put("MESSAGE_TYPE", CommonConstants.Request_RoboticArm);
                    JsonTask.put("VOUCHER_NO", roboticArmTask.getVoucherNo());
                    JsonTask.put("WO_SERIAL", roboticArmTask.getWoserial());

                    // 取出WO_QTY資料，轉成JSONArray格式
                    String dataArray = "{\"WO_QTY\":"+roboticArmTask.getWoQty()+"}";
                    JSONObject JsonNewData = JSON.parseObject(dataArray);
                    JSONArray data = JsonNewData.getJSONArray("WO_QTY");
                    JsonTask.put("WO_QTY", data);

                    // 取出DO_QTY資料，轉成JSONArray格式
                    dataArray = "{\"DO_QTY\":"+roboticArmTask.getDoQty()+"}";
                    JsonNewData = JSON.parseObject(dataArray);
                    data = JsonNewData.getJSONArray("DO_QTY");
                    JsonTask.put("DO_QTY", data);

                    // 取出FROM_PALLET_QTY資料，轉成JSONArray格式
                    dataArray = "{\"FROM_PALLET_QTY\":"+roboticArmTask.getFromPalletQty()+"}";
                    JsonNewData = JSON.parseObject(dataArray);
                    data = JsonNewData.getJSONArray("FROM_PALLET_QTY");
                    JsonTask.put("FROM_PALLET_QTY", data);

                    // 取出TO_PALLET_QTY資料，轉成JSONArray格式
                    dataArray = "{\"TO_PALLET_QTY\":"+roboticArmTask.getToPalletQty()+"}";
                    JsonNewData = JSON.parseObject(dataArray);
                    data = JsonNewData.getJSONArray("TO_PALLET_QTY");
                    JsonTask.put("TO_PALLET_QTY", data);

                    JsonTask.put("TYPE", roboticArmTask.getType());
                    JsonTask.put("RESOURCE", roboticArmTask.getResource());
                    JsonTask.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis());
                    messageSendService.send(CommonConstants.Request_RoboticArm, JsonTask);

                    // 更新機械手臂的狀態為START
                    roboticArmTask.setStatus(CommonConstants.STATUS_START);// 狀態有 NEW、START、END
                    roboticArmTaskService.saveOrUpdate(roboticArmTask);
                    //roboticArmTaskService.updateRoboticArmTask(roboticArmTask);
                }//End if(doSentRFIDDataToWCS)
            }
        }
        return result;
    }

    // 找尋任務
    // 任務狀態有 NEW、START、END
    @Override
    public RoboticArmTask findRoboticArmTaskByWoSerial(String woSerial){
        List<RoboticArmTask> list = roboticArmTaskMapper.findRoboticArmTaskByWoSerial(woSerial);
        RoboticArmTask roboticArmTask = new RoboticArmTask();
        RoboticArmTask roboticArmTaskCheck = new RoboticArmTask();
        if(list.size()>0){
            // 先找到已完成的最大TaskOrder  TaskOrder最小為1 若皆沒完成 則直接回傳序號為1的任務
            int maxTaskOrder = 0;
            int nowTaskOrder = 0;
            for(int i=0; i<list.size();i++){
                roboticArmTaskCheck = list.get(i);
                nowTaskOrder = Integer.parseInt(roboticArmTaskCheck.getTaskOrder());
                if((nowTaskOrder>maxTaskOrder)&&(CommonConstants.STATUS_END.equals(roboticArmTaskCheck.getStatus()))){
                    maxTaskOrder = nowTaskOrder;
                }
            }

            if(maxTaskOrder==list.size())   return null;
            // 回傳沒完成的中最小的TaskOrder
            String nextTaskOrder = String.valueOf(maxTaskOrder+1);
            for(int i=0; i<list.size();i++){
                roboticArmTaskCheck = list.get(i);
                if(nextTaskOrder.equals(roboticArmTaskCheck.getTaskOrder())){
                    roboticArmTask = list.get(i);
                    break;
                }
            }
        }
        return roboticArmTask;
    }
    // 找尋任務
    // 任務狀態有 NEW、START、END
    @Override
    public RoboticArmTask findRoboticArmTaskByHandle(String handle){ return roboticArmTaskMapper.findRoboticArmTaskByHandle(handle);
    }

    // 新增任務
    @Override
    public void insertRoboticArmTask(RoboticArmTask roboticArmTask){
        roboticArmTaskMapper.insert(roboticArmTask);
    }

    // 分任務
    @Override
    public void listRoboticArmTask(String woSerial, String resource){
        // 取得指定woSerial的ASRSOrder
        // 一個入庫單可能有多個憑單，但是每個憑單只有一個料號一種箱型
        // 出庫才會有箱型
        List<AsrsOrder> asrsOrderList = asrsOrderService.findAsrsOrderByWoSerial(woSerial);
        AsrsOrder asrsOrder = asrsOrderList.get(0);
        String woType = asrsOrder.getWoType();//確定出入庫類型

        String container = ""; //CONTAINER貨品包裝
        int qty = 0; //ITEM_COUNT憑單號處理量
        int[] totalQTY = new int[5];
        int[] tempQTY = new int[5];
        int setOrder; //順序
        int containerPointer = 0; //箱型指針
        //int[] containerBaseQTY = {4, 8, 18, 60, 84};  //此數據為2021/05/21驗測所得
        //int[] containerBaseQTY = {4, 8, 18, 40, 84};  //此數據為2021/06/11驗測所得
        int[] containerBaseQTY = {100, 100, 100, 100, 100};

        // 如果訂單超過1張，且為入庫單
        if(woType.equals(CommonConstants.OrderType1) || woType.equals(CommonConstants.OrderType5)){
            // 初始化totalQTY
            for(int i=0; i<5;i++){
                totalQTY[i] = 0;
                tempQTY[i] = 0;
            }

            // 同一份ASRS工單  有不同憑單號訂單 => 順序照排
            setOrder = 1;
            for(int i=0; i<asrsOrderList.size();i++){
                asrsOrder = asrsOrderList.get(i);
                container = asrsOrder.getContainer();
                qty = Integer.parseInt(asrsOrder.getItemCount());

                containerPointer = this.containerTypeForRoboticArmTask(container);
                totalQTY[containerPointer] = qty;

                RoboticArmTask roboticArmTask = new RoboticArmTask();
                roboticArmTask.setWoserial(woSerial);
                roboticArmTask.setVoucherNo(asrsOrder.getVoucherNo());
                roboticArmTask.setResource(resource);
                roboticArmTask.setFromPalletQty(Arrays.toString(tempQTY));
                roboticArmTask.setToPalletQty(Arrays.toString(tempQTY));
                roboticArmTask.setStatus(CommonConstants.STATUS_NEW);// 狀態有 NEW、START、END
                roboticArmTask.setCarrier("");
                roboticArmTask.setStorageBin("");
                roboticArmTask.setResult("");
                roboticArmTask.setType(CommonConstants.Type_IN); // 類型有 IN、OUT
                roboticArmTask.setCreator(CommonConstants.UPDATE_USER);
                roboticArmTask.setUpdater(CommonConstants.UPDATE_USER);

                while(qty>0){
                    roboticArmTask.setHandle(DateUtil.getDateTimeWithRandomNum());//"WCS_"+System.currentTimeMillis());
                    roboticArmTask.setTaskOrder(String.valueOf(setOrder));
                    for(int j=0; j<5;j++)   tempQTY[j] = 0;
                    if(qty<=containerBaseQTY[containerPointer]){
                        tempQTY[containerPointer] = qty;
                    }
                    else{
                        tempQTY[containerPointer] = containerBaseQTY[containerPointer];
                    }
                    roboticArmTask.setDoQty(Arrays.toString(tempQTY));
                    roboticArmTask.setWoQty(Arrays.toString(tempQTY));
                    roboticArmTask.setCreateTime(LocalDateTime.now());
                    roboticArmTask.setUpdateTime(LocalDateTime.now());

                    // 將任務存放至資料庫
                    roboticArmTaskService.save(roboticArmTask);
                    // roboticArmTaskMapper.insert(roboticArmTask);
                    qty = qty - containerBaseQTY[containerPointer];
                    setOrder = setOrder +1;
                }//End while(qty>0)
            }//End for(int i=0; i<asrsOrderList.size();i++)

            //確認輸送帶上棧板狀況，確認是否要增加CarrierTask
            //擇一機械手臂任務執行
            boolean result = this.deployRoboticArmTask(woSerial);
        }
        //如果訂單超過1張，且為出庫單
        else if(woType.equals(CommonConstants.OrderType6)){
            // 同一份ASRS工單  有不同憑單號訂單
            setOrder = 1;
            for(int i=0; i<asrsOrderList.size();i++){
                asrsOrder = asrsOrderList.get(i);
                container = asrsOrder.getContainer();
                qty = Integer.parseInt(asrsOrder.getItemCount());
                // 初始化totalQTY
                for(int j=0; j<5;j++){
                    totalQTY[j] = 0;
                }
                containerPointer = this.containerTypeForRoboticArmTask(container);

                //領料
                MaterialRequisitionDTO materialRequisitionDTO = new MaterialRequisitionDTO();
                materialRequisitionDTO.setItem(asrsOrder.getItem());
                BigDecimal summaryQty = new BigDecimal(asrsOrder.getItemCount());
                materialRequisitionDTO.setQty(summaryQty);
                String startLog = "{\"start\":\"materialRequisitionDTO :" + summaryQty.toString() +"\"}";
                JSONObject startLogObject = JSONObject.parseObject(startLog);
                messageSendService.send(CommonConstants.MQ_LOG, startLogObject);

                List<MaterialRequisitionStorageBinDTO> materialRequisitionStorageBinDTOList = materialRequisitionService.doMaterialRequisition("administrator", materialRequisitionDTO);
                JSONObject jsonObject2 = new JSONObject();
                try {
                for (int j=0; j<materialRequisitionStorageBinDTOList.size(); j++) {
                    String handle = DateUtil.getDateTimeWithRandomNum();
                    // 建立機械手臂任務 需設置貨物儲格與棧板
                    RoboticArmTask roboticArmTask = new RoboticArmTask();
                    roboticArmTask.setHandle(handle);
                    roboticArmTask.setWoserial(woSerial);
                    roboticArmTask.setVoucherNo(asrsOrder.getVoucherNo());
                    roboticArmTask.setResource(resource);//任務使用輸送帶
                    roboticArmTask.setTaskOrder(String.valueOf(setOrder));
                    roboticArmTask.setStorageBin(materialRequisitionStorageBinDTOList.get(j).getStorageBin()); //任務-此次貨物儲格
                    roboticArmTask.setCarrier(StringUtils.trimHandle(materialRequisitionStorageBinDTOList.get(j).getCarrierBo())); //任務-此次貨物棧板
                    //totalQTY[containerPointer] = materialRequisitionStorageBinDTOList.get(j).getQty().intValue();
                    int[] DoQty = {0, 2, 0, 0, 0};
                    roboticArmTask.setDoQty(Arrays.toString(DoQty));//任務-此次貨物要求數量
                    roboticArmTask.setWoQty(Arrays.toString(DoQty));//任務-貨物要求數量
                    //roboticArmTask.setDoQty(Arrays.toString(totalQTY));//任務-此次貨物要求數量
                    //roboticArmTask.setWoQty(Arrays.toString(totalQTY));//任務-貨物要求數量
                    int[] FromPalletQty = {0, 4, 0, 0, 0};
                    roboticArmTask.setFromPalletQty(Arrays.toString(FromPalletQty));
                    //roboticArmTask.setFromPalletQty(Arrays.toString(totalQTY));//任務-來料棧板數量(出庫用)
                    totalQTY[containerPointer] = 0;
                    roboticArmTask.setToPalletQty(Arrays.toString(totalQTY));//任務-機械手臂放置物料的棧板，目前的數量
                    roboticArmTask.setStatus(CommonConstants.STATUS_NEW);// 狀態有 NEW、START、END
                    roboticArmTask.setResult(""); // 結果有 COMPLETE、ERROR_END
                    roboticArmTask.setType(CommonConstants.Type_OUT); // 類型有 IN、OUT
                    roboticArmTask.setCreator(CommonConstants.UPDATE_USER);
                    roboticArmTask.setUpdater(CommonConstants.UPDATE_USER);
                    roboticArmTask.setCreateTime(LocalDateTime.now());
                    roboticArmTask.setUpdateTime(LocalDateTime.now());
                    roboticArmTaskService.save(roboticArmTask);


                    //String startLog = "{\"start\":\"save task message to roboticArm " + jsonObject2 +"\"}";
                    //JSONObject startLogObject = JSONObject.parseObject(startLog);
                    //messageSendService.send(CommonConstants.MQ_LOG, startLogObject);

                    ShelfOffLog shelfOffLog = new ShelfOffLog();
                    shelfOffLog.setmessageId(handle);
                    shelfOffLog.setCorrelationId(handle);
                    shelfOffLog.setActionCode("MR"); // marerial requisition
                    shelfOffLog.setCarrier(StringUtils.trimHandle(materialRequisitionStorageBinDTOList.get(j).getCarrierBo()));
                    shelfOffLog.setStorageLocation(StringUtils.trimHandle(materialRequisitionStorageBinDTOList.get(j).getStorageLocationBo()));
                    shelfOffLog.setStorageBin(materialRequisitionStorageBinDTOList.get(j).getStorageBin());
                    shelfOffLog.setInventoryBo(materialRequisitionStorageBinDTOList.get(j).getInventoryBo());
                    shelfOffLog.setQty(materialRequisitionStorageBinDTOList.get(j).getQty());
                    shelfOffLog.setSplit(materialRequisitionStorageBinDTOList.get(j).isSplit() ? CommonConstants.Y : CommonConstants.N);
                    shelfOffLog.setCreateTime(LocalDateTime.now());
                    shelfOffLogService.save(shelfOffLog);

                    jsonObject2 = new JSONObject();
                    startLog = "{\"start\":\"save shelfOffLog message to shelfOffLog " + jsonObject2 +"\"}";
                    startLogObject = JSONObject.parseObject(startLog);
                    messageSendService.send(CommonConstants.MQ_LOG, startLogObject);

                    setOrder = setOrder +1;
                }
            
                } catch (Exception e) {
                    // 測試失敗日誌
                    String errorLog = "{\"error\":\"Failed to save " + jsonObject2
                        + ", Error: " + e.getMessage() + "\"}";
                    JSONObject errorLogObject = JSONObject.parseObject(errorLog);
                    messageSendService.send(CommonConstants.MQ_LOG, errorLogObject);
                }

            }//End for(int i=0; i<asrsOrderList.size();i++)

            //擇一機械手臂任務執行
            boolean result = this.deployRoboticArmTask(woSerial);
        }

    }

    // 工單箱型確認(PB1->0、PB2->1、PB3->2、PB4->3、XXX->4)
    @Override
    public int containerTypeForRoboticArmTask(String container){
        int pointer = 0;

        if("PB1".equals(container)){
            pointer = 0;
        }
        else if("PB2".equals(container)){
            pointer = 1;
        }
        else if("PB3".equals(container)){
            pointer = 2;
        }
        else if("PB4".equals(container)){
            pointer = 3;
        }
        else if("XXX".equals(container)){
            pointer = 4;
        }

        return pointer;
    }
}
