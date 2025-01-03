package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitSpec;
import com.sap.ewm.biz.mapper.HandlingUnitSpecMapper;
import com.sap.ewm.biz.service.HandlingUnitSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 處理單元規格記錄（處理單元過測量裝置時記錄） 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HandlingUnitSpecServiceImpl extends ServiceImpl<HandlingUnitSpecMapper, HandlingUnitSpec> implements HandlingUnitSpecService {


    @Autowired
    private HandlingUnitSpecMapper handlingUnitSpecMapper;

    @Override
    public IPage<HandlingUnitSpec> selectPage(FrontPage<HandlingUnitSpec> frontPage, HandlingUnitSpec handlingUnitSpec) {
        QueryWrapper<HandlingUnitSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitSpec);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<HandlingUnitSpec> selectList(HandlingUnitSpec handlingUnitSpec) {
        QueryWrapper<HandlingUnitSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitSpec);
        return super.list(queryWrapper);
    }


}