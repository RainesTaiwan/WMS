package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.StorageLocationDTO;
import com.sap.ewm.biz.dto.WcsHandlingUnitSpecDTO;
import com.sap.ewm.biz.mapper.HandlingUnitLocationMapper;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.biz.model.Carrier;
import com.sap.ewm.biz.model.HandlingUnitLocation;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.service.HandlingUnitLocationService;
import com.sap.ewm.biz.service.HandlingUnitService;
import com.sap.ewm.biz.service.StorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 處理單元位置繫結（記錄目前處理單元所在的貨格） 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HandlingUnitLocationServiceImpl extends ServiceImpl<HandlingUnitLocationMapper, HandlingUnitLocation> implements HandlingUnitLocationService {


    @Autowired
    private HandlingUnitLocationMapper handlingUnitLocationMapper;

    @Autowired
    private HandlingUnitService handlingUnitService;

    @Autowired
    private StorageLocationService storageLocationService;

    @Override
    public IPage<HandlingUnitLocation> selectPage(FrontPage<HandlingUnitLocation> frontPage, HandlingUnitLocation handlingUnitLocation) {
        QueryWrapper<HandlingUnitLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLocation);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<HandlingUnitLocation> selectList(HandlingUnitLocation handlingUnitLocation) {
        QueryWrapper<HandlingUnitLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLocation);
        return super.list(queryWrapper);
    }

    /**
     * 根據載具編號獲取處理單元位置
     * @param carrier
     * @return
     */
    @Override
    public HandlingUnitLocation getByCarrier(String carrier) {
        return this.getOne(Wrappers.<HandlingUnitLocation>lambdaQuery().eq(HandlingUnitLocation::getCarrierBo, Carrier.genHandle(carrier)), false);
    }

    /**
     * 根據貨格編號獲取繫結資訊
     * @param storageBin
     * @return
     */
    @Override
    public HandlingUnitLocation getByStorageBin(String storageBin) {
        return this.getOne(Wrappers.<HandlingUnitLocation>lambdaQuery().eq(HandlingUnitLocation::getBindContextGbo, StorageBin.genHandle(storageBin)), false);
    }

    /**
     * 計算最優貨格
     * @param carrierBo
     * @param wcsHandlingUnitSpecDTO
     * @return
     */
    @Override
    public StorageBin getOptimalStorageBin(String carrierBo, WcsHandlingUnitSpecDTO wcsHandlingUnitSpecDTO) {
        List<String> busyStorageLocationList = wcsHandlingUnitSpecDTO.getBusyStorageLocationList();
        // 先放使用最少的貨架，混料控制
        List<StorageLocationDTO> storageLocationDTOList = storageLocationService.listUsableStorageLocationPriority(busyStorageLocationList);
        if(storageLocationDTOList == null || storageLocationDTOList.size() == 0) {
            return null;
        }
        boolean mixed = handlingUnitService.isMixed(StringUtils.trimHandle(carrierBo));
        for(StorageLocationDTO storageLocationDTO : storageLocationDTOList) {
            String storageLocationBo = storageLocationDTO.getHandle();
            BigDecimal width = wcsHandlingUnitSpecDTO.getWidth();
            BigDecimal height = wcsHandlingUnitSpecDTO.getHeight();
            BigDecimal length = wcsHandlingUnitSpecDTO.getLength();
            BigDecimal weight = wcsHandlingUnitSpecDTO.getWeight();
            List<StorageBin> usableBinList = storageLocationService.listUsableEmptyStorageBin(storageLocationBo, mixed ? CommonConstants.Y : CommonConstants.N, width, height, length, weight);
            if(usableBinList == null || usableBinList.size() == 0) {
                continue;
            }
            // TODO 先任意返回第一個貨格，後續根據最優放置演演算法放置
            return usableBinList.get(0);
        }
        return null;
    }
}