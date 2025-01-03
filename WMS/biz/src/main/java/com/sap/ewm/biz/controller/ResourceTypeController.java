package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.ResourceTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.ResourceType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author syngna
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/resource-type")
public class ResourceTypeController {

    @Autowired
    public ResourceTypeService resourceTypeService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getResourceTypeById(@PathVariable String id) {
        return AjaxResult.success(resourceTypeService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getResourceTypeList(ResourceType resourceType) {
        List<ResourceType> result;
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceType);
        result = resourceTypeService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<ResourceType> frontPage, ResourceType resourceType) {
        IPage result;
        QueryWrapper<ResourceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceType);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(ResourceType::getHandle, frontPage.getGlobalQuery())
                    .or().like(ResourceType::getResourceType, frontPage.getGlobalQuery())
                    .or().like(ResourceType::getDescription, frontPage.getGlobalQuery())
            );
        }
        result = resourceTypeService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param resourceType 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("resource_type_add")
    public AjaxResult save(@RequestBody ResourceType resourceType) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        resourceType.setCreator(user);
        resourceType.setUpdater(user);
        resourceType.setCreateTime(now);
        resourceType.setUpdateTime(now);
        return AjaxResult.success(resourceTypeService.save(resourceType));
    }

    /**
     * 修改
     *
     * @param resourceType 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("resource_type_edit")
    public AjaxResult updateById(@RequestBody ResourceType resourceType) {
        String user = CommonMethods.getUser();
        resourceType.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        resourceType.setUpdateTime(now);
        return AjaxResult.success(resourceTypeService.updateById(resourceType));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("resource_type_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(resourceTypeService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("resource_type_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(resourceTypeService.removeByIds(ids));
    }
}