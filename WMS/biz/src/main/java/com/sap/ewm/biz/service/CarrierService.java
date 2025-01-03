package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.CarrierDTO;
import com.sap.ewm.biz.model.Carrier;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 載具主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface CarrierService extends IService<Carrier> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<Carrier> selectPage(FrontPage<Carrier> frontPage, Carrier carrier);

    List<Carrier> selectList(Carrier carrier);

    boolean save(CarrierDTO carrierDTO);

    boolean updateById(CarrierDTO carrierDTO);

    void changeStatus(String carrier, String item, String operation, String status);

    //檢查是否有此棧板，有回傳true
    boolean checkCarrier(String carrier);

    //新增棧板
    void insertCarrier(String carrier, String type);

    /*
    //要求WCS放置新棧板
    Carrier askWCSNewCarrier();

     */
}