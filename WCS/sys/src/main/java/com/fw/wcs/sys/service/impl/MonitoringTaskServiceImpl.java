package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.sys.mapper.MonitoringTaskMapper;
import com.fw.wcs.sys.model.CarrierTask;
import com.fw.wcs.sys.model.MonitoringTask;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.MonitoringTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.core.utils.StringUtils;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonitoringTaskServiceImpl extends ServiceImpl<MonitoringTaskMapper, MonitoringTask> implements MonitoringTaskService {

    @Autowired
    private MonitoringTaskMapper monitoringTaskMapper;
    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Override
    public void sendMonitoringTask(String taskID, String type, String resource, String storageBin){
        JSONObject JsonGen = new JSONObject();
        JsonGen.put("MESSAGE_ID", taskID);
        JsonGen.put("MESSAGE_TYPE", CustomConstants.MonitoringRequest);
        JsonGen.put("RESOURCE", resource);
        JsonGen.put("STORAGE_BIN", storageBin);
        JsonGen.put("TYPE", type);
        JsonGen.put("SEND_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Wms( CustomConstants.MonitoringRequest, JsonGen.toJSONString());
    }

    @Override
    public void createMonitoringTask(String taskID, String type, String resource, String storageBin){
        Date nowDate = new Date();
        MonitoringTask monitoringTask = new MonitoringTask();
        monitoringTask.setHandle(taskID);
        monitoringTask.setResource(resource);
        monitoringTask.setStorageBin(storageBin);
        monitoringTask.setType(type);
        monitoringTask.setStatus(CustomConstants.START);
        monitoringTask.setCreateUser(CustomConstants.CREATE_USER);
        monitoringTask.setCreatedTime(nowDate);  //LocalDateTime.now()
        monitoringTask.setUpdateUser(CustomConstants.UPDATE_USER);
        monitoringTask.setUpdatedTime(nowDate);
        monitoringTaskMapper.insert(monitoringTask);

        this.sendMonitoringTask(taskID, type, resource, storageBin);
    }

    @Override
    public void updateMonitoringTaskByID(MonitoringTask monitoringTask){
        Wrapper<MonitoringTask> wrapper = new EntityWrapper<>();
        wrapper.eq(MonitoringTask.HANDLE, monitoringTask.getHandle());
        this.update(monitoringTask, wrapper);
    }

    @Override
    public MonitoringTask findMonitoringTaskByID(String taskID){
        return monitoringTaskMapper.findMonitoringTaskByID(taskID);
    }

}
