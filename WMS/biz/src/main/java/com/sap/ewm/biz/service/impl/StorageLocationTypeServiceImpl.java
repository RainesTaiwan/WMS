package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageLocationType;
import com.sap.ewm.biz.mapper.StorageLocationTypeMapper;
import com.sap.ewm.biz.service.StorageLocationTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 儲存位置型別主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageLocationTypeServiceImpl extends ServiceImpl<StorageLocationTypeMapper, StorageLocationType> implements StorageLocationTypeService {


    @Autowired
    private StorageLocationTypeMapper storageLocationTypeMapper;

    @Override
    public IPage<StorageLocationType> selectPage(FrontPage<StorageLocationType> frontPage, StorageLocationType storageLocationType) {
        QueryWrapper<StorageLocationType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocationType);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageLocationType> selectList(StorageLocationType storageLocationType) {
        QueryWrapper<StorageLocationType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocationType);
        return super.list(queryWrapper);
    }


}