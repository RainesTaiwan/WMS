package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.model.StorageLocation;
import com.fw.wcs.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WmsServiceImpl implements WmsService {

    @Autowired
    private CarrierTaskService carrierTaskService;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private StorageLocationService storageLocationService;
    @Autowired
    private ReceiveStationBindService receiveStationBindService;

    @Override
    public void carrierOutboundTask(String carrier, String storageBin, String correlationId) {
        //查詢出庫可用的接駁站
        ReceiveStation receiveStationModel = receiveStationService.getOutboundReceiveStation();
        if (receiveStationModel == null) {
            throw new BusinessException("沒有可使用的接駁站");
        }
        String receiveStation = receiveStationModel.getReceiveStation();

        //接駁站繫結載具
        receiveStationBindService.receiveStationBind(receiveStation, "CV1", carrier);

        //建立載具出庫任務
        carrierTaskService.createCarrierTask(carrier, CustomConstants.TYPE_OUT, storageBin, receiveStation, correlationId);

    }

    @Override
    public void carrierInStorage(String carrie, String storageBin) {
        //查詢貨格所在貨架
        String storageLocation = "";
        StorageLocation storageLocationModel = storageLocationService.getStorageLocationByBin(storageBin);
        if (storageLocationModel != null) {
            storageLocation = storageLocationModel.getStorageLocation();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimemessageId());
        jsonObject.put("MESSAGE_TYPE", CustomConstants.HANDLING_UNIT_IN_STORAGE);
        jsonObject.put("CARRIER", carrie);
        jsonObject.put("STORAGE_BIN", storageBin);
        jsonObject.put("STORAGE_LOCATION", storageLocation);
        jsonObject.put("SEND_TIME", DateUtil.getDateTime());

        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.HANDLING_UNIT_IN_STORAGE, jsonObject.toJSONString());
    }

    @Override
    public void carrierOutStorage(String carrie, String storageBin) {
        //查詢貨格所在貨架
        String storageLocation = "";
        StorageLocation storageLocationModel = storageLocationService.getStorageLocationByBin(storageBin);
        if (storageLocationModel != null) {
            storageLocation = storageLocationModel.getStorageLocation();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimemessageId());
        jsonObject.put("MESSAGE_TYPE", CustomConstants.HANDLING_UNIT_OUT_STORAGE);
        jsonObject.put("CARRIER", carrie);
        jsonObject.put("STORAGE_BIN", storageBin);
        jsonObject.put("STORAGE_LOCATION", storageLocation);
        jsonObject.put("SEND_TIME", DateUtil.getDateTime());

        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.HANDLING_UNIT_OUT_STORAGE, jsonObject.toJSONString());
    }
}
