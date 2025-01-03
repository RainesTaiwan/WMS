package com.fw.wcs.sys.service.impl;

import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.StorageLocation;
import com.fw.wcs.sys.mapper.StorageLocationMapper;
import com.fw.wcs.sys.service.StorageLocationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Leon
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageLocationServiceImpl extends ServiceImpl<StorageLocationMapper, StorageLocation> implements StorageLocationService {


    @Autowired
    private StorageLocationMapper storageLocationMapper;

    @Override
    public Page<StorageLocation> selectPage(FrontPage<StorageLocation> frontPage, StorageLocation storageLocation) {
        EntityWrapper<StorageLocation> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(storageLocation);
        return super.selectPage(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageLocation> selectList(StorageLocation storageLocation) {
        EntityWrapper<StorageLocation> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(storageLocation);
        return super.selectList(queryWrapper);
    }

    @Override
    public StorageLocation getStorageLocationByBin(String storageBin) {
        return storageLocationMapper.selectStorageLocationByBin(storageBin);
    }


}