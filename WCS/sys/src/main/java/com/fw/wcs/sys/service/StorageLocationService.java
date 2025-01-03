package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.StorageLocation;
import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface StorageLocationService extends IService<StorageLocation> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    Page<StorageLocation> selectPage(FrontPage<StorageLocation> frontPage, StorageLocation storageLocation);

    List<StorageLocation> selectList(StorageLocation storageLocation);

    StorageLocation getStorageLocationByBin(String storageBin);
}