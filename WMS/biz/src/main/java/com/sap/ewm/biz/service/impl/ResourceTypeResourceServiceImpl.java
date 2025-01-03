package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ResourceTypeResource;
import com.sap.ewm.biz.mapper.ResourceTypeResourceMapper;
import com.sap.ewm.biz.service.ResourceTypeResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 資源關聯資源型別 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceTypeResourceServiceImpl extends ServiceImpl<ResourceTypeResourceMapper, ResourceTypeResource> implements ResourceTypeResourceService {


    @Autowired
    private ResourceTypeResourceMapper resourceTypeResourceMapper;

    @Override
    public IPage<ResourceTypeResource> selectPage(FrontPage<ResourceTypeResource> frontPage, ResourceTypeResource resourceTypeResource) {
        QueryWrapper<ResourceTypeResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceTypeResource);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ResourceTypeResource> selectList(ResourceTypeResource resourceTypeResource) {
        QueryWrapper<ResourceTypeResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceTypeResource);
        return super.list(queryWrapper);
    }


}