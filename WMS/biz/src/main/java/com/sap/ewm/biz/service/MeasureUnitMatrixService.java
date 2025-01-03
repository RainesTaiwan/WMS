package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.MeasureUnitMatrix;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 單位轉換矩陣 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-07-19
 */
public interface MeasureUnitMatrixService extends IService<MeasureUnitMatrix> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<MeasureUnitMatrix> selectPage(FrontPage<MeasureUnitMatrix> frontPage, MeasureUnitMatrix measureUnitMatrix);

    List<MeasureUnitMatrix> selectList(MeasureUnitMatrix measureUnitMatrix);
}