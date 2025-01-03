package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.RoboticArmTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface RoboticArmTaskService extends IService<RoboticArmTask>{
    // 建立任務
    void createRoboticArmTask(String messageId, String voucherNo, String woSerial, String woQty, String doQty
                             , String fromPalletQty, String toPalletQty, String conveyor, String type);

    // 更新任務
    void updateRoboticArmTask(String handle, String status, String result);
    void updateRoboticArmTask(RoboticArmTask roboticArmTask);

    // 用MQID找尋任務
    RoboticArmTask findRoboticArmTaskByMessageID(String messageId);

    // 用Handle找尋任務
    RoboticArmTask findRoboticArmTaskByID(String handle);

    // 用輸送帶找尋"新"任務
    RoboticArmTask findRoboticArmTaskByResource(String resource);

    // 用VoucherNo找尋任務
    List<RoboticArmTask> findRoboticArmTaskByVoucherNo(String voucherNo);

    // 檢查指定輸送帶，若有任務待處理，則執行
    void checkDoRoboticArmTask(String resource);

    // 發送任務 - Request.Robotic.Arm
    void sendRequestRoboticArm(RoboticArmTask roboticArmTask);

    // 結束機械手臂任務
    void endRoboticArmTask(String resource);

    // 告知WMS任務完成
    void reportWMSRoboticArmTask(String handle, String result);

    // 模擬呼叫 Robotic.Arm.Report.WCS，Type有"ALL"、"PART"表示回傳數量要全部或部分
    void reportTasktoWCS(String handle, String type);

}
