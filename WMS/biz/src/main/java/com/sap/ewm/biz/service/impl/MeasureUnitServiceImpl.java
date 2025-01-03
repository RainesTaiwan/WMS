package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.MeasureUnit;
import com.sap.ewm.biz.mapper.MeasureUnitMapper;
import com.sap.ewm.biz.service.MeasureUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 計量單位主數據 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-07-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureUnitServiceImpl extends ServiceImpl<MeasureUnitMapper, MeasureUnit> implements MeasureUnitService {


    @Autowired
    private MeasureUnitMapper measureUnitMapper;

    @Override
    public IPage<MeasureUnit> selectPage(FrontPage<MeasureUnit> frontPage, MeasureUnit measureUnit) {
        QueryWrapper<MeasureUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnit);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<MeasureUnit> selectList(MeasureUnit measureUnit) {
        QueryWrapper<MeasureUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnit);
        return super.list(queryWrapper);
    }


}