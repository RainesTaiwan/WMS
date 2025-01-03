package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.sys.model.MqLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * MQ日誌 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-07-14
 */
public interface MqLogService extends IService<MqLog> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<MqLog> selectPage(FrontPage<MqLog> frontPage, MqLog mqLog);

    List<MqLog> selectList(MqLog mqLog);
}