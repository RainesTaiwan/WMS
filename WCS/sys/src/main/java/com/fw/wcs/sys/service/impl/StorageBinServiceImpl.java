package com.fw.wcs.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.sys.mapper.StorageBinMapper;
import com.fw.wcs.sys.model.StorageBin;
import com.fw.wcs.sys.service.StorageBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class StorageBinServiceImpl extends ServiceImpl<StorageBinMapper, StorageBin> implements StorageBinService {


    @Autowired
    private StorageBinMapper storageBinMapper;


    @Override
    public StorageBin getStorageBin(String storageBin) {
        return storageBinMapper.selectStorageBin(storageBin);
    }
}