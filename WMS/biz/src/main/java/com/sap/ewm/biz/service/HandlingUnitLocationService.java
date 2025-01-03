package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.WcsHandlingUnitSpecDTO;
import com.sap.ewm.biz.model.HandlingUnitLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 處理單元位置繫結（記錄目前處理單元所在的貨格） 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface HandlingUnitLocationService extends IService<HandlingUnitLocation> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<HandlingUnitLocation> selectPage(FrontPage<HandlingUnitLocation> frontPage, HandlingUnitLocation handlingUnitLocation);

    List<HandlingUnitLocation> selectList(HandlingUnitLocation handlingUnitLocation);

    HandlingUnitLocation getByCarrier(String carrier);

    HandlingUnitLocation getByStorageBin(String storageBin);

    /**
     * 計算最優貨格
     * @param carrierBo
     * @param wcsHandlingUnitSpecDTO
     * @return
     */
    StorageBin getOptimalStorageBin(String carrierBo, WcsHandlingUnitSpecDTO wcsHandlingUnitSpecDTO);
}