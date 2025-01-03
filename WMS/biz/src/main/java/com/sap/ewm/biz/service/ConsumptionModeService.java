package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ConsumptionMode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 物料耗用模式主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ConsumptionModeService extends IService<ConsumptionMode> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ConsumptionMode> selectPage(FrontPage<ConsumptionMode> frontPage, ConsumptionMode consumptionMode);

    List<ConsumptionMode> selectList(ConsumptionMode consumptionMode);
}