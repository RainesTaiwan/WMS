package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.mapper.ReceiveStationBindMapper;
import com.fw.wcs.sys.model.ReceiveStation;
import com.fw.wcs.sys.model.ReceiveStationBind;
import com.fw.wcs.sys.service.ReceiveStationBindService;
import com.fw.wcs.sys.service.ReceiveStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Leon
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiveStationBindServiceImpl extends ServiceImpl<ReceiveStationBindMapper, ReceiveStationBind> implements ReceiveStationBindService {


    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationBindMapper receiveStationBindMapper;

    @Override
    public List<ReceiveStationBind> findReceiveStationBind(String receiveStation, String carrier) {
        return receiveStationBindMapper.findReceiveStationBind(receiveStation, carrier);
    }

    @Override
    public void receiveStationBind(String receiveStation, String station, String carrier) {
        Date nowDate = new Date();

        ReceiveStation receiveStationModel = receiveStationService.getReceiveStation(receiveStation);
        if (receiveStationModel == null) {
            throw new BusinessException("輸送帶編號【"+receiveStation+"】不存在");
        }

        // Binding
        List<ReceiveStationBind> list = this.findReceiveStationBind(receiveStation, carrier);
        if (list == null || list.size() <= 0) {
            ReceiveStationBind receiveStationBind = new ReceiveStationBind();
            receiveStationBind.setHandle(UUID.randomUUID().toString());
            receiveStationBind.setReceiveStation(receiveStation);
            receiveStationBind.setStation(station);
            receiveStationBind.setCarrier(carrier);
            receiveStationBind.setStatus("Y");
            receiveStationBind.setCreateUser("ADMIN");
            receiveStationBind.setCreatedTime(nowDate);
            receiveStationBindMapper.insert(receiveStationBind);
        } else {
            ReceiveStationBind receiveStationBind = list.get(0);
            receiveStationBind.setStation(station);
            receiveStationBind.setUpdateUser("ADMIN");
            receiveStationBind.setUpdatedTime(nowDate);
            receiveStationBindMapper.updateById(receiveStationBind);
        }
    }

    @Override
    public void receiveStationBind(String receiveStation, String station, String carrier, String handle){
        Date nowDate = new Date();

        ReceiveStation receiveStationModel = receiveStationService.getReceiveStation(receiveStation);
        if (receiveStationModel == null) {
            throw new BusinessException("輸送帶編號【"+receiveStation+"】不存在");
        }
        if(handle==null)  handle = UUID.randomUUID().toString();

        // Binding
        List<ReceiveStationBind> list = this.findReceiveStationBind(receiveStation, carrier);
        if (list == null || list.size() <= 0) {
            ReceiveStationBind receiveStationBind = new ReceiveStationBind();
            receiveStationBind.setHandle(handle);
            receiveStationBind.setReceiveStation(receiveStation);
            receiveStationBind.setStation(station);
            receiveStationBind.setCarrier(carrier);
            receiveStationBind.setStatus("Y");
            receiveStationBind.setCreateUser("ADMIN");
            receiveStationBind.setCreatedTime(nowDate);
            receiveStationBindMapper.insert(receiveStationBind);
        } else {
            ReceiveStationBind receiveStationBind = list.get(0);
            receiveStationBind.setStation(station);
            receiveStationBind.setUpdateUser("ADMIN");
            receiveStationBind.setUpdatedTime(nowDate);
            receiveStationBindMapper.updateById(receiveStationBind);
        }
    }

    @Override
    public void receiveStationUnBind(String receiveStation, String carrier) {
        List<ReceiveStationBind> list = this.findReceiveStationBind(receiveStation, carrier);
        if (list != null && list.size() > 0) {
            receiveStationBindMapper.deleteById(list.get(0).getHandle());
        }
    }
}