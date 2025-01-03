package com.sap.ewm.dashboard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.dashboard.model.CarrierStatus;

import java.util.List;

/**
 * <p>
 * handling unit stataus summary 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-08-06
 */
public interface CarrierStatusService extends IService<CarrierStatus> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<CarrierStatus> selectPage(FrontPage<CarrierStatus> frontPage, CarrierStatus carrierStatus);

    List<CarrierStatus> selectList(CarrierStatus carrierStatus);
}