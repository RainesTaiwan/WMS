package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.AsrsOrder;
import java.util.List;

/**
 * 訂單主數據 服務類
 *
 * @author Glanz
 * @since 2020-04-20
 */
public interface ASRSOrderService extends IService<AsrsOrder> {

    // 整棧入庫、零散件入庫、理貨入庫 訂單驗證
    boolean inStorageOrderVerifier(String mqText);

    // 零散件出庫、單箱出庫 訂單驗證
    boolean outStorageOrderVerifier(String mqText);

    // 整棧出庫、特定儲位抽盤 訂單驗證
    boolean checkOrderVerifier(String mqText);

    // 訂單錯誤字串
    String asrsOrderAlarmMessage(int type, String item);

    // 確認WO_SERIAL是否發生重複
    boolean woIdVerifier(String woId);

    // 確認MESSAGE_ID是否發生重複
    boolean messageIdVerifier(String messageId);

    // 確認VOUCHER_NO是否發生重複
    boolean voucherNoVerifier(String voucherNo);

    // 執行理貨入庫 (未完成)
    void asrsOrderInStorageBox(String messageId);
    // 執行散貨入庫 (未完成)
    void asrsOrderInStoragePart(String messageId);
    // 執行盤點 (未完成)
    void asrsOrderCheckInventory(String messageId);
    // 執行整棧出庫 (未完成)
    void asrsOrderOutStoragePallet(String messageId);
    // 執行理貨出庫 (未完成)
    void asrsOrderOutStorageBox(String messageId);
    // 執行散貨出庫 (未完成)
    void asrsOrderOutStoragePart(String messageId);
    //WO1工令
    void asrsOrderWO1(String messageId);

    // 透過ID找尋ASRSOrder
    //ASRSOrder findAsrsOrderByID(String handle);

    // 透過WoSerial找尋ASRSOrder
    List<AsrsOrder> findAsrsOrderByWoSerial(String woSerial);

    // 透過MessageId找尋ASRSOrder
    List<AsrsOrder> findAsrsOrderByMessageId(String messageId);

    // 選擇輸送帶
    String selectASRSConveyor(String messageId, String woSerial, String woType);

    // 找尋指定工單(ASRS Order)列表，改狀態與輸送帶
    void updateASRSOrderInfo(String woSerial, String conveyor, String status);

    // 結束工單
    void completeASRSOrder(String woSerial);
}
