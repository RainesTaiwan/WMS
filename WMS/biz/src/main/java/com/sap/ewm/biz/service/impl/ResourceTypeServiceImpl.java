package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.sap.ewm.biz.model.ResourceType;
import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.mapper.ResourceTypeMapper;
import com.sap.ewm.biz.service.ResourceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
/**
 * <p>
 * 資源組組主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceTypeServiceImpl extends ServiceImpl<ResourceTypeMapper, ResourceType> implements ResourceTypeService {


    @Autowired
    private ResourceTypeMapper resourceTypeMapper;

    @Override
    public IPage<ResourceType> selectPage(FrontPage<ResourceType> frontPage, ResourceType resourceType) {
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceType);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ResourceType> selectList(ResourceType resourceType) {
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceType);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(ResourceType resourceType) {
        resourceType.setHandle(ResourceType.genHandle(resourceType.getResourceType()));
        int count;
        try {
            count = baseMapper.insert(resourceType);
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("資源型別'" + resourceType.getResourceType() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(ResourceType resourceType) {
        resourceType.setHandle(ResourceType.genHandle(resourceType.getResourceType()));
        int count = baseMapper.updateById(resourceType);
        return retBool(count);
    }
}