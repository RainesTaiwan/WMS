package com.fw.wcs.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.CarrierTask;

import java.util.List;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface CarrierTaskService extends IService<CarrierTask> {

    List<CarrierTask> findCarrierTask(String carrier, String status);
    List<CarrierTask> findNoCompletedTask(String carrier, String category, String startPosition);

    void createCarrierTask(String carrier, String category, String startPosition, String targetPosition, String wmsId);
    void updateCarrierTaskStatus(String carrier, String category, String startPosition, String status);
    void updateCarrierTaskAgvInfo(String carrier, String agvNo);

    void carrierOnMachine(String resource, String type, String station, String carrier);

    // 取得棧板長寬高重，告知WMS
    void carrierInfoReport(String resource, String station, String carrier);
}