package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.Warehouse;
import com.sap.ewm.biz.mapper.WarehouseMapper;
import com.sap.ewm.biz.service.WarehouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 倉庫主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {


    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public IPage<Warehouse> selectPage(FrontPage<Warehouse> frontPage, Warehouse warehouse) {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(warehouse);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Warehouse> selectList(Warehouse warehouse) {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(warehouse);
        return super.list(queryWrapper);
    }


}