package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.RoboticArm;

import java.util.List;

public interface RoboticArmService extends IService<RoboticArm>{
    // 找尋指定機械手臂
    RoboticArm findRoboticArmByName(String RoboticArmName);

    // 更新機械手臂狀態
    void updateRoboticArmStatus(String RoboticArmName, String serviceResource, String status);

    // 機械手臂狀態報告
    void reportASRS(String RoboticArmName, String type);
}
