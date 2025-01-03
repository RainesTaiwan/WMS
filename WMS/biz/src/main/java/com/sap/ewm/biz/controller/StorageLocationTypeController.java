package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.StorageLocationTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.StorageLocationType;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/storage-location-type")
public class StorageLocationTypeController {

    @Autowired
    public StorageLocationTypeService storageLocationTypeService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getStorageLocationTypeById(@PathVariable String id) {
        return AjaxResult.success( storageLocationTypeService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getStorageLocationTypeList(StorageLocationType storageLocationType){
        List<StorageLocationType> result;
        QueryWrapper<StorageLocationType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocationType);
        result = storageLocationTypeService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<StorageLocationType> frontPage, StorageLocationType storageLocationType){
        IPage result;
        QueryWrapper<StorageLocationType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocationType);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(StorageLocationType::getHandle, frontPage.getGlobalQuery())
                .or().like(StorageLocationType::getStorageLocationType, frontPage.getGlobalQuery())
                .or().like(StorageLocationType::getDescription, frontPage.getGlobalQuery())
                .or().like(StorageLocationType::getCreator, frontPage.getGlobalQuery())
                .or().like(StorageLocationType::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = storageLocationTypeService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param storageLocationType  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("storage_location_type_add")
    public AjaxResult save(@RequestBody StorageLocationType storageLocationType) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        storageLocationType.setCreator(user);
        storageLocationType.setUpdater(user);
        storageLocationType.setCreateTime(now);
        storageLocationType.setUpdateTime(now);
        return AjaxResult.success(storageLocationTypeService.save(storageLocationType));
    }

    /**
     * 修改
     * @param storageLocationType  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("storage_location_type_edit")
    public AjaxResult updateById(@RequestBody StorageLocationType storageLocationType) {
		String user = CommonMethods.getUser();
        storageLocationType.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        storageLocationType.setUpdateTime(now);
        return AjaxResult.success(storageLocationTypeService.updateById(storageLocationType));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("storage_location_type_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(storageLocationTypeService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("storage_location_type_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(storageLocationTypeService.removeByIds(ids));
    }
}