package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ResourceTypeResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 資源關聯資源型別 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ResourceTypeResourceService extends IService<ResourceTypeResource> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ResourceTypeResource> selectPage(FrontPage<ResourceTypeResource> frontPage, ResourceTypeResource resourceTypeResource);

    List<ResourceTypeResource> selectList(ResourceTypeResource resourceTypeResource);
}