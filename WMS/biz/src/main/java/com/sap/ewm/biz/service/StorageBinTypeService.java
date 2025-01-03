package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageBinType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 儲存貨格型別主數據 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-07-20
 */
public interface StorageBinTypeService extends IService<StorageBinType> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<StorageBinType> selectPage(FrontPage<StorageBinType> frontPage, StorageBinType storageBinType);

    List<StorageBinType> selectList(StorageBinType storageBinType);
}