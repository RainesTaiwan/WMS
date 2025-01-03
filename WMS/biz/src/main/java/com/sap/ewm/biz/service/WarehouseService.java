package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 倉庫主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface WarehouseService extends IService<Warehouse> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<Warehouse> selectPage(FrontPage<Warehouse> frontPage, Warehouse warehouse);

    List<Warehouse> selectList(Warehouse warehouse);
}