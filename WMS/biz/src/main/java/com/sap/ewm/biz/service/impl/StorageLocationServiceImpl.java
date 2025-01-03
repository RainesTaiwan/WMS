package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.dto.StorageLocationDTO;
import com.sap.ewm.biz.mapper.StorageLocationMapper;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.model.StorageLocation;
import com.sap.ewm.biz.model.StorageLocationType;
import com.sap.ewm.biz.model.Warehouse;
import com.sap.ewm.biz.service.StorageLocationService;
import com.sap.ewm.core.base.FrontPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 儲存位置主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageLocationServiceImpl extends ServiceImpl<StorageLocationMapper, StorageLocation> implements StorageLocationService {


    @Autowired
    private StorageLocationMapper storageLocationMapper;

    @Override
    public IPage<StorageLocation> selectPage(FrontPage<StorageLocation> frontPage, StorageLocation storageLocation) {
        QueryWrapper<StorageLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocation);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageLocation> selectList(StorageLocation storageLocation) {
        QueryWrapper<StorageLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocation);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(StorageLocationDTO storageLocation) {
        storageLocation.setHandle(StorageLocation.genHandle(storageLocation.getStorageLocation()));
        if (StrUtil.isNotBlank(storageLocation.getStorageLocationType())) {
            storageLocation.setStorageLocationTypeBo(StorageLocationType.genHandle(storageLocation.getStorageLocationType()));
        }
        if (StrUtil.isNotBlank(storageLocation.getWarehouse())) {
            storageLocation.setWarehouseBo(Warehouse.genHandle(storageLocation.getWarehouse()));
        }
        int count;
        try {
            count = baseMapper.insert(storageLocation);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("儲存地點'" + storageLocation.getStorageLocation() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(StorageLocationDTO storageLocation) {
        storageLocation.setHandle(StorageLocation.genHandle(storageLocation.getStorageLocation()));
        if (StrUtil.isNotBlank(storageLocation.getStorageLocationType())) {
            storageLocation.setStorageLocationTypeBo(StorageLocationType.genHandle(storageLocation.getStorageLocationType()));
        }
        if (StrUtil.isNotBlank(storageLocation.getWarehouse())) {
            storageLocation.setWarehouseBo(Warehouse.genHandle(storageLocation.getWarehouse()));
        }
        int count = baseMapper.updateById(storageLocation);
        return retBool(count);
    }

    /**
     * 列出可用儲存地點
     *
     * @return
     */
    @Override
    public List<StorageLocationDTO> listUsableStorageLocationPriority(List<String> busyStorageLocationList) {
        List<StorageLocationDTO> storageLocationDTOList = storageLocationMapper.listUsableStorageLocation();
        if (busyStorageLocationList != null && busyStorageLocationList.size() > 0) {
            storageLocationDTOList.removeIf(s -> busyStorageLocationList.contains(s.getStorageLocation()));
        }
        if (storageLocationDTOList != null && storageLocationDTOList.size() > 1) {
            storageLocationDTOList.sort(Comparator.comparing(StorageLocationDTO::getUsedProportion, Comparator.nullsFirst(BigDecimal::compareTo)));
        }
        return storageLocationDTOList;
    }

    /**
     * 列出儲存地點可用的貨格
     *
     * @param storageLocationBo
     * @param mixItem
     * @param width
     * @param height
     * @param length
     * @param weight
     * @return
     */
    @Override
    public List<StorageBin> listUsableEmptyStorageBin(String storageLocationBo, String mixItem, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal weight) {
        return storageLocationMapper.listUsableEmptyStorageBin(storageLocationBo, mixItem, width, height, length, weight);
    }
}