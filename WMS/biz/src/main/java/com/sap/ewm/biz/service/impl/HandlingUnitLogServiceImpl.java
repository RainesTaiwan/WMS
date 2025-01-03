package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitLog;
import com.sap.ewm.biz.mapper.HandlingUnitLogMapper;
import com.sap.ewm.biz.service.HandlingUnitLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 處理單元建立日誌記錄 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HandlingUnitLogServiceImpl extends ServiceImpl<HandlingUnitLogMapper, HandlingUnitLog> implements HandlingUnitLogService {


    @Autowired
    private HandlingUnitLogMapper handlingUnitLogMapper;

    @Override
    public IPage<HandlingUnitLog> selectPage(FrontPage<HandlingUnitLog> frontPage, HandlingUnitLog handlingUnitLog) {
        QueryWrapper<HandlingUnitLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLog);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<HandlingUnitLog> selectList(HandlingUnitLog handlingUnitLog) {
        QueryWrapper<HandlingUnitLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLog);
        return super.list(queryWrapper);
    }


}