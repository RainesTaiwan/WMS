package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.ResourceTypeResourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.ResourceTypeResource;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/resourceTypeResource")
public class ResourceTypeResourceController {

    @Autowired
    public ResourceTypeResourceService resourceTypeResourceService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getResourceTypeResourceById(@PathVariable String id) {
        return AjaxResult.success( resourceTypeResourceService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getResourceTypeResourceList(ResourceTypeResource resourceTypeResource){
        List<ResourceTypeResource> result;
        QueryWrapper<ResourceTypeResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceTypeResource);
        result = resourceTypeResourceService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<ResourceTypeResource> frontPage, ResourceTypeResource resourceTypeResource){
        IPage result;
        QueryWrapper<ResourceTypeResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resourceTypeResource);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(ResourceTypeResource::getHandle, frontPage.getGlobalQuery())
                .or().like(ResourceTypeResource::getResourceTypeBo, frontPage.getGlobalQuery())
                .or().like(ResourceTypeResource::getResourceBo, frontPage.getGlobalQuery())
    );
        }
        result = resourceTypeResourceService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param resourceTypeResource  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("resourceTypeResource_add")
    public AjaxResult save(@RequestBody ResourceTypeResource resourceTypeResource) {
        return AjaxResult.success(resourceTypeResourceService.save(resourceTypeResource));
    }

    /**
     * 修改
     * @param resourceTypeResource  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("resourceTypeResource_edit")
    public AjaxResult updateById(@RequestBody ResourceTypeResource resourceTypeResource) {
        return AjaxResult.success(resourceTypeResourceService.updateById(resourceTypeResource));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("resourceTypeResource_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(resourceTypeResourceService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("resourceTypeResource_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(resourceTypeResourceService.removeByIds(ids));
    }
}