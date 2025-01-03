package com.fw.wcs.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.fw.wcs.sys.model.RFIDReaderMask;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.ArrayList;

public interface RFIDReaderMaskService extends IService<RFIDReaderMask>{
    // 刪除指定RFID清單
    void deleteRFID(String voucherNo);

    // 存放RFID清單
    void insertRFIDTags(String voucherNo, JSONArray DataList);

    // 更新RFID清單
    void updateRFIDTags(String voucherNo, JSONArray DataList, String pallet);

    // 找到指定工單的RFID清單
    List<RFIDReaderMask> findRFIDList(String voucherNo);

    //透過voucherNo找指定RFID MASK集合 告知WMS
    void reportReadRFIDList(String voucherNo);

    // 模擬RFID Reader上位機 - 給定WOSerial傳送RFID清單
    void SimulationRFIDReaderTransTags(String voucherNo, String messageId, String resource);
}
