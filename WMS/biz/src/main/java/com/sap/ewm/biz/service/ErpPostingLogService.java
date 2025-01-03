package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ErpPostingLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * ERP互動日誌 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ErpPostingLogService extends IService<ErpPostingLog> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ErpPostingLog> selectPage(FrontPage<ErpPostingLog> frontPage, ErpPostingLog erpPostingLog);

    List<ErpPostingLog> selectList(ErpPostingLog erpPostingLog);
}