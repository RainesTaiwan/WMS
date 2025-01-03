package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.RFIDReaderTask;

public interface RFIDReaderTaskService extends IService<RFIDReaderTask>{
    // 建立任務
    void createRFIDReaderTask(String voucherNo, String receiveStation, String station, String type);

    //透過MessageID找指定任務
    RFIDReaderTask findTaskByMessageID(String MessageID);

    //透過MessageID更新任務
    void updateTaskByMessageID(String MessageID, RFIDReaderTask rfidReaderTask);

    //透過rfid找指定任務
    RFIDReaderTask findTaskByRFID(String rfidId);

}
