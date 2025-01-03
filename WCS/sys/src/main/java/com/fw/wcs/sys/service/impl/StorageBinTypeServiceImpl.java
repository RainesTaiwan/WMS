package com.fw.wcs.sys.service.impl;

import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.StorageBinType;
import com.fw.wcs.sys.mapper.StorageBinTypeMapper;
import com.fw.wcs.sys.service.StorageBinTypeService;
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
public class StorageBinTypeServiceImpl extends ServiceImpl<StorageBinTypeMapper, StorageBinType> implements StorageBinTypeService {


    @Autowired
    private StorageBinTypeMapper storageBinTypeMapper;

    @Override
    public Page<StorageBinType> selectPage(FrontPage<StorageBinType> frontPage, StorageBinType storageBinType) {
        EntityWrapper<StorageBinType> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(storageBinType);
        return super.selectPage(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageBinType> selectList(StorageBinType storageBinType) {
        EntityWrapper<StorageBinType> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(storageBinType);
        return super.selectList(queryWrapper);
    }


}