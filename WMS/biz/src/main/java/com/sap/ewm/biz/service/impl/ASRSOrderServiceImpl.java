package com.sap.ewm.biz.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.mapper.ASRSOrderMapper;
import com.sap.ewm.biz.model.AsrsOrder;
import com.sap.ewm.biz.model.AsrsRfid;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sap.ewm.core.utils.DateUtil;
/**
 * 訂單主數據 服務實現類
 *
 * @author Glanz
 * @since 2020-04-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ASRSOrderServiceImpl extends ServiceImpl<ASRSOrderMapper, AsrsOrder> implements ASRSOrderService {
    @Autowired
    HandlingUnitService handlingUnitService;
    @Autowired
    MessageSendService messageSendService;
    @Autowired
    ASRSOrderMapper asrsOrderMapper;
    @Autowired
    ASRSFRIDService asrsFRIDService;
    @Autowired
    ASRSOrderService asrsOrderService;
    @Autowired
    private ConveyorSerivce conveyorSerivce;
    @Autowired
    private ItemService itemService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CarrierService carrierService;
    @Autowired
    RoboticArmTaskService roboticArmTaskService;

    // 找尋指定工單(ASRS Order)列表，改狀態與輸送帶
    // 檢查指定工單(ASRS Order)列表是否為指定狀態，若否則值皆改成指定狀態
    @Override
    public void updateASRSOrderInfo(String woSerial, String conveyor, String status){
        // 透過woSerial 選擇 ASRS_ORDER
        List<AsrsOrder> doList = asrsOrderMapper.findASRSOrderByWoSerial(woSerial);
        if(doList==null || doList.size()==0);
        else{
            AsrsOrder asrsOrder = new AsrsOrder();
            LocalDateTime now = LocalDateTime.now();
            for(int i=0;i<doList.size();i++){
                asrsOrder = doList.get(i);
                if(status.equals(asrsOrder.getStatus())==false){
                    //更新asrsOrder
                    asrsOrder.setStatus(status);
                    if(conveyor != null)    asrsOrder.setResource(conveyor);
                    asrsOrder.setUpdater(CommonConstants.UPDATE_USER);
                    asrsOrder.setUpdateTime(now);
                    asrsOrderService.saveOrUpdate(asrsOrder);
                }
            }
        }
    }

    // 選擇輸送帶
    @Override
    public String selectASRSConveyor(String messageId, String woSerial, String woType){
        // 回傳的字串
        String selectConveyor = "";

        // 透過woSerial 選擇 ASRS_ORDER
        List<AsrsOrder> ubeDoList = asrsOrderMapper.findASRSOrderByWoSerial(woSerial);
        // List<ASRSOrder> doList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(ubeDoList==null || ubeDoList.size()==0);
        else{
            AsrsOrder asrsOrder = new AsrsOrder();
            // 如果存在一個有給予"輸送帶"，則相同woSerial下皆使用相同"輸送帶"
            for(int i=0;i<ubeDoList.size();i++) {
                asrsOrder = ubeDoList.get(i);
                // 透過Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)確認
                if (CommonConstants.STATUS_ASSIGN.equals(ubeDoList.get(0).getStatus())) {
                    selectConveyor = asrsOrder.getResource();
                }
            }
            if("".equals(selectConveyor)==false){
                this.updateASRSOrderInfo(woSerial, selectConveyor, CommonConstants.STATUS_ASSIGN);
            }
            else{
                selectConveyor = conveyorSerivce.selectConveyor(woType);
                // 告知ASRS選擇結果
                JSONObject jsonSelectConveyor = new JSONObject();
                jsonSelectConveyor.put("MESSAGE_TYPE", CommonConstants.ASRS_Conveyor_Location);
                jsonSelectConveyor.put("CORRELATION_ID", messageId);
                jsonSelectConveyor.put("RESOURCE", selectConveyor);
                jsonSelectConveyor.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
                messageSendService.send(CommonConstants.ASRS_Conveyor_Location, jsonSelectConveyor);
                // 更新 輸送帶選擇結果 and Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                this.updateASRSOrderInfo(woSerial, selectConveyor, CommonConstants.STATUS_ASSIGN);
            }
        }
        return selectConveyor;
    }

    // 結束工單
    @Override
    public void completeASRSOrder(String woSerial){
        // 更新ASRS Order狀態 STATUS: NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
        asrsOrderService.updateASRSOrderInfo(woSerial, null, CommonConstants.STATUS_COMPLETE);
    }

    // 透過voucherNo找尋ASRSOrder
    @Override
    public List<AsrsOrder> findAsrsOrderByWoSerial(String woSerial){
        return asrsOrderMapper.findASRSOrderByWoSerial(woSerial);
    }
    // 透過MessageId找尋ASRSOrder
    @Override
    public List<AsrsOrder> findAsrsOrderByMessageId(String messageId){
        return asrsOrderMapper.findASRSOrderByMessageId(messageId);
    }

    // 整棧入庫、零散件入庫、理貨入庫 訂單驗證
    @Override
    public boolean inStorageOrderVerifier(String mqText){
        boolean valid = true;
        int alarmType = 0;
        String alarmMsg = "";

        JSONObject obj = JSONObject.parseObject(mqText);
        String messageId = obj.getString("MESSAGE_ID");
        String woSerial = obj.getString("WO_SERIAL");
        String woType = obj.getString("WO_TYPE");

        try {
            String sendTime = obj.getString("SEND_TIME");
            String goodsCount = obj.getString("GOODS_COUNT"); // 表示有多少憑單

            JSONArray vouchers = obj.getJSONArray("GOODS");
            List<String> voucherNoList= new ArrayList<>();
            List<String> itemsCountList= new ArrayList<>();
            List<String> itemTypeList= new ArrayList<>();
            List<String> rfidList= new ArrayList<>();
            List<String> netWeightList= new ArrayList<>();
            List<String> measureUnitList= new ArrayList<>();
            List<String> ContainerTypeList= new ArrayList<>();

            if(this.messageIdVerifier(messageId)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "MESSAGE_ID");
            }else if(this.woIdVerifier(woSerial)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "WO_SERIAL");
            }else if(goodsCount==null){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "GOODS_COUNT");
            }else if(vouchers.size()==0){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO/ITEMS_COUNT/ITEM_TYPE/ITEMS");
            }else if(sendTime==null){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "SEND_TIME");
            }
            else{
                for (int i = 0; i < vouchers.size(); i++){
                    JSONObject voucher = vouchers.getJSONObject(i);
                    String voucherNo = voucher.getString("VOUCHER_NO");//入庫憑單號
                    voucherNoList.add(voucherNo);
                    String itemsCount = voucher.getString("ITEMS_COUNT");//此憑單號處理量，校驗用
                    itemsCountList.add(itemsCount);
                    String itemType = voucher.getString("ITEM_TYPE");//貨品類型
                    itemTypeList.add(itemType);
                    JSONArray items = voucher.getJSONArray("ITEMS");
                    if(voucherNo==null){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO");
                        i = vouchers.size();
                        break;
                    }else if(this.voucherNoVerifier(voucherNo)==false){ //重複於資料庫
                        alarmType = 3;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO");
                        i = vouchers.size();
                        break;
                    }else if(itemsCount==null){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "ITEMS_COUNT");
                        i = vouchers.size();
                        break;
                    }else if(itemType==null){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "ITEM_TYPE");
                        i = vouchers.size();
                        break;
                    }else if(items.size()==0){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "RFID/NET_WEIGHT/MEASURE_UNIT/CONTAINER_TYPE");
                        i = vouchers.size();
                        break;
                    }else if(items.size()!=Integer.parseInt(itemsCount)){
                        alarmType = 6;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, voucherNo);
                        i = vouchers.size();
                        break;
                    }else{
                        for (int j = 0; j < items.size(); j++){
                            JSONObject item = items.getJSONObject(j);
                            String rfid = item.getString("RFID");//貨品ID
                            rfidList.add(rfid);
                            String netWeight = item.getString("NET_WEIGHT");//淨重
                            netWeightList.add(netWeight);
                            String measureUnit = item.getString("MEASURE_UNIT");//貨品度量單位
                            measureUnitList.add(measureUnit);
                            String containerType = item.getString("CONTAINER_TYPE");//貨品包裝
                            ContainerTypeList.add(containerType);

                            if(rfid==null){
                                alarmType = 1;
                                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "RFID");
                                i = vouchers.size();j = items.size();
                                break;
                            }else if(netWeight==null){
                                alarmType = 1;
                                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "NET_WEIGHT");
                                i = vouchers.size();j = items.size();
                                break;
                            }else if(measureUnit==null){
                                alarmType = 1;
                                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "MEASURE_UNIT");
                                i = vouchers.size();j = items.size();
                                break;
                            }else if(containerType==null){
                                alarmType = 1;
                                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "CONTAINER_TYPE");
                                i = vouchers.size();j = items.size();
                                break;
                            }
                        }//End  for (int j = 0; j < items.size(); j++)
                    }
                }// End for (int i = 0; i < vouchers.size(); i++)
            }

            if(asrsFRIDService.hasDuplicateAsrsRfid(rfidList)){
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "RFID");
            }

            LocalDateTime now = LocalDateTime.now();
            if(alarmType>0){
                valid = false;
                JSONObject alarmJSON = new JSONObject();
                alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
                alarmJSON.put("CORRELATION_ID", messageId);
                alarmJSON.put("WO_SERIAL", woSerial);
                alarmJSON.put("VOUCHER_NO", "3");
                alarmJSON.put("ALARM_TYPE", alarmType);
                alarmJSON.put("MSG", alarmMsg);
                alarmJSON.put("SEND_TIME", LocalDateTime.now().toString());//System.currentTimeMillis()
                messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);

                AsrsOrder asrsOrder = new AsrsOrder();
                asrsOrder.setHandle(StringUtils.createQUID());
                asrsOrder.setMessageId(messageId);
                asrsOrder.setWoSerial(woSerial);
                asrsOrder.setWoType(woType);
                asrsOrder.setStatus(CommonConstants.STATUS_UNCOMPLETE); //狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
                asrsOrder.setValidation(CommonConstants.VERIFY_INVALID);
                asrsOrder.setCreator(CommonConstants.CREATE_USER);
                asrsOrder.setCreateTime( now );
                asrsOrderService.save(asrsOrder);
                //asrsOrderMapper.insert(asrsOrder);
            }else{
                AsrsOrder asrsOrder = new AsrsOrder();
                AsrsRfid asrsRfid = new AsrsRfid();
                int items_start =0 , items_end =0;
                for (int i = 0; i < Integer.parseInt(goodsCount); i++){
                    String asrsOrderBo = StringUtils.createQUID();
                    asrsOrder.setHandle(asrsOrderBo);
                    asrsOrder.setMessageId(messageId);
                    asrsOrder.setWoSerial(woSerial);
                    asrsOrder.setWoType(woType);
                    asrsOrder.setStatus(CommonConstants.STATUS_NEW); //狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
                    asrsOrder.setOrderQTY(itemsCountList.get(i));
                    asrsOrder.setVoucherNo(voucherNoList.get(i));
                    asrsOrder.setItem(itemTypeList.get(i));
                    asrsOrder.setItemCount(itemsCountList.get(i));

                    //確認料號是否存在，不存在則存放至資料庫
                    itemService.itemVerify(itemTypeList.get(i), ContainerTypeList.get(items_start));

                    //將東西存放至 Inventory資料庫
                    inventoryService.saveASRSInStorageData(voucherNoList.get(i), itemTypeList.get(i), itemsCountList.get(i));

                    asrsRfid.setVoucherNo(voucherNoList.get(i));
                    asrsRfid.setAsrsOrderBo(asrsOrderBo);
                    asrsRfid.setCreator(CommonConstants.CREATE_USER);
                    asrsRfid.setCreateTime(now);
                    asrsRfid.setStatus(CommonConstants.STATUS_PROCESSING); //狀態：Processing、OnPallet、BindPallet、InStorage、OutStation

                    int itemCount = Integer.parseInt(itemsCountList.get(i));
                    items_end = items_end + itemCount;
                    List<String> rfidListTemp= new ArrayList<>();
                    for (int j = items_start; j < items_end; j++){
                        rfidListTemp.add(rfidList.get(j));

                        asrsRfid.setHandle(rfidList.get(j));
                        asrsRfid.setMeasureUnit(measureUnitList.get(j));
                        asrsRfid.setNetWeight(netWeightList.get(j));
                        asrsFRIDService.insertASRSRFID(asrsRfid);
                    }

                    asrsOrder.setContainer(ContainerTypeList.get(items_start));
                    asrsOrder.setValidation(CommonConstants.VERIFY_VALID);
                    asrsOrder.setCreator(CommonConstants.CREATE_USER);
                    asrsOrder.setCreateTime( now );
                    asrsOrderService.save(asrsOrder);
                    //asrsOrderMapper.insert(asrsOrder);
                    items_start = items_end;
                }// End for (int i = 0; i < Integer.parseInt(goodsCount); i++)
            }
        } catch (Exception e) {
            valid = false;
            alarmType = 1;
            alarmMsg = e.getMessage();

            JSONObject alarmJSON = new JSONObject();
            alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
            alarmJSON.put("CORRELATION_ID", messageId);
            alarmJSON.put("WO_SERIAL", woSerial);
            alarmJSON.put("VOUCHER_NO", "4");
            alarmJSON.put("ALARM_TYPE", alarmType);
            alarmJSON.put("MSG", alarmMsg);
            alarmJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis());
            messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);
        }
        return valid;
    }
    // 零散件出庫、單箱出庫 訂單驗證
    @Override
    public boolean outStorageOrderVerifier(String mqText){
        boolean valid = true;
        int alarmType = 0;
        String alarmMsg = "";

        JSONObject obj = JSONObject.parseObject(mqText);
        String messageId = obj.getString("MESSAGE_ID");
        String woSerial = obj.getString("WO_SERIAL");
        String woType = obj.getString("WO_TYPE");
        String storageBin = obj.getString("STORAGE_BIN");
        try {
            String sendTime = obj.getString("SEND_TIME");
            String goodsCount = obj.getString("GOODS_COUNT"); // 表示有多少憑單

            JSONArray vouchers = obj.getJSONArray("GOODS");
            List<String> voucherNoList= new ArrayList<>();
            List<String> itemsCountList= new ArrayList<>();
            List<String> itemTypeList= new ArrayList<>();
            List<String> rfidList= new ArrayList<>();
            List<String> netWeightList= new ArrayList<>();
            List<String> measureUnitList= new ArrayList<>();
            List<String> ContainerTypeList= new ArrayList<>();

            if(this.messageIdVerifier(messageId)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "MESSAGE_ID");
            }else if(this.woIdVerifier(woSerial)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "WO_SERIAL");
            }else if(goodsCount==null){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "GOODS_COUNT");
            }else if(vouchers.size()==0){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO/ITEMS_COUNT/ITEM_TYPE/ITEMS");
            }else if(sendTime==null){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "SEND_TIME");
            }
            else{
                for (int i = 0; i < vouchers.size(); i++){
                    JSONObject voucher = vouchers.getJSONObject(i);
                    String voucherNo = voucher.getString("VOUCHER_NO");//出庫憑單號
                    voucherNoList.add(voucherNo);
                    String itemsCount = voucher.getString("ITEMS_COUNT");//此憑單號處理量，校驗用
                    itemsCountList.add(itemsCount);
                    String itemType = voucher.getString("ITEM_TYPE");//貨品類型
                    itemTypeList.add(itemType);
                    if(voucherNo==null){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO");
                        i = vouchers.size();
                        break;
                    }else if(this.voucherNoVerifier(voucherNo)==false){ //重複於資料庫
                        alarmType = 3;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO");
                        i = vouchers.size();
                        break;
                    }else if(itemsCount==null){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "ITEMS_COUNT");
                        i = vouchers.size();
                        break;
                    }else if(itemType==null){
                        alarmType = 1;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "ITEM_TYPE");
                        i = vouchers.size();
                        break;
                    }
                    else if(itemService.itemExist(itemType)==false){// 確認料號是否存在，不存在則不可出庫
                        alarmType = 8;
                        alarmMsg = this.asrsOrderAlarmMessage(alarmType, "ITEM_TYPE");
                        i = vouchers.size();
                        break;
                    }
                    // 確認出庫量足夠 (未寫)
                }// End for (int i = 0; i < vouchers.size(); i++)
            }

            LocalDateTime now = LocalDateTime.now();
            if(alarmType>0){
                valid = false;
                JSONObject alarmJSON = new JSONObject();
                alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
                alarmJSON.put("CORRELATION_ID", messageId);
                alarmJSON.put("WO_SERIAL", woSerial);
                alarmJSON.put("VOUCHER_NO", "5");
                alarmJSON.put("ALARM_TYPE", alarmType);
                alarmJSON.put("MSG", alarmMsg);
                alarmJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis());
                messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);

                AsrsOrder asrsOrder = new AsrsOrder();
                asrsOrder.setHandle(StringUtils.createQUID());
                asrsOrder.setMessageId(messageId);
                asrsOrder.setWoSerial(woSerial);
                asrsOrder.setWoType(woType);
                asrsOrder.setStatus(CommonConstants.STATUS_UNCOMPLETE); //狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
                asrsOrder.setValidation(CommonConstants.VERIFY_INVALID);
                asrsOrder.setCreator(CommonConstants.CREATE_USER);
                asrsOrder.setCreateTime( now );
                //storageBin
                asrsOrder.setStorageBin( storageBin );
                asrsOrderService.save(asrsOrder);
                //asrsOrderMapper.insert(asrsOrder);
            }else{
                AsrsOrder asrsOrder = new AsrsOrder();
                for (int i = 0; i < Integer.parseInt(goodsCount); i++){
                    String asrsOrderBo = StringUtils.createQUID();
                    asrsOrder.setHandle(asrsOrderBo);
                    asrsOrder.setMessageId(messageId);
                    asrsOrder.setWoSerial(woSerial);
                    asrsOrder.setWoType(woType);
                    asrsOrder.setOrderQTY(itemsCountList.get(i));
                    asrsOrder.setVoucherNo(voucherNoList.get(i));
                    asrsOrder.setItem(itemTypeList.get(i));
                    asrsOrder.setItemCount(itemsCountList.get(i));
                    // 搜尋item資料庫，item資料庫中的item type指的是包裝型態
                    asrsOrder.setContainer(itemService.itemContainer(itemTypeList.get(i)));
                    asrsOrder.setStatus(CommonConstants.STATUS_NEW); //狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
                    asrsOrder.setValidation(CommonConstants.VERIFY_VALID);
                    asrsOrder.setCreator(CommonConstants.CREATE_USER);
                    asrsOrder.setCreateTime( now );
                    //storageBin
                    asrsOrder.setStorageBin( storageBin );
                    asrsOrderService.save(asrsOrder);
                    //asrsOrderMapper.insert(asrsOrder);
                }// End for (int i = 0; i < Integer.parseInt(goodsCount); i++)
            }
        } catch (Exception e) {
            valid = false;
            alarmType = 1;
            alarmMsg = e.getMessage();

            JSONObject alarmJSON = new JSONObject();
            alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
            alarmJSON.put("CORRELATION_ID", messageId);
            alarmJSON.put("WO_SERIAL", woSerial);
            alarmJSON.put("VOUCHER_NO", "6");
            alarmJSON.put("ALARM_TYPE", alarmType);
            alarmJSON.put("MSG", alarmMsg);
            alarmJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis());
            messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);
        }
        return valid;
    }
    // 整棧出庫、特定儲位抽盤 訂單驗證
    @Override
    public boolean checkOrderVerifier(String mqText){
        boolean valid = true;
        int alarmType = 0;
        String alarmMsg = "";

        AsrsOrder asrsOrder = new AsrsOrder();
        LocalDateTime now = LocalDateTime.now();

        JSONObject obj = JSONObject.parseObject(mqText);
        String messageId = obj.getString("MESSAGE_ID");
        String woSerial = obj.getString("WO_SERIAL");
        String woType = obj.getString("WO_TYPE");
        try {
            String sendTime = obj.getString("SEND_TIME");
            JSONArray vouchers = obj.getJSONArray("GOODS");
            String voucherNo = vouchers.getJSONObject(0).getString("VOUCHER_NO");//入庫憑單號
            String storageBin = obj.getString("STORAGE_BIN");

            if(this.messageIdVerifier(messageId)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "MESSAGE_ID");
            }else if(this.woIdVerifier(woSerial)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "WO_SERIAL");
            }else if(this.voucherNoVerifier(voucherNo)==false){ //重複於資料庫
                alarmType = 3;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO");
            }else if(voucherNo==null){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "VOUCHER_NO");
            }else if(storageBin==null){
                alarmType = 1;
                alarmMsg = this.asrsOrderAlarmMessage(alarmType, "STORAGE_BIN");
            }

            asrsOrder.setHandle(StringUtils.createQUID());
            asrsOrder.setMessageId(messageId);
            asrsOrder.setWoSerial(woSerial);
            asrsOrder.setWoType(woType);

            if(alarmType>0){
                valid = false;
                JSONObject alarmJSON = new JSONObject();
                alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
                alarmJSON.put("CORRELATION_ID", messageId);
                alarmJSON.put("WO_SERIAL", woSerial);
                alarmJSON.put("VOUCHER_NO", "1");
                alarmJSON.put("ALARM_TYPE", alarmType);
                alarmJSON.put("MSG", alarmMsg);
                alarmJSON.put("SEND_TIME", System.currentTimeMillis());
                messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);

                asrsOrder.setStatus(CommonConstants.STATUS_UNCOMPLETE); //狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
                asrsOrder.setValidation(CommonConstants.VERIFY_INVALID);
                asrsOrder.setCreator(CommonConstants.CREATE_USER);
                asrsOrder.setCreateTime( now );
                asrsOrderMapper.insert(asrsOrder);

            }//end if(alarmType>0)
            else{
                asrsOrder.setValidation(CommonConstants.VERIFY_VALID);
                asrsOrder.setStatus(CommonConstants.STATUS_NEW); //狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE
                asrsOrder.setVoucherNo(voucherNo);
                asrsOrder.setStorageBin(storageBin);
                asrsOrder.setCreator(CommonConstants.CREATE_USER);
                asrsOrder.setCreateTime( now );
                asrsOrderMapper.insert(asrsOrder);
            }
        } catch (Exception e) {
            valid = false;
            alarmType = 1;
            alarmMsg = e.getMessage();

            JSONObject alarmJSON = new JSONObject();
            alarmJSON.put("MESSAGE_TYPE", CommonConstants.ASRS_RequestAlarm);
            alarmJSON.put("CORRELATION_ID", messageId);
            alarmJSON.put("WO_SERIAL", woSerial);
            alarmJSON.put("VOUCHER_NO", "2");
            alarmJSON.put("ALARM_TYPE", alarmType);
            alarmJSON.put("MSG", alarmMsg);
            alarmJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis());
            messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON);
        }
        return valid;
    }

    // 確認WO_SERIAL是否發生重複
    @Override
    public boolean woIdVerifier(String woId){
        List<AsrsOrder> list = asrsOrderMapper.findASRSOrderByWoSerial(woId);
        if (list == null || list.size()==0) return true;
        else    return false;
    }

    // 確認MESSAGE_ID是否發生重複
    @Override
    public boolean messageIdVerifier(String messageId){
        List<AsrsOrder> list = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if (list == null || list.size()==0) return true;
        else    return false;
    }

    // 確認VOUCHER_NO是否發生重複
    @Override
    public boolean voucherNoVerifier(String voucherNo){
        List<AsrsOrder> list = asrsOrderMapper.findASRSOrderByVoucherNo(voucherNo);
        if (list == null || list.size()==0) return true;
        else    return false;
    }

    // 訂單錯誤字串
    @Override
    public String asrsOrderAlarmMessage(int type, String item){
        String alarmMsg = "";
        if(type==1) alarmMsg = item + " (必要)資訊為空";
        else if(type==2) alarmMsg = item + " 超出預設值/字串長度不對";
        else if(type==3) alarmMsg = item + " 重複於資料庫/工單內容";
        else if(type==4) alarmMsg = item + " 同一憑單號中貨品/料號不一致";
        else if(type==5) alarmMsg = item + " 數量不符合場域內現有數量";
        else if(type==6) alarmMsg = item + " 同一憑單號中貨品/料號與RFID的數量不一致";
        else if(type==7) alarmMsg = item + " 儲位不存在棧板";
        else if(type==8) alarmMsg = item + " 出庫物料不存在";
        else if(type==9) alarmMsg = item + " RFID Tags與機械手臂數量不一致";
        // 錯誤類型1: 必要資訊為空
        // 錯誤類型2: 超出預設值/字串長度不對
        // 錯誤類型3: 工單序號 woID/RFID Tags重複於資料庫/工單內容中
        // 錯誤類型4: 同一憑單號中貨品/料號不一致
        // 錯誤類型5: 物料數量不符合場域內現有數量
        // 錯誤類型6: 同一憑單號中貨品/料號與RFID的數量不一致
        // 錯誤類型7: 儲位不存在棧板
        // 錯誤類型8: 程序錯誤
        return alarmMsg;
    }
    // 執行WO1工令
    @Override
    public void asrsOrderWO1(String messageId){
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());

                    // 找到工單 PROCESSING 且 輸送帶一樣 則等待先不拆工單
                    List<AsrsOrder> asrsOrderDoList = asrsOrderMapper.findASRSOrderByResourceAndStatus(conveyor, CommonConstants.STATUS_PROCESSING);
                    if(asrsOrderDoList==null || asrsOrderDoList.size()==0){
                        // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                        // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                        asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);

                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("SEND_TIME",LocalDateTime.now().toString());
                        jsonObject2.put("MESSAGE_ID", asrsOrder.getMessageId());
                        jsonObject2.put("MESSAGE_TYPE", "Button.Task");
                        jsonObject2.put("TYPE", "WO01");
                        jsonObject2.put("RESOURCE", "Conveyor4");
                        messageSendService.sendMessage4Topic("Button.Task", jsonObject2);

                    }
                    
                }
            }
        }
    }
    // 執行理貨入庫
    @Override
    public void asrsOrderInStorageBox(String messageId){
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());

                    // 找到工單 PROCESSING 且 輸送帶一樣 則等待先不拆工單
                    List<AsrsOrder> asrsOrderDoList = asrsOrderMapper.findASRSOrderByResourceAndStatus(conveyor, CommonConstants.STATUS_PROCESSING);
                    if(asrsOrderDoList==null || asrsOrderDoList.size()==0){
                        // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                        // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                        asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);

                        // 機械手臂任務清單建立
                        roboticArmTaskService.listRoboticArmTask(asrsOrder.getWoSerial(), conveyor);
                    }
                    
                }
            }
        }
    }

    // 執行散貨入庫 (未完成)
    @Override
    public void asrsOrderInStoragePart(String messageId){
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            // CarrierTask任務順序，同一個messageId/woSerial要一致
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());

                    // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                    // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                    asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);
                }

                // CarrierTask任務建立
                // 1. 棧板到CV1 方法1:(使用按鈕Button.Task)PutPallet；方法2:(使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1
                // 2. 放置專用儲籃到棧板 (使用按鈕Button.Task)PutBasketOnPallet
                // 3. 放置物料到專用儲籃(手動) (使用按鈕Button.Task)PutMaterialInBasket (需要啟動RFID)
                // 4. 入庫棧板 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN
            }
        }
    }

    // 執行盤點 (未完成)
    @Override
    public void asrsOrderCheckInventory(String messageId) {
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            // CarrierTask任務順序，同一個messageId/woSerial要一致
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());

                    // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                    // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                    asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);
                }

                // CarrierTask任務建立
                // 1. 棧板到CV1 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1
                // 2. 確認棧板資料 (使用按鈕Button.Task)CheckInventory
                // 3. 入庫棧板 (使用入庫棧板Conveyor.To.Storage.Bin): IN-CV1toBIN
            }
        }
    }

    // 執行理貨出庫 (未完成)
    @Override
    public void asrsOrderOutStorageBox(String messageId){
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());
                    // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                    // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                    asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);

                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("MESSAGE_ID", asrsOrder.getMessageId());
                    jsonObject2.put("MESSAGE_TYPE", "Request.AGV");
                    jsonObject2.put("TASK_TYPE", "1");
                    jsonObject2.put("CARRIER", "ASRS_PALLET_00001");
                    jsonObject2.put("VEHICLE_ID",asrsOrder.getWoSerial());
                    jsonObject2.put("TO_NODE_NO", asrsOrder.getResource());
                    jsonObject2.put("FROM_NODE_NO",asrsOrder.getStorageBin());
                    //jsonObject2.put("TO_NODE_NO", "Conveyor4");
                    //jsonObject2.put("FROM_NODE_NO","C09R04L1");
                    //jsonObject2.put("SEND_TIME",LocalDateTime.now().toString());
                    messageSendService.sendMessage4Topic("WCS-AGV-2", jsonObject2);
                    messageSendService.sendMessage4Topic("MQ_LOG", jsonObject2);

                    // 機械手臂任務清單建立
                    roboticArmTaskService.listRoboticArmTask(asrsOrder.getWoSerial(), conveyor);
                    //JSONObject logUpdate = new JSONObject();
                    //logUpdate.put("MESSAGE_BODY", "Start to Raines.test ");
                    //logUpdate.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                    //messageSendService.send("Raines.test2", logUpdate);
                }
            }
        }
    }

    // 執行散貨出庫 (未完成)
    @Override
    public void asrsOrderOutStoragePart(String messageId){
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());

                    // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                    // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                    asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);
                }

                // 機械手臂任務清單建立
                //roboticArmTaskService.listRoboticArmTask(asrsOrder.getWoSerial(), conveyor);
            }
        }
    }

    // 執行整棧出庫 (未完成)
    @Override
    public void asrsOrderOutStoragePallet(String messageId){
        List<AsrsOrder> asrsOrderList = asrsOrderMapper.findASRSOrderByMessageId(messageId);
        if(asrsOrderList.size()!=0){
            // CarrierTask任務順序，同一個messageId/woSerial要一致
            for(int j=0;j<asrsOrderList.size();j++){
                AsrsOrder asrsOrder = asrsOrderList.get(j);
                // 選擇輸送帶
                String conveyor = asrsOrder.getResource();
                if("".equals(asrsOrder.getResource()) || asrsOrder.getResource()==null){
                    conveyor = this.selectASRSConveyor(asrsOrder.getMessageId(), asrsOrder.getWoSerial(), asrsOrder.getWoType());

                    // 確認指定ASRS ORDER狀態為PROCESSING，若沒有則改狀態
                    // asrsOrder Status(狀態：NEW、ASSIGN、PROCESSING、COMPLETE, UNCOMPLETE)
                    asrsOrderService.updateASRSOrderInfo(asrsOrder.getWoSerial(), null, CommonConstants.STATUS_PROCESSING);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("MESSAGE_ID", asrsOrder.getMessageId());
                    jsonObject2.put("MESSAGE_TYPE", "Request.AGV");
                    jsonObject2.put("TASK_TYPE", "1");
                    jsonObject2.put("CARRIER", "ASRS_PALLET_00001");
                    jsonObject2.put("VEHICLE_ID",asrsOrder.getWoSerial());
                    jsonObject2.put("TO_NODE_NO", asrsOrder.getResource());
                    jsonObject2.put("FROM_NODE_NO",asrsOrder.getStorageBin());
                    jsonObject2.put("SEND_TIME",LocalDateTime.now().toString());
                    messageSendService.sendMessage4Topic("WCS-AGV-2", jsonObject2);
                    JSONObject alarmJSON = new JSONObject();
                    alarmJSON.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
                    alarmJSON.put("MESSAGE_TYPE", "Button.Task");
                    alarmJSON.put("TYPE", "WO02");
                    alarmJSON.put("RESOURCE", "Conveyor4");
                    alarmJSON.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis());
                    messageSendService.send(CommonConstants.ASRS_RequestAlarm, alarmJSON); 
                }

                // CarrierTask任務建立
                // 1. 棧板到CV1 (使用出庫棧板Storage.Bin.To.Conveyor): OUT-BINtoCV1
                // 2. 棧板出站 (使用按鈕Button.Task): OutStation
            }
        }
    }
}