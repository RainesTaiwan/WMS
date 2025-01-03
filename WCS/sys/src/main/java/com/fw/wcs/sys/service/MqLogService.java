package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.MqLog;
import com.fw.wcs.sys.dto.WmsResponse;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface MqLogService extends IService<MqLog> {

    void saveMqLog(MqLog mqLog, String requestText, WmsResponse wmsResponse);
}