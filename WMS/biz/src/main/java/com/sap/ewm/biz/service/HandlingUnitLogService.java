package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 處理單元建立日誌記錄 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface HandlingUnitLogService extends IService<HandlingUnitLog> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<HandlingUnitLog> selectPage(FrontPage<HandlingUnitLog> frontPage, HandlingUnitLog handlingUnitLog);

    List<HandlingUnitLog> selectList(HandlingUnitLog handlingUnitLog);
}