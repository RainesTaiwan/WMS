package com.sap.ewm.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.biz.dto.ItemDTO;
import com.sap.ewm.biz.model.ItemGroupMember;
import com.sap.ewm.biz.service.ItemGroupMemberService;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.biz.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author syngna
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    public ItemService itemService;

    @Autowired
    public ItemGroupMemberService itemGroupMemberService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getItemById(@PathVariable String id) {
        return AjaxResult.success(itemService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getItemList(Item item) {
        List<Item> result;
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(item);
        result = itemService.list(queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 分頁查詢數據
     *
     * @param frontPage 分頁資訊
     * @return
     */
    @ResponseBody
    @GetMapping("/page")
    public AjaxResult page(FrontPage<Item> frontPage, Item item) {
        IPage result;
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(item);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(Item::getHandle, frontPage.getGlobalQuery())
                    .or().like(Item::getItem, frontPage.getGlobalQuery())
                    .or().like(Item::getDescription, frontPage.getGlobalQuery())
                    .or().like(Item::getMixItem, frontPage.getGlobalQuery())
                    .or().like(Item::getInspectionRequired, frontPage.getGlobalQuery())
                    .or().like(Item::getItemStatus, frontPage.getGlobalQuery())
                    .or().like(Item::getItemTypeBo, frontPage.getGlobalQuery())
                    .or().like(Item::getConsumptionMode, frontPage.getGlobalQuery())
                    .or().like(Item::getUnitOfMeasure, frontPage.getGlobalQuery())
            );
        }
        result = itemService.page(frontPage.getPagePlus(), queryWrapper);
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Item> itemList = result.getRecords();
        List<String> itemBoList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            itemBoList = itemList.stream().map(Item::getHandle).collect(Collectors.toList());
        }
        if (itemBoList.size() > 0) {
            List<ItemGroupMember> itemGroupMemberList = itemGroupMemberService.list(Wrappers.<ItemGroupMember>lambdaQuery().in(ItemGroupMember::getItemBo, itemBoList));
            Map<String, List<String>> itemGroupMap = new HashMap<>();
            for (ItemGroupMember itemGroupMember : itemGroupMemberList) {
                List<String> itemGroupList = itemGroupMap.get(itemGroupMember.getItemBo());
                if (itemGroupList == null) {
                    itemGroupList = new ArrayList<>();
                    itemGroupMap.put(itemGroupMember.getItemBo(), itemGroupList);
                }
                itemGroupList.add(StringUtils.trimHandle(itemGroupMember.getItemGroupBo()));
            }
            for(Item it : itemList) {
                ItemDTO itemDTO = new ItemDTO();
                BeanUtils.copyProperties(it, itemDTO);
                itemDTO.setItemGroups(itemGroupMap.get(it.getHandle()));
                itemDTOList.add(itemDTO);
            }
            result.setRecords(itemDTOList);
        }

        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param item 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("item_add")
    public AjaxResult save(@RequestBody ItemDTO item) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        item.setCreator(user);
        item.setUpdater(user);
        item.setCreateTime(now);
        item.setUpdateTime(now);
        return AjaxResult.success(itemService.save(item));
    }

    /**
     * 修改
     *
     * @param item 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("item_edit")
    public AjaxResult updateById(@RequestBody ItemDTO item) {
        String user = CommonMethods.getUser();
        item.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        item.setUpdateTime(now);
        return AjaxResult.success(itemService.updateById(item));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("item_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(itemService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("item_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(itemService.removeByIds(ids));
    }
}