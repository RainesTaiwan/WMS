package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.CarrierTask;

import java.util.List;

/**
 * 棧板任務主數據 服務類
 *
 * @author Glanz
 * @since 2021-06-02
 */
public interface CarrierTaskService extends IService<CarrierTask>{
    // 依照RoboticArmTask任務類型對輸送帶檢查棧板位置 (需透過woSerial找RoboticArmTask)
    // 若沒有需要新增CarrierTask
    CarrierTask checkCarrierForRoboticArmTask(String rArmTaskId);

    // 透過機械手臂任務ID找CarrierTask
    CarrierTask findCarrierTaskByRArmTaskId(String rArmTaskId);

    // 透過woSerial找CarrierTask
    CarrierTask findCarrierTaskByWoSerial(String woSerial);

    // 找到CarrierTask中任務序號最大的值

    // 透過任務ID找CarrierTask
    CarrierTask findCarrierTaskByHandle(String handle);

    // 發布機械手臂相關任務，成功回傳1，失敗回傳0
    boolean deployCarrierTask(String rArmTaskId, String woSerial);

    // 更新任務
    // 任務狀態STATUS: NEW、START、END
    void updateCarrierTask(CarrierTask carrierTask);

    // 新增任務
    void insertCarrierTask(CarrierTask carrierTask);

    // 給定指定任務內容 新增
    void insertCarrierTask(String woType, String voucherNo, String woSerial, String rArmTaskId
            , String carrier, String resource, String startStation, String endStation
            , String storageBin, String taskType, String taskOrder);
}
