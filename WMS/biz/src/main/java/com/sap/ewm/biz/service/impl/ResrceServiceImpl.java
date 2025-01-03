package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.dto.ResourceDTO;
import com.sap.ewm.biz.mapper.ResrceMapper;
import com.sap.ewm.biz.model.ResourceType;
import com.sap.ewm.biz.model.ResourceTypeResource;
import com.sap.ewm.biz.model.Resrce;
import com.sap.ewm.biz.service.ResourceTypeResourceService;
import com.sap.ewm.biz.service.ResrceService;
import com.sap.ewm.core.base.FrontPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 * 資源主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResrceServiceImpl extends ServiceImpl<ResrceMapper, Resrce> implements ResrceService {


    @Autowired
    private ResrceMapper resrceMapper;

    @Autowired
    private ResourceTypeResourceService resourceTypeResourceService;

    @Override
    public IPage<Resrce> selectPage(FrontPage<Resrce> frontPage, Resrce resrce) {
        QueryWrapper<Resrce> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resrce);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Resrce> selectList(Resrce resrce) {
        QueryWrapper<Resrce> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resrce);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(ResourceDTO resrce) {
        resrce.setHandle(Resrce.genHandle(resrce.getResrce()));
        int count;
        try {
            count = baseMapper.insert(resrce);
            saveResourceTypeResource(resrce.getHandle(), resrce.getResourceTypes());
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("資源'" + resrce.getResrce() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(ResourceDTO resrce) {
        resrce.setHandle(Resrce.genHandle(resrce.getResrce()));
        int count = baseMapper.updateById(resrce);
        saveResourceTypeResource(resrce.getHandle(), resrce.getResourceTypes());
        return retBool(count);
    }

    @Override
    public boolean removeById(Serializable resourceBo) {
        saveResourceTypeResource((String)resourceBo, null);
        return super.removeById(resourceBo);
    }

    private void saveResourceTypeResource(String resourceBo, List<String> resourceTypes) {
        List<ResourceTypeResource> resourceTypeResourceList = new ArrayList<>();
        resourceTypeResourceService.remove(Wrappers.<ResourceTypeResource>lambdaUpdate().eq(ResourceTypeResource::getResourceBo, resourceBo));
        if(resourceTypes != null && resourceTypes.size() > 0) {
            for(String resourceType : resourceTypes) {
                ResourceTypeResource resourceTypeResource = new ResourceTypeResource(ResourceType.genHandle(resourceType), resourceBo);
                resourceTypeResourceList.add(resourceTypeResource);
            }
            if(resourceTypes.size() > 0) {
                resourceTypeResourceService.saveBatch(resourceTypeResourceList);
            }
        }
    }
}