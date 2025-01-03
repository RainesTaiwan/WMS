package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ConsumptionMode;
import com.sap.ewm.biz.mapper.ConsumptionModeMapper;
import com.sap.ewm.biz.service.ConsumptionModeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 物料耗用模式主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConsumptionModeServiceImpl extends ServiceImpl<ConsumptionModeMapper, ConsumptionMode> implements ConsumptionModeService {


    @Autowired
    private ConsumptionModeMapper consumptionModeMapper;

    @Override
    public IPage<ConsumptionMode> selectPage(FrontPage<ConsumptionMode> frontPage, ConsumptionMode consumptionMode) {
        QueryWrapper<ConsumptionMode> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(consumptionMode);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ConsumptionMode> selectList(ConsumptionMode consumptionMode) {
        QueryWrapper<ConsumptionMode> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(consumptionMode);
        return super.list(queryWrapper);
    }


}