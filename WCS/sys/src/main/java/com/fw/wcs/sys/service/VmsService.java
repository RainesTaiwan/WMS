package com.fw.wcs.sys.service;

import com.alibaba.fastjson.JSONObject;

public interface VmsService {

    //WCS發送運輸任務
    //void sendTransportTask(String agvNo, String carrier, String taskType, String startPosition, String targetPosition);
    //VWS運輸任務上報
   // void agvTransportState(String agvNo, String status, String carrier);

    //WCS發送運輸任務 (給與指定TaskID)
    void sendTransportTaskWithTaskID(String taskID, String carrier, String taskType, String startPosition, String targetPosition);
    //VWS運輸任務上報 (給與指定TaskID)
    void agvTransportStateWithTaskID(String taskID, String agvNo, String status, String carrier);

    //模擬VMS
    void agvTransportCommand(JSONObject jsonObject);
}
