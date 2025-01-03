package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.StorageLocationDTO;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.model.StorageLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 儲存位置主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface StorageLocationService extends IService<StorageLocation> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<StorageLocation> selectPage(FrontPage<StorageLocation> frontPage, StorageLocation storageLocation);

    List<StorageLocation> selectList(StorageLocation storageLocation);

    boolean save(StorageLocationDTO storageLocation);

    boolean updateById(StorageLocationDTO storageLocation);

    List<StorageLocationDTO> listUsableStorageLocationPriority(List<String> busyStorageLocationList);

    List<StorageBin> listUsableEmptyStorageBin(String storageLocationBo, String mixItem, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal weight);
}