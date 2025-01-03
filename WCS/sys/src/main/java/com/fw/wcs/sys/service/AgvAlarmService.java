package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.AgvAlarm;
import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface AgvAlarmService extends IService<AgvAlarm> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    Page<AgvAlarm> selectPage(FrontPage<AgvAlarm> frontPage, AgvAlarm agvAlarm);

    List<AgvAlarm> selectList(AgvAlarm agvAlarm);

    List<AgvAlarm> getNoCompletedAlarm(String agvNo, String alarmCode);

    void agvAlarm(String agvNo, String severity, String alarmCode, String description, String status);
}