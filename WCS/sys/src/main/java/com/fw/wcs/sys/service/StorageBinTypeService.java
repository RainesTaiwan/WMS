package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.StorageBinType;
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
public interface StorageBinTypeService extends IService<StorageBinType> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    Page<StorageBinType> selectPage(FrontPage<StorageBinType> frontPage, StorageBinType storageBinType);

    List<StorageBinType> selectList(StorageBinType storageBinType);
}