package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitChangeLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 處理單元變更日誌記錄 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface HandlingUnitChangeLogService extends IService<HandlingUnitChangeLog> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<HandlingUnitChangeLog> selectPage(FrontPage<HandlingUnitChangeLog> frontPage, HandlingUnitChangeLog handlingUnitChangeLog);

    List<HandlingUnitChangeLog> selectList(HandlingUnitChangeLog handlingUnitChangeLog);
}