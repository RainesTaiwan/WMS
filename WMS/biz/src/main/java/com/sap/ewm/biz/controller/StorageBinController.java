package com.sap.ewm.biz.controller;

import com.sap.ewm.biz.dto.StorageBinDTO;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.StorageBinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.StorageBin;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author syngna
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/storage-bin")
public class StorageBinController {

    @Autowired
    public StorageBinService storageBinService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getStorageBinById(@PathVariable String id) {
        return AjaxResult.success(storageBinService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getStorageBinList(StorageBin storageBin) {
        List<StorageBin> result;
        QueryWrapper<StorageBin> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBin);
        result = storageBinService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<StorageBin> frontPage, StorageBin storageBin) {
        IPage result;
        QueryWrapper<StorageBin> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBin);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(StorageBin::getHandle, frontPage.getGlobalQuery())
                    .or().like(StorageBin::getStorageBin, frontPage.getGlobalQuery())
                    .or().like(StorageBin::getDescription, frontPage.getGlobalQuery())
                    .or().like(StorageBin::getStorageBinTypeBo, frontPage.getGlobalQuery())
                    .or().like(StorageBin::getStorageLocationBo, frontPage.getGlobalQuery())
                    .or().like(StorageBin::getCreator, frontPage.getGlobalQuery())
                    .or().like(StorageBin::getUpdater, frontPage.getGlobalQuery())
            );
        }
        result = storageBinService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param storageBin 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("storage_bin_add")
    public AjaxResult save(@RequestBody StorageBinDTO storageBin) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        storageBin.setCreator(user);
        storageBin.setUpdater(user);
        storageBin.setCreateTime(now);
        storageBin.setUpdateTime(now);
        return AjaxResult.success(storageBinService.save(storageBin));
    }

    /**
     * 修改
     *
     * @param storageBin 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("storage_bin_edit")
    public AjaxResult updateById(@RequestBody StorageBinDTO storageBin) {
        String user = CommonMethods.getUser();
        storageBin.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        storageBin.setUpdateTime(now);
        return AjaxResult.success(storageBinService.updateById(storageBin));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("storage_bin_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(storageBinService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("storage_bin_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(storageBinService.removeByIds(ids));
    }
}