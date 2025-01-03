package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.ResourceDTO;
import com.sap.ewm.biz.model.Resrce;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 資源主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ResrceService extends IService<Resrce> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<Resrce> selectPage(FrontPage<Resrce> frontPage, Resrce resrce);

    List<Resrce> selectList(Resrce resrce);

    boolean save(ResourceDTO resrce);

    boolean updateById(ResourceDTO resrce);
}