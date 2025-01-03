package com.fw.wcs.sys.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.sys.mapper.RoboticArmMapper;
import com.fw.wcs.sys.mapper.RoboticArmTaskMapper;
import com.fw.wcs.sys.model.RoboticArm;
import com.fw.wcs.sys.model.RoboticArmTask;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.RoboticArmService;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoboticArmServiceImpl extends ServiceImpl<RoboticArmMapper, RoboticArm> implements RoboticArmService{
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private RoboticArmMapper roboticArmMapper;

    // 找尋指定機械手臂
    @Override
    public RoboticArm findRoboticArmByName(String RoboticArmName){
        RoboticArm roboticArm = roboticArmMapper.findRoboticArmByName(RoboticArmName);
        return roboticArm;
    }

    // 更新機械手臂狀態
    @Override
    public void updateRoboticArmStatus(String RoboticArmName, String serviceResource, String status){
        RoboticArm roboticArm = roboticArmMapper.findRoboticArmByName(RoboticArmName);
        roboticArm.setStatus(status);
        roboticArm.setServiceResource(serviceResource);
        roboticArm.setUpdateUser("ADMIN");
        roboticArm.setUpdatedTime(new Date());

        Wrapper<RoboticArm> wrapper = new EntityWrapper<>();
        wrapper.eq(RoboticArm.ROBOTIC_ARM, RoboticArmName);
        this.update(roboticArm, wrapper);
    }

    // 機械手臂狀態報告
    @Override
    public void reportASRS(String RoboticArmName, String type){
        // 若任務 RESULT 0: 任務完成、1: 專用儲籃裝滿但任務未完成、2: 物料拿取完畢但任務未完成、
        //              3: 發生異常(斷線、抓取物料失敗、機械手臂視覺故障、沒有感測到專用儲籃、任務錯誤、其他)
        // 若任務開始 type: WORKING

        JSONObject JsonStatus = new JSONObject();
        JsonStatus.put("MESSAGE_TYPE", "Device.Status.ASRS");
        JsonStatus.put("RESOURCE", RoboticArmName);
        JsonStatus.put("WO_SERIAL", "");
        JsonStatus.put("VOUCHER_NO", "");
        JsonStatus.put("CAPACITY", "");

        if("0".equals(type) || "1".equals(type) || "2".equals(type)){
            // 告知ASRS RoboticArm狀態為IDLE
            JsonStatus.put("STATUS", "0"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if("3".equals(type)){
            // 告知ASRS RoboticArm狀態為異常
            JsonStatus.put("STATUS", "2"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }
        else if("WORKING".equals(type)){
            // 告知ASRS RoboticArm狀態為工作
            JsonStatus.put("STATUS", "3"); //設備狀態 0：IDLE、1:網路異常、2:硬體異常(Default)、3：WORKING、4：CHARGING(只有AGV用)
        }

        JsonStatus.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Device.Status.ASRS", JsonStatus.toString());
    }
}
