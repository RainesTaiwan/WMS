package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.core.utils.StringUtils;
import com.fw.wcs.sys.mapper.AgvMapper;
import com.fw.wcs.sys.mapper.AgvTaskMapper;
import com.fw.wcs.sys.mapper.ReceiveStationBindMapper;
import com.fw.wcs.sys.model.Agv;
import com.fw.wcs.sys.model.AgvTask;
import com.fw.wcs.sys.model.CarrierTask;
import com.fw.wcs.sys.model.ReceiveStationBind;
import com.fw.wcs.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Leon
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AgvTaskServiceImpl extends ServiceImpl<AgvTaskMapper, AgvTask> implements AgvTaskService {

    @Autowired
    private AgvService agvService;
    @Autowired
    private VmsService vmsService;
    @Autowired
    private AgvTaskMapper agvTaskMapper;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ReceiveStationBindService receiveStationBindService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;

    @Override
    public AgvTask getAgvTask(String agvNo) {
        return agvTaskMapper.selectAgvTask(agvNo);
    }

    //從任務ID找AgvTask
    @Override
    public AgvTask getAgvTaskByTaskID(String taskID) {
        return agvTaskMapper.selectAgvTaskByTaskID(taskID);
    }

    //從起始位置、終點位置找最新的AgvTask
    @Override
    public AgvTask getAgvTaskByPositionInfo(String startPosition, String targetPosition) {
        List<AgvTask> list = agvTaskMapper.selectAgvTaskByPositionInfo(startPosition, targetPosition);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public AgvTask createAgvTransportTaskWithTaskID(String taskID, String category, String carrier, String startPosition, String targetPosition) {
        Date nowDate = new Date();

        //AGV任務
        AgvTask agvTask = new AgvTask();
        agvTask.setHandle(taskID);
        agvTask.setCategory(category);
        agvTask.setCarrier(carrier);
        agvTask.setStartPosition(startPosition);
        agvTask.setTargetPosition(targetPosition);
        agvTask.setStatus(CustomConstants.NEW);
        agvTask.setCreateUser("ADMIN");
        agvTask.setCreatedTime(nowDate);
        agvTaskMapper.insert(agvTask);

        //TODO 發送AGV任務
        vmsService.sendTransportTaskWithTaskID(taskID, carrier, "TRANSPORT", startPosition, targetPosition);
        return agvTask;
    }
/*
    @Override
    public AgvTask createAgvTransportTask(String agvNo, String category, String carrier, String startPosition, String targetPosition) {
        Date nowDate = new Date();

        //AGV任務
        String agvTaskBo = "AGV" + DateUtil.getDateTimemessageId();
        AgvTask agvTask = new AgvTask();
        agvTask.setHandle(agvTaskBo);
        agvTask.setAgvNo(agvNo);
        agvTask.setCategory(category);
        agvTask.setCarrier(carrier);
        agvTask.setStartPosition(startPosition);
        agvTask.setTargetPosition(targetPosition);
        agvTask.setStatus(CustomConstants.NEW);
        agvTask.setCreateUser("ADMIN");
        agvTask.setCreatedTime(nowDate);
        agvTaskMapper.insert(agvTask);

        //修改AGV狀態
        Agv agvModel = new Agv();
        agvModel.setTaskBo(agvTaskBo);
        agvModel.setAgvNo(agvNo);
        agvModel.setStatus(CustomConstants.AGV_RUN);
        agvModel.setUpdateUser("ADMIN");
        agvModel.setUpdatedTime(nowDate);
        agvService.updateByAgvNo(agvNo, agvModel);

        //TODO 發送AGV任務
        vmsService.sendTransportTask(agvNo, carrier, "TRANSPORT", startPosition, targetPosition);

        return agvTask;
    }
*/
    @Override
    public void completeAgvTask(AgvTask agvTask) {
        Date nowDate = new Date();
        String agvNo = agvTask.getAgvNo();

        //AGV任務完成
        agvTask.setStatus(CustomConstants.COMPLETE);
        agvTask.setUpdateUser("ADMIN");
        agvTask.setUpdatedTime(nowDate);
        this.updateById(agvTask);

        try{
            // 告知ASRS AGV狀態為IDLE
            agvService.reportASRS(agvNo, CustomConstants.IDLE);
        }catch (Exception e){
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "completeAgvTask -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms("MQ_LOG", JsonE.toJSONString());
        }

        //AGV繫結任務解除
        Agv agv = new Agv();
        agv.setTaskId("");
        agv.setTaskType("");
        agv.setTaskStartTime(null);
        agv.setStatus(CustomConstants.AGV_IDLE);
        agvService.updateByAgvNo(agvNo, agv);

        // 如果AGV的任務CATEGORY為"OUT"，則需要檢查是否有輸送帶的任務要做，檢查receiveStationBind的
        if("OUT".equals(agvTask.getCategory())){
            //Binding INFO
            List<ReceiveStationBind> list = receiveStationBindService.findReceiveStationBind(agvTask.getTargetPosition(), agvTask.getCarrier());
            if (list != null) {
                ReceiveStationBind receiveStationBind = list.get(0);
                // 建立輸送帶移動任務
                if("CV1".equals(receiveStationBind.getStation())){
                    // 建立並執行任務
                    receiveStationTaskService.createReceiveStationTask(receiveStationBind.getHandle(), receiveStationBind.getReceiveStation()
                            , receiveStationBind.getCarrier(), "Transfer", "CV3", "CV1", false);

                    // 等待輸送帶移動 確認移動完畢 (等待35,000毫秒+30,000毫秒)
                    try{
                        //Thread.sleep(new Long(10000));//隨意設置可更動
                        long waitingTime = CustomConstants.WAITING_Conveyor_Transfer2;
                        Thread.sleep(new Long(waitingTime)); //預設10秒
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    boolean taskResult = receiveStationTaskService.checkReceiveStationTask(receiveStationBind.getHandle(), receiveStationBind.getReceiveStation());
                }
                else if("CV2".equals(receiveStationBind.getStation())){
                    // 建立並執行任務
                    receiveStationTaskService.createReceiveStationTask(receiveStationBind.getHandle(), receiveStationBind.getReceiveStation()
                            , receiveStationBind.getCarrier(), "Transfer", "CV3", "CV2", false);

                    //確認移動完畢 (等待35,000毫秒)
                    try{
                        //Thread.sleep(new Long(10000));//隨意設置可更動
                        long waitingTime = CustomConstants.WAITING_Conveyor_Transfer1;
                        Thread.sleep(new Long(waitingTime)); //預設10秒
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    boolean taskResult = receiveStationTaskService.checkReceiveStationTask(receiveStationBind.getHandle(), receiveStationBind.getReceiveStation());
                    //boolean taskResult = receiveStationTaskService.doReceiveStationTask(receiveStationBind.getHandle(), receiveStationBind.getReceiveStation(), receiveStationBind.getCarrier(), "Transfer", "CV3", "CV2");
                }
            }
        }
    }
}