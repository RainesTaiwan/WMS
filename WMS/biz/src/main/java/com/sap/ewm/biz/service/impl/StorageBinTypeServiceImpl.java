package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageBinType;
import com.sap.ewm.biz.mapper.StorageBinTypeMapper;
import com.sap.ewm.biz.service.StorageBinTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
/**
 * <p>
 * 儲存貨格型別主數據 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-07-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageBinTypeServiceImpl extends ServiceImpl<StorageBinTypeMapper, StorageBinType> implements StorageBinTypeService {


    @Autowired
    private StorageBinTypeMapper storageBinTypeMapper;

    @Override
    public IPage<StorageBinType> selectPage(FrontPage<StorageBinType> frontPage, StorageBinType storageBinType) {
        QueryWrapper<StorageBinType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinType);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageBinType> selectList(StorageBinType storageBinType) {
        QueryWrapper<StorageBinType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinType);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(StorageBinType storageBinType) {
        storageBinType.setHandle(StorageBinType.genHandle(storageBinType.getStorageBinType()));
        int count;
        try {
            count = baseMapper.insert(storageBinType);
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("貨格型別'" + storageBinType.getStorageBinType() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(StorageBinType storageBinType) {
        storageBinType.setHandle(StorageBinType.genHandle(storageBinType.getStorageBinType()));
        int count = baseMapper.updateById(storageBinType);
        return retBool(count);
    }
}