package com.sap.ewm.biz.controller;

import com.sap.ewm.biz.dto.StorageLocationDTO;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.StorageLocationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.StorageLocation;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author syngna
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/storage-location")
public class StorageLocationController {

    @Autowired
    public StorageLocationService storageLocationService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getStorageLocationById(@PathVariable String id) {
        return AjaxResult.success( storageLocationService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getStorageLocationList(StorageLocation storageLocation){
        List<StorageLocation> result;
        QueryWrapper<StorageLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocation);
        result = storageLocationService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<StorageLocation> frontPage, StorageLocation storageLocation){
        IPage result;
        QueryWrapper<StorageLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageLocation);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(StorageLocation::getHandle, frontPage.getGlobalQuery())
                .or().like(StorageLocation::getStorageLocation, frontPage.getGlobalQuery())
                .or().like(StorageLocation::getDescription, frontPage.getGlobalQuery())
                .or().like(StorageLocation::getStorageLocationTypeBo, frontPage.getGlobalQuery())
                .or().like(StorageLocation::getWarehouseBo, frontPage.getGlobalQuery())
                .or().like(StorageLocation::getCreator, frontPage.getGlobalQuery())
                .or().like(StorageLocation::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = storageLocationService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param storageLocation  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("storage_location_add")
    public AjaxResult save(@RequestBody StorageLocationDTO storageLocation) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        storageLocation.setCreator(user);
        storageLocation.setUpdater(user);
        storageLocation.setCreateTime(now);
        storageLocation.setUpdateTime(now);
        return AjaxResult.success(storageLocationService.save(storageLocation));
    }

    /**
     * 修改
     * @param storageLocation  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("storage_location_edit")
    public AjaxResult updateById(@RequestBody StorageLocationDTO storageLocation) {
		String user = CommonMethods.getUser();
        storageLocation.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        storageLocation.setUpdateTime(now);
        return AjaxResult.success(storageLocationService.updateById(storageLocation));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("storage_location_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(storageLocationService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("storage_location_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(storageLocationService.removeByIds(ids));
    }
}