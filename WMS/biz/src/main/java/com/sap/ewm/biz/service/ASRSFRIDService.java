package com.sap.ewm.biz.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.AsrsRfid;

import java.util.List;

/**
 * ASRS訂單物料主數據 服務類
 *
 * @author Glanz
 * @since 2020-04-20
 */
public interface ASRSFRIDService extends IService<AsrsRfid> {

    // 放入資料庫
    void insertASRSRFID(AsrsRfid asrsRfid);

    // 確認資料庫該RFID不存在
    boolean isExistingAsrsRfidInDB(String handle);

    // 確認RFID是否存在重複值
    boolean hasDuplicateAsrsRfid(List<String> rfidList);

    // 要求將RFID資料傳給WCS
    boolean sentRFIDDataToWCS(String voucherNo, String operationType, String ioType, String carrier);

    // 找指定RFID資料
    AsrsRfid findRFIDByID(String rfidID);

    // 找所有符合HandlingID+指定狀態的RFID
    List<AsrsRfid> findRFIDByHandlingIDWithStatus(String handlingId, String status);

    // 找所有符合HandlingID的RFID
    List<AsrsRfid> findRFIDByHandlingID(String handlingId);

    // 找所有符合HandlingID的RFID
    List<AsrsRfid> findRFIDByWOSERIAL(String woSerial);

    // 找所有不完全出庫還在棧板上的RFID應該有的voucherNo
    String findHandlingIDForRemainRFID(String handlingId);

    // 找所有符合voucherNo的RFID
    List<AsrsRfid> findRFIDByVoucherNo(String voucherNo);

    // 找所有符合voucherNo+指定狀態的RFID
    List<AsrsRfid> findRFIDByVoucherNoWithStatus(String voucherNo, String status);

    // 找所有符合carrier+指定狀態的RFID
    List<AsrsRfid> findRFIDByCarrierWithStatus(String carrier, String status);

    // 狀態：Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION、OUT_STATION_NO_REPORT
    void updateRFIDStatus(List<String> rfidList, String pallet, String handlingId, String status);

    // 找所有符合HandlingID的RFID
    JSONArray findRFIDByHandlingID(String handlingId, String type);
    
    // 找所有符合HandlingID的RFID
    JSONArray findRFIDByWOSERIAL(String woSerial, String type);
    

}
