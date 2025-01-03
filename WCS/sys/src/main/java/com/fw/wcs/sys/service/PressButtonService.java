package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.ButtonTask;

public interface PressButtonService extends IService<ButtonTask>{
    //建立任務
    void createPressButtonTask(String handle, String receiveStation, String type);
    //找尋任務
    ButtonTask findButtonTask(String receiveStation);
    //透過HANDLE找尋任務
    ButtonTask findButtonTaskByID(String handle);


    // 執行任務
    void doPressButtonTask(String handle, String receiveStation, String type, String status);
    // 更新任務
    // void updateButtonTask(String handle, String receiveStation, String type, String status);
    // 完成任務
    void endPressButtonTask(String receiveStation);

    // 告知WMS完成
    void reportButtonTaskResult(String handle);
}
