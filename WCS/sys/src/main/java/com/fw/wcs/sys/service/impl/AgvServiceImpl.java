package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.mapper.AgvMapper;
import com.fw.wcs.sys.model.Agv;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.AgvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

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
public class AgvServiceImpl extends ServiceImpl<AgvMapper, Agv> implements AgvService {

    @Autowired
    private AgvMapper agvMapper;
    @Autowired
    private ActiveMqSendService activeMqSendService;

    // 找到指定AGV
    @Override
    public Agv findAGV(String agvNo){
        return agvMapper.findAgv(agvNo);
    }

    @Override
    public Agv getCanTransportAgv() {
        List<Agv> list = agvMapper.selectCanTransportAgv();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateByAgvNo(String agvNo, Agv agv) {
        agv.setUpdateUser("ADMIN");
        agv.setUpdatedTime(new Date());
        Wrapper<Agv>wrapper = new EntityWrapper<>();
        wrapper.eq(Agv.AGV_NO, agvNo);
        this.update(agv, wrapper);
        //agvMapper.updateByAgvNo(agvNo, agv);
    }

    @Override
    public void updateAgvStatus(String agvNo, String status) {
        Agv agvModel = new Agv();
        agvModel.setStatus(status);
        agvModel.setUpdateUser("ADMIN");
        agvModel.setUpdatedTime(new Date());
        this.updateByAgvNo(agvNo, agvModel);
    }

    // AGV狀態報告
    @Override
    public void reportASRS(String agvNo, String type){

        Agv agv = this.findAGV(agvNo);

        JSONObject JsonStatus = new JSONObject();
        JsonStatus.put("MESSAGE_TYPE", "Device.Status.ASRS");
        JsonStatus.put("RESOURCE", agvNo);
        JsonStatus.put("WO_SERIAL", "");
        JsonStatus.put("VOUCHER_NO", "");
        if(agv.getCapacity()==null) JsonStatus.put("CAPACITY", null);
        else JsonStatus.put("CAPACITY", agv.getCapacity());

        if("IDLE".equals(type)){
            JsonStatus.put("STATUS", "0"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if("WORKING".equals(type)){
            JsonStatus.put("STATUS", "3"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if("ALARM".equals(type)){
            JsonStatus.put("STATUS", "2"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }

        JsonStatus.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Device.Status.ASRS", JsonStatus.toString());

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", "Device.Status.ASRS - AGV");
        JsonTemp.put("MESSAGE_BODY", JsonStatus.toString());
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toString());
    }
}