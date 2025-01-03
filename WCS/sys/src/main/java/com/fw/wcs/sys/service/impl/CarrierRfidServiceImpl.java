package com.fw.wcs.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.sys.mapper.CarrierRfidMapper;
import com.fw.wcs.sys.model.CarrierRfid;
import com.fw.wcs.sys.service.CarrierRfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *  服務實現類
 *
 * @author Leon/Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarrierRfidServiceImpl extends ServiceImpl<CarrierRfidMapper, CarrierRfid> implements CarrierRfidService {

    @Autowired
    private CarrierRfidMapper carrierRfidMapper;


    @Override
    public CarrierRfid getCarrierByTag(String tag) {
        return carrierRfidMapper.selectCarrierByTag(tag);
    }

    // 新增棧板
    @Override
    public void insertCarrier(String carrierId){
        CarrierRfid carrierRfid = new CarrierRfid();
        carrierRfid.setHandle("CarrierBO:"+carrierId);
        carrierRfid.setRfid(carrierId);
        carrierRfid.setCarrier(carrierId);
        carrierRfid.setCreateUser("ADMIN");
        carrierRfid.setCreatedTime(new Date());
        carrierRfid.setUpdateUser("ADMIN");
        carrierRfid.setUpdatedTime(new Date());
        carrierRfidMapper.insert(carrierRfid);
    }

    //檢查是否有此棧板，有回傳true
    @Override
    public boolean checkCarrier(String carrierID){
        boolean checkResult = false;
        CarrierRfid carrierRfid = carrierRfidMapper.findCarrierByID(carrierID);
        if(carrierRfid==null) checkResult = true;
        return checkResult;
    }
}