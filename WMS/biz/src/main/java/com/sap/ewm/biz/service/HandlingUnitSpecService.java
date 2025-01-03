package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitSpec;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 處理單元規格記錄（處理單元過測量裝置時記錄） 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface HandlingUnitSpecService extends IService<HandlingUnitSpec> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<HandlingUnitSpec> selectPage(FrontPage<HandlingUnitSpec> frontPage, HandlingUnitSpec handlingUnitSpec);

    List<HandlingUnitSpec> selectList(HandlingUnitSpec handlingUnitSpec);
}