package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitChangeLog;
import com.sap.ewm.biz.mapper.HandlingUnitChangeLogMapper;
import com.sap.ewm.biz.service.HandlingUnitChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 處理單元變更日誌記錄 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HandlingUnitChangeLogServiceImpl extends ServiceImpl<HandlingUnitChangeLogMapper, HandlingUnitChangeLog> implements HandlingUnitChangeLogService {


    @Autowired
    private HandlingUnitChangeLogMapper handlingUnitChangeLogMapper;

    @Override
    public IPage<HandlingUnitChangeLog> selectPage(FrontPage<HandlingUnitChangeLog> frontPage, HandlingUnitChangeLog handlingUnitChangeLog) {
        QueryWrapper<HandlingUnitChangeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitChangeLog);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<HandlingUnitChangeLog> selectList(HandlingUnitChangeLog handlingUnitChangeLog) {
        QueryWrapper<HandlingUnitChangeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitChangeLog);
        return super.list(queryWrapper);
    }


}