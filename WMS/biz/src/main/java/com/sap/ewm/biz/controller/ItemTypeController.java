package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemType;
import com.sap.ewm.biz.service.ItemTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/item-type")
public class ItemTypeController {

    @Autowired
    public ItemTypeService itemTypeService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getItemTypeById(@PathVariable String id) {
        return AjaxResult.success( itemTypeService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getItemTypeList(ItemType itemType){
        List<ItemType> result;
        QueryWrapper<ItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemType);
        result = itemTypeService.list(queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 分頁查詢數據
     *
     * @param frontPage  分頁資訊
     * @return
     */
    @ResponseBody
    @GetMapping("/page")
    public AjaxResult page(FrontPage<ItemType> frontPage, ItemType itemType){
        IPage result;
        QueryWrapper<ItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemType);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(ItemType::getHandle, frontPage.getGlobalQuery())
                .or().like(ItemType::getItemType, frontPage.getGlobalQuery())
                .or().like(ItemType::getMixItem, frontPage.getGlobalQuery())
                .or().like(ItemType::getDescription, frontPage.getGlobalQuery())
                .or().like(ItemType::getCreator, frontPage.getGlobalQuery())
                .or().like(ItemType::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = itemTypeService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param itemType  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("item_type_add")
    public AjaxResult save(@RequestBody ItemType itemType) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        itemType.setCreator(user);
        itemType.setUpdater(user);
        itemType.setCreateTime(now);
        itemType.setUpdateTime(now);
        return AjaxResult.success(itemTypeService.save(itemType));
    }

    /**
     * 修改
     * @param itemType  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("item_type_edit")
    public AjaxResult updateById(@RequestBody ItemType itemType) {
		String user = CommonMethods.getUser();
        itemType.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        itemType.setUpdateTime(now);
        return AjaxResult.success(itemTypeService.updateById(itemType));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("item_type_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(itemTypeService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("item_type_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(itemTypeService.removeByIds(ids));
    }
}