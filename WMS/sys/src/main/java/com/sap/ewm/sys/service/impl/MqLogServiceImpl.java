package com.sap.ewm.sys.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.sys.model.MqLog;
import com.sap.ewm.sys.mapper.MqLogMapper;
import com.sap.ewm.sys.service.MqLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * MQ日誌 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-07-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MqLogServiceImpl extends ServiceImpl<MqLogMapper, MqLog> implements MqLogService {


    @Autowired
    private MqLogMapper mqLogMapper;

    @Override
    public IPage<MqLog> selectPage(FrontPage<MqLog> frontPage, MqLog mqLog) {
        QueryWrapper<MqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(mqLog);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<MqLog> selectList(MqLog mqLog) {
        QueryWrapper<MqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(mqLog);
        return super.list(queryWrapper);
    }


}