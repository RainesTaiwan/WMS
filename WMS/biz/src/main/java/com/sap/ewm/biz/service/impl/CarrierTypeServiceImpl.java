package com.sap.ewm.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.CarrierType;
import com.sap.ewm.biz.mapper.CarrierTypeMapper;
import com.sap.ewm.biz.service.CarrierTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
/**
 * <p>
 * 載具型別主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarrierTypeServiceImpl extends ServiceImpl<CarrierTypeMapper, CarrierType> implements CarrierTypeService {


    @Autowired
    private CarrierTypeMapper carrierTypeMapper;

    @Override
    public IPage<CarrierType> selectPage(FrontPage<CarrierType> frontPage, CarrierType carrierType) {
        QueryWrapper<CarrierType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrierType);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<CarrierType> selectList(CarrierType carrierType) {
        QueryWrapper<CarrierType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrierType);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(CarrierType carrierType) {
        carrierType.setHandle(CarrierType.genHandle(carrierType.getCarrierType()));
        int count;
        try {
            count = baseMapper.insert(carrierType);
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("棧板型別'" + carrierType.getCarrierType() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(CarrierType carrierType) {
        carrierType.setHandle(CarrierType.genHandle(carrierType.getCarrierType()));
        int count = baseMapper.updateById(carrierType);
        return retBool(count);
    }

    //確認Type是否存在，不存在則回傳false
    @Override
    public boolean checkCarrierType(String carrierTypeName) {
        boolean checkResult = true;
        CarrierType carrierType = carrierTypeMapper.findCarrierTypeByID(carrierTypeName);
        if(carrierType==null) checkResult = false;
        return checkResult;
    }
}