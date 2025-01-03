package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.MqLogText;
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
public interface MqLogTextService extends IService<MqLogText> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    Page<MqLogText> selectPage(FrontPage<MqLogText> frontPage, MqLogText mqLogText);

    List<MqLogText> selectList(MqLogText mqLogText);
}