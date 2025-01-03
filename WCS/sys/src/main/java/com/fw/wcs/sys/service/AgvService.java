package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.Agv;

import java.util.Date;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface AgvService extends IService<Agv> {

    //查詢可以執行運輸任務的AGV-狀態為:IDLE
    Agv getCanTransportAgv();

    void updateByAgvNo(String agvNo, Agv agv);

    void updateAgvStatus(String agvNo, String status);

    // 找到指定AGV
    Agv findAGV(String agvNo);

    // AGV狀態報告ASRS
    void reportASRS(String agvNo, String type);
}