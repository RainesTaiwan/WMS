package com.sap.ewm.biz.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.model.AsrsOrder;
import com.sap.ewm.biz.model.ReportAsrs;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.model.HandlingUnitLocation;
import com.sap.ewm.biz.service.ASRSFRIDService;
import com.sap.ewm.biz.service.ASRSOrderService;
import com.sap.ewm.biz.service.StorageBinService;
import com.sap.ewm.biz.service.HandlingUnitLocationService;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.sap.ewm.biz.mapper.ReportASRSMapper;
import com.sap.ewm.biz.service.ReportASRSService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 回報ASRS訂單結果要求主數據 服務實現類
 *
 * @author Glanz
 * @since 2020-08-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReportASRSServiceImpl  extends ServiceImpl<ReportASRSMapper, ReportAsrs> implements ReportASRSService{
    @Autowired
    MessageSendService messageSendService;
    @Autowired
    ReportASRSService reportASRSService;
    @Autowired
    ReportASRSMapper reportASRSMapper;
    @Autowired
    ASRSOrderService asrsOrderService;
    @Autowired
    ASRSFRIDService asrsRFIDService;
    @Autowired
    StorageBinService storageBinService;
    @Autowired
    HandlingUnitLocationService handlingUnitLocationService;

    // SAVE回報清單資訊
    @Override
    public void saveReportASRS(String carrier, String woSerial, String handlingId){
        ReportAsrs reportASRS = new ReportAsrs();
        reportASRS.setHandle(DateUtil.getDateTimeWithRandomNum());
        reportASRS.setCarrier(carrier);
        reportASRS.setWoSerial(woSerial);
        reportASRS.setHandlingId(handlingId);
        reportASRS.setCreator(CommonConstants.CREATE_USER);
        reportASRS.setCreateTime(LocalDateTime.now());
        reportASRSService.save(reportASRS);
    }
    // 回報ASRS指定工單內容WO.Result
@Override
public void reportASRS(String woSerial) {
    try {
        JSONObject logUpdate = new JSONObject();
        logUpdate.put("MESSAGE_BODY", "Start to reportASRS message to woSerial: " + woSerial);
        logUpdate.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        messageSendService.send(CommonConstants.MQ_LOG, logUpdate);
        List<ReportAsrs> carrierList = reportASRSMapper.findASRSOrderByWoSerial(woSerial);
        logUpdate.put("MESSAGE_BODY", "carrierList.get(i).getHandlingId(): " + carrierList.get(0).getHandlingId());
        logUpdate.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        messageSendService.send(CommonConstants.MQ_LOG, logUpdate);
        JSONObject JsonReport = new JSONObject();
        JsonReport.put("MESSAGE_TYPE", CommonConstants.ASRS_WOResult);
        JsonReport.put("WO_SERIAL", woSerial);

        List<AsrsOrder> asrsOrderList = asrsOrderService.findAsrsOrderByWoSerial(woSerial);
        JsonReport.put("CORRELATION_ID", asrsOrderList.get(0).getMessageId());

        JsonReport.put("RESULT", "0"); // 0: 成功、1: 失敗
        JsonReport.put("MSG", "");

        String inOutType = this.woTypeToInOutType(asrsOrderList.get(0).getWoType());
        String ioType = this.woTypeToIOType(asrsOrderList.get(0).getWoType());
        JSONArray rfidList1 = asrsRFIDService.findRFIDByWOSERIAL(woSerial, inOutType);
        logUpdate.put("MESSAGE_BODY", "rfidList: " + rfidList1 + " inOutType" + inOutType);
        logUpdate.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        messageSendService.send(CommonConstants.MQ_LOG, logUpdate);
        JSONArray groupOutside = new JSONArray();
        boolean hasRFID = false; // 用於檢查是否有非空的 RFID
        for (int i = 0; i < carrierList.size(); i++) {
            String carrier = carrierList.get(i).getCarrier();
            JSONObject groupInside = new JSONObject();
            // 決定IO類型
            groupInside.put("IO", ioType);
            // 棧板
            groupInside.put("CARRIER", carrier);
            // RFID
            JSONArray rfidList = asrsRFIDService.findRFIDByWOSERIAL(woSerial, inOutType);
            groupInside.put("RFID", rfidList);
            if (rfidList != null && !rfidList.isEmpty()) {
                hasRFID = true; // 如果有非空的 RFID，設定為 true
            }
            // 庫位
            HandlingUnitLocation handlingUnitLocation = handlingUnitLocationService.getByCarrier(carrier);
            if (handlingUnitLocation == null) {
                groupInside.put("STORAGE_BIN", "");
            } else {
                StorageBin storageBin = storageBinService.findByHandle(handlingUnitLocation.getBindContextGbo());
                groupInside.put("STORAGE_BIN", storageBin.getStorageBin());
            }
            groupOutside.add(groupInside);
        }
        JsonReport.put("GROUP", groupOutside);

        // 檢查是否有非空的 RFID，只有在有 RFID 時才發送訊息
        if (hasRFID) {
            messageSendService.send(CommonConstants.ASRS_WOResult, JsonReport);
        }

    } catch (Exception e) {
        // 捕捉所有例外並記錄錯誤日誌
        JSONObject jsonObject2 = new JSONObject();
        String errorLog = "{\"error\":\"Failed to publish test message to topic: " + "reportASRS" + jsonObject2
        + ", Error: " + e.getMessage() + "\"}";
        JSONObject errorLogObject = JSONObject.parseObject(errorLog);
        messageSendService.send(CommonConstants.MQ_LOG, errorLogObject);
    }
}

    // IO 類型決定
    @Override
    public String woTypeToIOType(String woType){
        String ioType = "O";
        if(CommonConstants.OrderType1.equals(woType) || CommonConstants.OrderType3.equals(woType)
                || CommonConstants.OrderType5.equals(woType)){
            ioType = "I";
        }
        else if(CommonConstants.OrderType7.equals(woType)){
            ioType = "P";
        }
        return ioType;
    }

    // IN、OUT 類型決定
    @Override
    public String woTypeToInOutType(String woType){
        String ioType = CommonConstants.Type_OUT;
        if(CommonConstants.OrderType1.equals(woType) || CommonConstants.OrderType3.equals(woType)
                || CommonConstants.OrderType5.equals(woType) || CommonConstants.OrderType7.equals(woType)){
            ioType = CommonConstants.Type_IN;
        }
        return ioType;
    }
}
