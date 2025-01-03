package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.dto.ItemDTO;
import com.sap.ewm.biz.mapper.ItemMapper;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.ItemGroupMemberService;
import com.sap.ewm.biz.service.ItemService;
import com.sap.ewm.biz.service.ItemTypeService;
import com.sap.ewm.core.base.FrontPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 物料主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {


    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemGroupMemberService itemGroupMemberService;
    @Autowired
    private ItemTypeService itemTypeService;

    @Override
    public IPage<Item> selectPage(FrontPage<Item> frontPage, Item item) {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(item);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Item> selectList(Item item) {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(item);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(ItemDTO item) {
        item.setHandle(Item.genHandle(item.getItem()));
        if (StrUtil.isNotBlank(item.getItemType())) {
            item.setItemTypeBo(ItemType.genHandle(item.getItemType()));
        }
        int count;
        try {
            count = baseMapper.insert(item);
            saveItemGroupMember(item.getHandle(), item.getItemGroups());
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("物料'" + item.getItem() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(ItemDTO item) {
        item.setHandle(Item.genHandle(item.getItem()));
        if (StrUtil.isNotBlank(item.getItemType())) {
            item.setItemTypeBo(ItemType.genHandle(item.getItemType()));
        }
        int count = baseMapper.updateById(item);
        saveItemGroupMember(item.getHandle(), item.getItemGroups());
        return retBool(count);
    }

    @Override
    public boolean removeById(Serializable itemBo) {
        saveItemGroupMember((String)itemBo, null);
        return super.removeById(itemBo);
    }

    private void saveItemGroupMember(String itemBo, List<String> itemGroups) {
        List<ItemGroupMember> itemGroupMemberList = new ArrayList<>();
        itemGroupMemberService.remove(Wrappers.<ItemGroupMember>lambdaUpdate().eq(ItemGroupMember::getItemBo, itemBo));
        if(itemGroups != null && itemGroups.size() > 0) {
            for(String itemGroup : itemGroups) {
                ItemGroupMember itemGroupMember = new ItemGroupMember(ItemGroup.genHandle(itemGroup), itemBo);
                itemGroupMemberList.add(itemGroupMember);
            }
            if(itemGroupMemberList.size() > 0) {
                itemGroupMemberService.saveBatch(itemGroupMemberList);
            }
        }
    }

    // 確認是否有該物料，沒有則自動新增
    @Override
    public void itemVerify(String itemCode, String itemType){
        Item item = itemMapper.findItemByName(itemCode);
        // 若沒有該物料，需要新增物料名稱
        if(item == null){
            LocalDateTime now = LocalDateTime.now();
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setHandle("itemBo:"+itemCode);
            itemDTO.setItem(itemCode);
            itemDTO.setDescription(itemCode);
            itemDTO.setMixItem("Y");
            itemDTO.setInspectionRequired("N");
            itemDTO.setItemStatus("AVAILABLE");
            itemDTO.setItemType(itemType);
            itemDTO.setConsumptionMode("ConsumptionModeBO:FIFO");
            itemDTO.setCreator("ADMIN");
            itemDTO.setUpdater("ADMIN");
            itemDTO.setCreateTime(now);
            itemDTO.setUpdateTime(now);
            this.save(itemDTO);
        }
    }
    // 確認是否有該物料，有則回報true，沒有則回報false
    @Override
    public boolean itemExist(String itemCode){
        Item item = itemMapper.findItemByName(itemCode);
        if(item == null)  return false;
        else return true;
    }

    // 取得物料包裝資訊
    @Override
    public String itemContainer(String itemCode){
        Item item = itemMapper.findItemByName(itemCode);
        if(item==null){
            return null;
        }
        else{
            ItemType itemType = itemTypeService.findItemTypeByHandle(item.getItemTypeBo());
            return itemType.getItemType();
        }
        /*
        if(item!=null) {

        }
        else */
    }
}