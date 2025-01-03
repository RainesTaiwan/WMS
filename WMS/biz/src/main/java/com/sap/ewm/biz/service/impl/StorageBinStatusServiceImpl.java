package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageBinStatus;
import com.sap.ewm.biz.mapper.StorageBinStatusMapper;
import com.sap.ewm.biz.service.StorageBinStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 儲存貨格狀態 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-07-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageBinStatusServiceImpl extends ServiceImpl<StorageBinStatusMapper, StorageBinStatus> implements StorageBinStatusService {


    @Autowired
    private StorageBinStatusMapper storageBinStatusMapper;

    @Override
    public IPage<StorageBinStatus> selectPage(FrontPage<StorageBinStatus> frontPage, StorageBinStatus storageBinStatus) {
        QueryWrapper<StorageBinStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinStatus);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageBinStatus> selectList(StorageBinStatus storageBinStatus) {
        QueryWrapper<StorageBinStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinStatus);
        return super.list(queryWrapper);
    }


}