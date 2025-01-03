package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.mapper.RFIDReaderMapper;
import com.fw.wcs.sys.model.RFIDReader;
import com.fw.wcs.sys.service.RFIDReaderService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RFIDReaderServiceImpl extends ServiceImpl<RFIDReaderMapper, RFIDReader> implements RFIDReaderService{

    @Autowired
    private RFIDReaderMapper rfidReaderMapper;


    @Override
    //透過 RFIDReader的ID 更新 RFIDReader的狀態
    public void updateRFIDReaderStatus(String readerID, String status){
        RFIDReader rfidReader = new RFIDReader();
        rfidReader.setStatus(status);
        rfidReader.setUpdateUser("ADMIN");
        rfidReader.setUpdatedTime(new Date());
        rfidReaderMapper.updateStatusByReaderID(readerID, rfidReader);
    }

    @Override
    //透過 receiveStation與station 查詢 RFIDReader的ID
    public String getRFIDReaderID(String receiveStation, String station){
        RFIDReader rfidReader = rfidReaderMapper.findRFIDReaderID(receiveStation, station);
        return rfidReader.getReaderID();
    }

    @Override
    //透過 RFIDReader的ID 查詢 receiveStation
    public String getReceiveStation(String readerID){
        RFIDReader rfidReader = rfidReaderMapper.findReceiveStation(readerID);
        return rfidReader.getReceiveStation();
    }

}
