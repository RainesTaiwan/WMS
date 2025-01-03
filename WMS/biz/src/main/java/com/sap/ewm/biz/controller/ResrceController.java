package com.sap.ewm.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.biz.dto.ResourceDTO;
import com.sap.ewm.biz.model.ResourceTypeResource;
import com.sap.ewm.biz.service.ResourceTypeResourceService;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.biz.service.ResrceService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.Resrce;

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
@RequestMapping("/resrce")
public class ResrceController {

    @Autowired
    public ResrceService resrceService;

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
    public AjaxResult getResrceById(@PathVariable String id) {
        return AjaxResult.success(resrceService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getResrceList(Resrce resrce) {
        List<Resrce> result;
        QueryWrapper<Resrce> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resrce);
        result = resrceService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<Resrce> frontPage, Resrce resrce) {
        IPage result;
        QueryWrapper<Resrce> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(resrce);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(Resrce::getHandle, frontPage.getGlobalQuery())
                    .or().like(Resrce::getResrce, frontPage.getGlobalQuery())
                    .or().like(Resrce::getDescription, frontPage.getGlobalQuery())
                    .or().like(Resrce::getStatus, frontPage.getGlobalQuery())
            );
        }
        result = resrceService.page(frontPage.getPagePlus(), queryWrapper);

        List<ResourceDTO> resourceDTOList = new ArrayList<>();
        List<Resrce> resourceList = result.getRecords();
        List<String> resourceBoList = new ArrayList<>();
        if (resourceList != null && resourceList.size() > 0) {
            resourceBoList = resourceList.stream().map(Resrce::getHandle).collect(Collectors.toList());
        }
        if (resourceBoList.size() > 0) {
            List<ResourceTypeResource> resourceTypeResourceList = resourceTypeResourceService.list(Wrappers.<ResourceTypeResource>lambdaQuery().in(ResourceTypeResource::getResourceBo, resourceBoList));
            Map<String, List<String>> resourceTypeMap = new HashMap<>();
            for (ResourceTypeResource resourceTypeResource : resourceTypeResourceList) {
                List<String> resourceTypeList = resourceTypeMap.get(resourceTypeResource.getResourceBo());
                if (resourceTypeList == null) {
                    resourceTypeList = new ArrayList<>();
                    resourceTypeMap.put(resourceTypeResource.getResourceBo(), resourceTypeList);
                }
                resourceTypeList.add(StringUtils.trimHandle(resourceTypeResource.getResourceTypeBo()));
            }
            for(Resrce it : resourceList) {
                ResourceDTO resourceDTO = new ResourceDTO();
                BeanUtils.copyProperties(it, resourceDTO);
                resourceDTO.setResourceTypes(resourceTypeMap.get(it.getHandle()));
                resourceDTOList.add(resourceDTO);
            }
            result.setRecords(resourceDTOList);
        }
        
        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param resrce 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("resrce_add")
    public AjaxResult save(@RequestBody ResourceDTO resrce) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        resrce.setCreator(user);
        resrce.setUpdater(user);
        resrce.setCreateTime(now);
        resrce.setUpdateTime(now);
        return AjaxResult.success(resrceService.save(resrce));
    }

    /**
     * 修改
     *
     * @param resrce 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("resrce_edit")
    public AjaxResult updateById(@RequestBody ResourceDTO resrce) {
        String user = CommonMethods.getUser();
        resrce.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        resrce.setUpdateTime(now);
        return AjaxResult.success(resrceService.updateById(resrce));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("resrce_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(resrceService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("resrce_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(resrceService.removeByIds(ids));
    }
}