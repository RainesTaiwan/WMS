package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.Agv;
import com.fw.wcs.sys.model.AgvAlarm;
import com.fw.wcs.sys.mapper.AgvAlarmMapper;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.AgvService;
import com.fw.wcs.sys.service.AgvAlarmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
public class AgvAlarmServiceImpl extends ServiceImpl<AgvAlarmMapper, AgvAlarm> implements AgvAlarmService {


    @Autowired
    private AgvAlarmMapper agvAlarmMapper;
    @Autowired
    private AgvService agvService;
    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Override
    public Page<AgvAlarm> selectPage(FrontPage<AgvAlarm> frontPage, AgvAlarm agvAlarm) {
        EntityWrapper<AgvAlarm> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(agvAlarm);
        return super.selectPage(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<AgvAlarm> selectList(AgvAlarm agvAlarm) {
        EntityWrapper<AgvAlarm> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(agvAlarm);
        return super.selectList(queryWrapper);
    }

    @Override
    public List<AgvAlarm> getNoCompletedAlarm(String agvNo, String alarmCode) {
        return agvAlarmMapper.selectNoCompletedAlarm(agvNo, alarmCode);
    }

    @Override
    public void agvAlarm(String agvNo, String severity, String alarmCode, String description, String status) {
        Date nowDate = new Date();

        //State<> true : occur,false : release
        if ("true".equals(status)) {
            //警報記錄
            AgvAlarm agvAlarm = new AgvAlarm();
            agvAlarm.setHandle(UUID.randomUUID().toString());
            agvAlarm.setAgvNo(agvNo);
            agvAlarm.setSeverity(severity);
            agvAlarm.setAlarmCode(alarmCode);
            agvAlarm.setDescription(description);
            agvAlarm.setStatus(status);
            agvAlarm.setOccurTime(nowDate);
            agvAlarmMapper.insert(agvAlarm);

            // 告知ASRS AGV狀態為ALARM
            agvService.reportASRS(agvNo, "ALARM");
        } else {
            //報警解除
            List<AgvAlarm> list = this.getNoCompletedAlarm(agvNo, alarmCode);
            if (list != null && list.size() > 0) {
                AgvAlarm agvAlarm = list.get(0);
                agvAlarm.setStatus(status);
                agvAlarm.setReleaseTime(nowDate);
                agvAlarmMapper.updateById(agvAlarm);

                // 告知ASRS AGV狀態為IDLE
                agvService.reportASRS(agvNo, "IDLE");
            }
        }
    }


}