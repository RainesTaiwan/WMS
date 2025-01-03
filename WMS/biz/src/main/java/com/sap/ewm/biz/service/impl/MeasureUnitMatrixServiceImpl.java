package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.MeasureUnitMatrix;
import com.sap.ewm.biz.mapper.MeasureUnitMatrixMapper;
import com.sap.ewm.biz.service.MeasureUnitMatrixService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 單位轉換矩陣 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-07-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MeasureUnitMatrixServiceImpl extends ServiceImpl<MeasureUnitMatrixMapper, MeasureUnitMatrix> implements MeasureUnitMatrixService {


    @Autowired
    private MeasureUnitMatrixMapper measureUnitMatrixMapper;

    @Override
    public IPage<MeasureUnitMatrix> selectPage(FrontPage<MeasureUnitMatrix> frontPage, MeasureUnitMatrix measureUnitMatrix) {
        QueryWrapper<MeasureUnitMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnitMatrix);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<MeasureUnitMatrix> selectList(MeasureUnitMatrix measureUnitMatrix) {
        QueryWrapper<MeasureUnitMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnitMatrix);
        return super.list(queryWrapper);
    }


}