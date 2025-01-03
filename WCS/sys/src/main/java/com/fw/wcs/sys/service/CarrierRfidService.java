package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.CarrierRfid;

/**
 *  服務類
 *
 * @author Leon/Glanz
 *
 */
public interface CarrierRfidService extends IService<CarrierRfid> {

    CarrierRfid getCarrierByTag(String tag);

    // 新增棧板
    void insertCarrier(String carrierId);

    //檢查是否有此棧板，有回傳true
    boolean checkCarrier(String carrierID);
}