package com.sap.ewm.dashboard.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.dashboard.model.CarrierStatus;
import com.sap.ewm.dashboard.mapper.CarrierStatusMapper;
import com.sap.ewm.dashboard.service.CarrierStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * handling unit stataus summary 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-08-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarrierStatusServiceImpl extends ServiceImpl<CarrierStatusMapper, CarrierStatus> implements CarrierStatusService {


    @Autowired
    private CarrierStatusMapper carrierStatusMapper;

    @Override
    public IPage<CarrierStatus> selectPage(FrontPage<CarrierStatus> frontPage, CarrierStatus carrierStatus) {
        QueryWrapper<CarrierStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrierStatus);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<CarrierStatus> selectList(CarrierStatus carrierStatus) {
        QueryWrapper<CarrierStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrierStatus);
        return super.list(queryWrapper);
    }


}