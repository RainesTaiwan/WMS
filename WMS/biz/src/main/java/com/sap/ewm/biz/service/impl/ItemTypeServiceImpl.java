package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemType;
import com.sap.ewm.biz.mapper.ItemTypeMapper;
import com.sap.ewm.biz.service.ItemTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 物料型別主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemTypeServiceImpl extends ServiceImpl<ItemTypeMapper, ItemType> implements ItemTypeService {


    @Autowired
    private ItemTypeMapper itemTypeMapper;

    @Override
    public IPage<ItemType> selectPage(FrontPage<ItemType> frontPage, ItemType itemType) {
        QueryWrapper<ItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemType);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ItemType> selectList(ItemType itemType) {
        QueryWrapper<ItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemType);
        return super.list(queryWrapper);
    }

    // 依據Handle取得ItemType
    @Override
    public ItemType findItemTypeByHandle(String handle){
        return itemTypeMapper.findItemTypeByHandle(handle);
    }

}