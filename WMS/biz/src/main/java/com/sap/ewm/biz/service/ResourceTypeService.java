package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ResourceType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 資源組組主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ResourceTypeService extends IService<ResourceType> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ResourceType> selectPage(FrontPage<ResourceType> frontPage, ResourceType resourceType);

    List<ResourceType> selectList(ResourceType resourceType);
}