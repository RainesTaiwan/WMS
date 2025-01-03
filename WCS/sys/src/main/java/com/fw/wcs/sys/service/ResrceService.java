package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.Resrce;
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
public interface ResrceService extends IService<Resrce> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    Page<Resrce> selectPage(FrontPage<Resrce> frontPage, Resrce resrce);

    List<Resrce> selectList(Resrce resrce);
}