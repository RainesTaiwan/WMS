package com.sap.ewm.dashboard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.dashboard.model.InventorySummary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * inventory summary by day 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-08-03
 */
public interface InventorySummaryService extends IService<InventorySummary> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<InventorySummary> selectPage(FrontPage<InventorySummary> frontPage, InventorySummary inventorySummary);

    List<InventorySummary> selectList(InventorySummary inventorySummary);

    void setTotalQty(String itemBo, BigDecimal summaryQty, LocalDate date);

    boolean inbound(String itemBo, BigDecimal qty, LocalDate date);

    boolean outBound(String itemBo, BigDecimal qty, LocalDate date);
}