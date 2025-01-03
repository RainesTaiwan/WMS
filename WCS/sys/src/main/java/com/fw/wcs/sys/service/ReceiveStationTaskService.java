package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.ReceiveStationTask;
import java.util.Date;
import java.util.List;

/**
 *  服務類
 *
 * @author Glanz
 *
 */
public interface ReceiveStationTaskService extends IService<ReceiveStationTask>{

    //透過輸送帶ID找尋任務
    List<ReceiveStationTask> findReceiveStationTask(String receiveStation);

    //透過輸送帶ID+狀態找尋任務
    ReceiveStationTask findReceiveStationTaskForStatus(String receiveStation, String status);

    //透過輸送帶ID+狀態+起始位置找尋任務
    ReceiveStationTask findReceiveStationTaskForStatusAndStartStation(String receiveStation, String status, String startStation);

    //透過輸送帶ID+任務狀態為START找尋任務
    ReceiveStationTask findReceiveStationTaskForStart(String receiveStation);

    //透過輸送帶ID+狀態+終點位置找尋任務
    ReceiveStationTask findReceiveStationTaskForStatusAndEndStation(String receiveStation, String status, String endStation);

    //透過MessageId找尋任務
    List<ReceiveStationTask> findReceiveStationTaskByMessageId(String messageId);

    // 建立任務 並執行
    void createReceiveStationTask(String messageId, String receiveStation, String pallet, String type
                                 , String startStation, String endStation, boolean needPalletInfoCheck);

    // 檢查是否需執行任務 需要則回傳 true，不須則回傳 false
    Boolean checkReceiveStationTask(String messageId, String receiveStation);

    // 執行棧板移動任務
    void doReceiveStationTaskTransfer(String receiveStation, String startStation, String endStation);

    // 更新任務狀態
    // 狀態: NEW、START、COMPLETE、ERROR_END
    void updateReceiveStationTask(String handle, String state);
    // 更新任務
    void updateReceiveStationTaskByHandle(ReceiveStationTask receiveStationTask);

    // 刪除指定TASK
    void deleteTask(String handle);

    // 輸送帶相關任務回報WMS
    void reportWMS(String mqName, String messageId, String storageBin, String resource, String palletId);

}
