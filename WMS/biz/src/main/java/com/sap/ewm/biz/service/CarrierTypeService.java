package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.CarrierType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 載具型別主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface CarrierTypeService extends IService<CarrierType> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<CarrierType> selectPage(FrontPage<CarrierType> frontPage, CarrierType carrierType);

    List<CarrierType> selectList(CarrierType carrierType);

    //確認Type是否存在，不存在則回傳false
    boolean checkCarrierType(String carrierType);
}