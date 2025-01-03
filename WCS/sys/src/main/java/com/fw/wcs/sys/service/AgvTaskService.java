package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.AgvTask;

import java.util.List;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface AgvTaskService extends IService<AgvTask> {

    AgvTask getAgvTask(String agvNo);
    //AgvTask createAgvTransportTask(String agvNo, String category, String carrier, String startPosition, String targetPosition);

    //從任務ID找AgvTask
    AgvTask getAgvTaskByTaskID(String taskID);
    //從起始位置、終點位置找最新的AgvTask
    AgvTask getAgvTaskByPositionInfo(String startPosition, String targetPosition);
    //WCS建立運輸任務 (給與指定TaskID)
    AgvTask createAgvTransportTaskWithTaskID(String taskID, String category, String carrier, String startPosition, String targetPosition);


    void completeAgvTask(AgvTask agvTask);
}