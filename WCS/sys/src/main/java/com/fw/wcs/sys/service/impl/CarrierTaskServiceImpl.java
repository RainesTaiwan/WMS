package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.core.utils.StringUtils;
import com.fw.wcs.sys.mapper.CarrierTaskMapper;
import com.fw.wcs.sys.model.CarrierTask;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
public class CarrierTaskServiceImpl extends ServiceImpl<CarrierTaskMapper, CarrierTask> implements CarrierTaskService {


    @Autowired
    private AgvService agvService;
    @Autowired
    private AgvTaskService agvTaskService;
    @Autowired
    private CarrierTaskMapper carrierTaskMapper;
    @Autowired
    private CarrierTaskService carrierTaskService;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationBindService receiveStationBindService;


    @Override
    public List<CarrierTask> findCarrierTask(String carrier, String status) {
        return carrierTaskMapper.findCarrierTask(carrier, status);
    }

    @Override
    public List<CarrierTask> findNoCompletedTask(String carrier, String category, String startPosition) {
        return carrierTaskMapper.findNoCompletedTask(carrier, category, startPosition);
    }

    // 取得棧板長寬高重，告知WMS
    @Override
    public void carrierInfoReport(String resource, String station, String carrier){
        ReceiveStation receiveStation = receiveStationService.getReceiveStation(resource);
        JSONObject JsonCarrierInfo = new JSONObject();
        JsonCarrierInfo.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        JsonCarrierInfo.put("MESSAGE_TYPE", CustomConstants.Conveyor_CarrierInfo);
        JsonCarrierInfo.put("CARRIER", carrier);
        JsonCarrierInfo.put("RESOURCE", resource);
        JsonCarrierInfo.put("STATION", station);
        JsonCarrierInfo.put("HEIGHT", receiveStation.getHeight());
        JsonCarrierInfo.put("WIDTH", receiveStation.getWidth());
        JsonCarrierInfo.put("LENGTH", receiveStation.getLength());
        JsonCarrierInfo.put("WEIGHT", receiveStation.getWeight());
        JsonCarrierInfo.put("SEND_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.Conveyor_CarrierInfo, JsonCarrierInfo.toJSONString());
    }

    @Override
    public void createCarrierTask(String carrier, String category, String startPosition, String targetPosition, String wmsId) {
        CarrierTask carrierTask = new CarrierTask();
        if(wmsId==null) wmsId = DateUtil.getDateTimeWithRandomNum(); //因為入庫create為null值
        carrierTask.setHandle(wmsId);
        carrierTask.setCarrier(carrier);
        carrierTask.setCategory(category);
        carrierTask.setStartPosition(startPosition);
        carrierTask.setTargetPosition(targetPosition);
        carrierTask.setStatus(CustomConstants.NEW);

        // 建立AGV任務
        //String taskID = "WcsMagID_"+DateUtil.getDateTimemessageId();
        //String taskID = "WCS_"+DateUtil.getDateTimemessageId();
        agvTaskService.createAgvTransportTaskWithTaskID(wmsId, category, carrier, startPosition, targetPosition);
        /*
        //查詢是否有可用AGV
        Agv agvModel = agvService.getCanTransportAgv();
        if (agvModel != null) {
            String agvNo = agvModel.getAgvNo();
            carrierTask.setAgvNo(agvNo);
            carrierTask.setStatus(CustomConstants.START);

            //建立AGV任務
            agvTaskService.createAgvTransportTask(agvNo, category, carrier, startPosition, targetPosition);
        }*/

        //建立載具任務
        carrierTask.setWmsId(wmsId);
        carrierTask.setCreateUser(CustomConstants.CREATE_USER);
        carrierTask.setCreatedTime(new Date());
        carrierTaskMapper.insert(carrierTask);
    }

    @Override
    public void updateCarrierTaskAgvInfo(String carrier, String agvNo) {
        CarrierTask carrierTask = new CarrierTask();
        carrierTask.setAgvNo(agvNo);
        carrierTask.setUpdateUser(CustomConstants.UPDATE_USER);
        carrierTask.setUpdatedTime(new Date());

        Wrapper<CarrierTask> wrapper = new EntityWrapper<>();
        wrapper.eq(CarrierTask.CARRIER, carrier);
        wrapper.eq(CarrierTask.STATUS, CustomConstants.NEW);
        this.update(carrierTask, wrapper);
    }

    @Override
    public void updateCarrierTaskStatus(String carrier, String category, String startPosition, String status) {
        CarrierTask carrierTask = new CarrierTask();
        carrierTask.setStatus(status);
        carrierTask.setUpdateUser(CustomConstants.UPDATE_USER);
        carrierTask.setUpdatedTime(new Date());

        Wrapper<CarrierTask> wrapper = new EntityWrapper<>();
        wrapper.eq(CarrierTask.CARRIER, carrier);
        wrapper.eq(CarrierTask.CATEGORY, category);
        wrapper.eq(CarrierTask.START_POSITION, startPosition);
        this.update(carrierTask, wrapper);
    }

    @Override
    public void carrierOnMachine(String resource, String type, String station, String carrier) {
        ReceiveStation receiveStationModel = receiveStationService.getReceiveStation(resource);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", "carrierOnMachine");
        JsonTemp.put("MESSAGE_BODY", "resource:"+resource+", station:"+station);
        JsonTemp.put("CREATED_DATE_TIME", DateUtil.getDateGMT8Time());
        activeMqSendService.sendMsgNoResponse4Wms("MQ_LOG", JsonTemp.toJSONString());

        if (receiveStationModel == null) {
            throw new BusinessException("接駁站編號【" +resource+ "】不存在");
        }

        if ("IN".equals(type)) {
            receiveStationBindService.receiveStationBind(resource, station, carrier);
            if ("CV1".equals(station)) {
                // 模擬呼叫carrier.info.report
                JSONObject JsonCarrierInfo = new JSONObject();
                JsonCarrierInfo.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimemessageId());
                JsonCarrierInfo.put("MESSAGE_TYPE", CustomConstants.Conveyor_CarrierInfo);
                JsonCarrierInfo.put("CARRIER", carrier);
                JsonCarrierInfo.put("RESOURCE", resource);
                JsonCarrierInfo.put("STATION", "CV2");
                JsonCarrierInfo.put("HEIGHT", "13.5");
                JsonCarrierInfo.put("WIDTH", "14.5");
                JsonCarrierInfo.put("LENGTH", "15.5");
                JsonCarrierInfo.put("WEIGHT", "16.5");
                JsonCarrierInfo.put("SEND_TIME", DateUtil.getDateTime());
                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.Conveyor_CarrierInfo, JsonCarrierInfo.toJSONString());
            }
            if ("CV3".equals(station)) {
                List<CarrierTask> list = this.findNoCompletedTask(carrier, CustomConstants.TYPE_IN, resource);
                if (list != null && list.size() > 0) {
                    return;
                }

                //發送WMS入庫請求
                JSONObject wmsJson = new JSONObject();
                wmsJson.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimemessageId());
                wmsJson.put("MESSAGE_TYPE", CustomConstants.HANDLING_UNIT_IN_REQUEST);
                wmsJson.put("CARRIER", carrier);
                wmsJson.put("SEND_TIME", DateUtil.getDateTime());
                String wmsResponse = activeMqSendService.sendMsgNeedResponse4Wms(CustomConstants.HANDLING_UNIT_IN_REQUEST, wmsJson.toJSONString());
                if (StringUtils.isBlank(wmsResponse)) {
                    throw new BusinessException("載具【" + carrier + "】入庫請求WMS未迴應");
                }

                JSONObject responseJson = JSON.parseObject(wmsResponse);
                String result = responseJson.getString("RESULT");
                if (!"1".equals(result)) {
                    throw new BusinessException(responseJson.getString("MESSAGE"));
                }
                String storageBin = responseJson.getString("STORAGE_BIN");

                this.createCarrierTask(carrier, CustomConstants.TYPE_IN, resource, storageBin, null);
            }
        } else if ("OUT".equals(type)) {
            if ("CV1".equals(station)) {
                receiveStationBindService.receiveStationBind(resource, station, carrier);

                //模擬呼叫carrier.on.machine
                JSONObject JsonCarrierOnStation = new JSONObject();
                JsonCarrierOnStation.put("MESSAGE_ID", "WCS_" + DateUtil.getDateTimemessageId());
                JsonCarrierOnStation.put("MESSAGE_TYPE", "carrier.on.machine");
                JsonCarrierOnStation.put("RESOURCE", "Conveyor3");
                JsonCarrierOnStation.put("TYPE", "OUT");
                JsonCarrierOnStation.put("STATION", "CV3");
                JsonCarrierOnStation.put("CARRIER", carrier);
                JsonCarrierOnStation.put("SEND_TIME", DateUtil.getDateTime());
                activeMqSendService.sendMsgNoResponse4Wms("carrier.on.machine", JsonCarrierOnStation.toJSONString());

            } else if ("CV3".equals(station)) {
                receiveStationBindService.receiveStationUnBind(resource, carrier);

                List<CarrierTask> list = this.findNoCompletedTask(carrier, CustomConstants.TYPE_OUT, null);
                if (list == null || list.size() <= 0) {
                    throw new BusinessException("載具【" + carrier + "】沒有正在執行的任務");
                }
                CarrierTask carrierTaskModel = list.get(0);

                carrierTaskModel.setStatus(CustomConstants.COMPLETE);
                carrierTaskModel.setUpdatedTime(new Date());
                this.updateById(carrierTaskModel);

                //notice wms
                JSONObject wmsJson = new JSONObject();
                wmsJson.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
                wmsJson.put("MESSAGE_TYPE", CustomConstants.HANDLING_UNIT_OUT_STATION);
                wmsJson.put("CARRIER", carrier);
                wmsJson.put("ACTION", "MR");
                wmsJson.put("CORRELATION_ID", carrierTaskModel.getWmsId());
                wmsJson.put("SEND_TIME", DateUtil.getDateGMT8Time());
                activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.HANDLING_UNIT_OUT_STATION, wmsJson.toJSONString());

            }
        }
    }
}