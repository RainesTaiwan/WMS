package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.RFIDReader;

import java.util.List;

public interface RFIDReaderService extends IService<RFIDReader>{

    //透過 RFIDReader的ID 更新 RFIDReader的狀態
    void updateRFIDReaderStatus(String readerID, String status);

    //透過 receiveStation與station 查詢 RFIDReader的ID
    String getRFIDReaderID(String receiveStation, String station);

    //透過 RFIDReader的ID 查詢 receiveStation
    String getReceiveStation(String readerID);

}
