package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.ReceiveStationBind;

import java.util.List;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface ReceiveStationBindService extends IService<ReceiveStationBind> {

    List<ReceiveStationBind> findReceiveStationBind(String receiveStation, String carrier);
    void receiveStationBind(String receiveStation, String station, String carrier);
    void receiveStationBind(String receiveStation, String station, String carrier, String handle);
    void receiveStationUnBind(String receiveStation, String carrier);
}