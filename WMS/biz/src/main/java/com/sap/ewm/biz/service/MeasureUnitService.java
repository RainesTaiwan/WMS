package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.MeasureUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 計量單位主數據 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-07-19
 */
public interface MeasureUnitService extends IService<MeasureUnit> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<MeasureUnit> selectPage(FrontPage<MeasureUnit> frontPage, MeasureUnit measureUnit);

    List<MeasureUnit> selectList(MeasureUnit measureUnit);
}