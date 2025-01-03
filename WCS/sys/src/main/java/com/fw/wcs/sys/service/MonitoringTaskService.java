package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.MonitoringTask;

import java.util.List;

/**
 *  服務類
 *
 * @author Glanz
 *
 */
public interface MonitoringTaskService extends IService<MonitoringTask> {

    void sendMonitoringTask(String taskID, String type, String resource, String storageBin);
    void createMonitoringTask(String taskID, String type, String resource, String storageBin);
    void updateMonitoringTaskByID(MonitoringTask monitoringTask);
    MonitoringTask findMonitoringTaskByID(String taskID);
}
