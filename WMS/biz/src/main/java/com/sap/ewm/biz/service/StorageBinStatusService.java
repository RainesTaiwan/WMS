package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageBinStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 儲存貨格狀態 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-07-14
 */
public interface StorageBinStatusService extends IService<StorageBinStatus> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<StorageBinStatus> selectPage(FrontPage<StorageBinStatus> frontPage, StorageBinStatus storageBinStatus);

    List<StorageBinStatus> selectList(StorageBinStatus storageBinStatus);
}