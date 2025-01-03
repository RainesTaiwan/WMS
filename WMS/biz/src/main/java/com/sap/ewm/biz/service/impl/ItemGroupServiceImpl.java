package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.sap.ewm.biz.model.ItemGroup;
import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.mapper.ItemGroupMapper;
import com.sap.ewm.biz.service.ItemGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
/**
 * <p>
 * 物料組主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemGroupServiceImpl extends ServiceImpl<ItemGroupMapper, ItemGroup> implements ItemGroupService {


    @Autowired
    private ItemGroupMapper itemGroupMapper;

    @Override
    public IPage<ItemGroup> selectPage(FrontPage<ItemGroup> frontPage, ItemGroup itemGroup) {
        QueryWrapper<ItemGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroup);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ItemGroup> selectList(ItemGroup itemGroup) {
        QueryWrapper<ItemGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroup);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(ItemGroup itemGroup) {
        itemGroup.setHandle(ItemGroup.genHandle(itemGroup.getItemGroup()));
        int count;
        try {
            count = baseMapper.insert(itemGroup);
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("物料組'" + itemGroup.getItemGroup() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(ItemGroup itemGroup) {
        itemGroup.setHandle(ItemGroup.genHandle(itemGroup.getItemGroup()));
        int count = baseMapper.updateById(itemGroup);
        return retBool(count);
    }
}