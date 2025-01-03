package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.RoboticArmTask;

import java.util.List;

/**
 * 機械手臂任務主數據 服務類
 *
 * @author Glanz
 * @since 2020-04-20
 */
public interface RoboticArmTaskService extends IService<RoboticArmTask>{

    // 分任務
    void listRoboticArmTask(String woSerial, String resource);

    // 發布任務，成功回傳1，失敗回傳0
    boolean deployRoboticArmTask(String woSerial);

    // 找尋任務
    RoboticArmTask findRoboticArmTaskByWoSerial(String woSerial);
    RoboticArmTask findRoboticArmTaskByHandle(String handle);

    // 更新任務
    void updateRoboticArmTask(RoboticArmTask roboticArmTask);

    // 新增任務
    void insertRoboticArmTask(RoboticArmTask roboticArmTask);

    // 工單箱型確認(PB1->0、PB2->1、PB3->2、PB4->3、XXX->4)
    int containerTypeForRoboticArmTask(String container);
}
