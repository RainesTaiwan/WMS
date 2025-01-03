package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageLocationType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 儲存位置型別主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface StorageLocationTypeService extends IService<StorageLocationType> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<StorageLocationType> selectPage(FrontPage<StorageLocationType> frontPage, StorageLocationType storageLocationType);

    List<StorageLocationType> selectList(StorageLocationType storageLocationType);
}