package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.ItemGroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.ItemGroup;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author syngna
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/item-group")
public class ItemGroupController {

    @Autowired
    public ItemGroupService itemGroupService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getItemGroupById(@PathVariable String id) {
        return AjaxResult.success(itemGroupService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getItemGroupList(ItemGroup itemGroup) {
        List<ItemGroup> result;
        QueryWrapper<ItemGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroup);
        result = itemGroupService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<ItemGroup> frontPage, ItemGroup itemGroup) {
        IPage result;
        QueryWrapper<ItemGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroup);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(ItemGroup::getHandle, frontPage.getGlobalQuery())
                    .or().like(ItemGroup::getItemGroup, frontPage.getGlobalQuery())
                    .or().like(ItemGroup::getDescription, frontPage.getGlobalQuery())
                    .or().like(ItemGroup::getConsumptionMode, frontPage.getGlobalQuery())
            );
        }
        result = itemGroupService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param itemGroup 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("item_group_add")
    public AjaxResult save(@RequestBody ItemGroup itemGroup) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        itemGroup.setCreator(user);
        itemGroup.setUpdater(user);
        itemGroup.setCreateTime(now);
        itemGroup.setUpdateTime(now);
        return AjaxResult.success(itemGroupService.save(itemGroup));
    }

    /**
     * 修改
     *
     * @param itemGroup 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("item_group_edit")
    public AjaxResult updateById(@RequestBody ItemGroup itemGroup) {
        String user = CommonMethods.getUser();
        itemGroup.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        itemGroup.setUpdateTime(now);
        return AjaxResult.success(itemGroupService.updateById(itemGroup));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("item_group_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(itemGroupService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("item_group_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(itemGroupService.removeByIds(ids));
    }
}