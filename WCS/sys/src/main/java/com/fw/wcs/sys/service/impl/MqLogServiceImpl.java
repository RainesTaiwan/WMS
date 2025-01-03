package com.fw.wcs.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.sys.mapper.MqLogMapper;
import com.fw.wcs.sys.model.MqLog;
import com.fw.wcs.sys.dto.WmsResponse;
import com.fw.wcs.sys.service.MqLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public class MqLogServiceImpl extends ServiceImpl<MqLogMapper, MqLog> implements MqLogService {


    @Autowired
    private MqLogMapper mqLogMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMqLog(MqLog mqLog, String requestText, WmsResponse wmsResponse) {
        mqLog.setHandle(UUID.randomUUID().toString());
        mqLog.setCreatedTime(new Date());
        mqLogMapper.insert(mqLog);
    }
}